package demo.app.crud;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.app.boundaries.ObjectBoundary;
import demo.app.converters.ObjectConverter;
import demo.app.entities.ObjectEntity;
import demo.app.entities.UserEntity;
import demo.app.enums.Role;
import demo.app.logics.EnhancedObjectLogic;
import demo.app.objects.InputValidation;
import demo.app.objects.ObjectId;
import jakarta.annotation.PostConstruct;

@Service
public class ObjectCrudImplementation implements EnhancedObjectLogic {
	private ObjectCrud objectCrud;
	private ObjectConverter objectConverter;
	private String springApplicationName;
	private UserCrud userCrud;

	public ObjectCrudImplementation(ObjectCrud objectCrud, ObjectConverter objectConverter, UserCrud userCrud) {
		this.objectCrud = objectCrud;
		this.objectConverter = objectConverter;
		this.userCrud = userCrud;

	}

	@Value("${spring.application.name:supperapp}")
	public void setup(String name) {
		this.springApplicationName = name;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}

	@PostConstruct
	public void setupIsDone() {
		System.err.println("Object logic implementation is ready");
	}

	@Override
	@Transactional(readOnly = false)
	public ObjectBoundary storeInDatabase(ObjectBoundary objectBoundary) {
		ObjectId objectId = new ObjectId();
		objectId.setId(UUID.randomUUID().toString());
		objectId.setSuperapp(springApplicationName);
		objectBoundary.setObjectId(objectId);

		objectBoundary.setCreationTimestamp(new Date());
		objectBoundary.getCreatedBy().getUserId().setSuperapp(springApplicationName);

		// Verify user permissions
		UserEntity user = userCrud
				.findById(objectBoundary.getCreatedBy().getUserId().getSuperapp() + "_"
						+ objectBoundary.getCreatedBy().getUserId().getEmail())
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));
		if (!user.getRole().equals(Role.SUPERAPP_USER)) {
			throw new MyForbiddenException("User not authorized");
		}

		validateObjectBoundary(objectBoundary);

		ObjectEntity entity = this.objectConverter.toEntity(objectBoundary);

		entity = this.objectCrud.save(entity);
		System.err.println("Saved in DB the object: " + entity);
		return this.objectConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ObjectBoundary> getSpecificObject(String id, String superapp, String userSuperapp,
			String userEmail) {
		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));

		if (!user.getRole().equals(Role.SUPERAPP_USER) && !user.getRole().equals(Role.MINIAPP_USER)) {
			throw new MyForbiddenException("User is not authorized");
		}

		String objectId = id + "_" + superapp;
		ObjectEntity entity = this.objectCrud.findById(objectId).orElse(null);

		if (entity == null) {
			return Optional.empty();
		}

		return Optional.of(this.objectConverter.toBoundary(entity));
	}

	@Override
	@Deprecated
	public List<ObjectBoundary> getAll() {
		throw new MyBadRequestException("deprecated operation");
//		List<ObjectEntity> entities = this.objectCrud.findAll();
//
//		List<ObjectBoundary> rv = new ArrayList<>();
//
//		for (ObjectEntity entity : entities) {
//			rv.add(this.objectConverter.toBoundary(entity));
//		}
//
//		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAll(int size, int page, String userSuperapp, String userEmail) {
		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized1"));

		if (!user.getRole().equals(Role.SUPERAPP_USER) && !user.getRole().equals(Role.MINIAPP_USER)) {
			throw new MyForbiddenException("User is not authorized");
		}

		List<ObjectEntity> entities;
		if (user.getRole().equals(Role.MINIAPP_USER)) {
			entities = this.objectCrud.findAllByActive(true, PageRequest.of(page, size, Direction.ASC, "objectID"))
					.stream().toList();
		} else {
			entities = this.objectCrud.findAll(PageRequest.of(page, size, Direction.ASC, "objectID")).toList();
		}
		List<ObjectBoundary> rv = new ArrayList<>();

		for (ObjectEntity entity : entities) {
			rv.add(this.objectConverter.toBoundary(entity));
		}

		return rv;
	}

	@Override
	public Optional<ObjectBoundary> updateById(String id, String superapp, String userSuperapp, String userEmail,
			ObjectBoundary update) {
		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));

		if (!user.getRole().equals(Role.SUPERAPP_USER)) {
			throw new MyForbiddenException("User is not authorized");
		}

		String objectId = id + "_" + superapp;
		ObjectEntity existing = this.objectCrud.findById(objectId).orElse(null);

		ObjectEntity temp = objectConverter.toEntity(update);
	
		if (update.getType() != null)
			existing.setType(temp.getType());
		if (update.getAlias() != null)
			existing.setAlias(temp.getAlias());
		if (update.getActive() != null)
			existing.setActive(temp.getActive());
		if (update.getObjectDetails() != null)
			existing.setObjectDetails(temp.getObjectDetails());

		this.objectCrud.save(existing);

		System.err.println("Updated in database: " + existing);
		return Optional.of(update);
	}

	private void validateObjectBoundary(ObjectBoundary objectBoundary) {
		if (objectBoundary == null) {
			throw new MyBadRequestException("ObjectBoundary cannot be null.");
		}

		if (objectBoundary.getType() == null || objectBoundary.getType().isBlank()) {
			throw new MyBadRequestException("Object type cannot be null.");
		}

		if (objectBoundary.getAlias() == null || objectBoundary.getAlias().isBlank()) {
			throw new MyBadRequestException("Object alias cannot be null.");
		}

		if (objectBoundary.getCreatedBy() == null || objectBoundary.getCreatedBy().getUserId() == null
				|| !InputValidation.isValidEmail(objectBoundary.getCreatedBy().getUserId().getEmail())) {
			throw new MyBadRequestException("CreatedBy and email cannot be null.");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllByAlias(String alias, int size, int page, String userSuperapp, String userEmail) {
		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));

		if (!user.getRole().equals(Role.SUPERAPP_USER) && !user.getRole().equals(Role.MINIAPP_USER)) {
			throw new MyForbiddenException("User is not authorized");
		}

		List<ObjectEntity> entities;
		if (user.getRole().equals(Role.MINIAPP_USER)) {
			entities = this.objectCrud.findAllByAliasAndActive(alias, true,
					PageRequest.of(page, size, Direction.ASC, "alias", "objectID")).stream().toList();
		} else {
			entities = this.objectCrud
					.findAllByAlias(alias, PageRequest.of(page, size, Direction.ASC, "alias", "objectID")).stream()
					.toList();
		}

		List<ObjectBoundary> rv = new ArrayList<>();

		for (ObjectEntity entity : entities) {
			rv.add(this.objectConverter.toBoundary(entity));
		}

		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllByType(String type, int size, int page, String userSuperapp, String userEmail) {
		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized1"));

		if (!user.getRole().equals(Role.SUPERAPP_USER) && !user.getRole().equals(Role.MINIAPP_USER)) {
			throw new MyForbiddenException("User is not authorized2");
		}

		List<ObjectEntity> entities;
		if (user.getRole().equals(Role.MINIAPP_USER)) {
			entities = this.objectCrud
					.findAllByTypeAndActive(type, true, PageRequest.of(page, size, Direction.ASC, "objectID"))
					.stream().toList();
		} else {
			entities = this.objectCrud
					.findAllByType(type, PageRequest.of(page, size, Direction.ASC, "objectID")).stream()
					.toList();
		}

		List<ObjectBoundary> rv = new ArrayList<>();

		for (ObjectEntity entity : entities) {
			rv.add(this.objectConverter.toBoundary(entity));
		}

		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllByAliasPattern(String pattern, int size, int page, String userSuperapp,
			String userEmail) {
		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));

		if (!user.getRole().equals(Role.SUPERAPP_USER) && !user.getRole().equals(Role.MINIAPP_USER)) {
			throw new MyForbiddenException("User is not authorized");
		}

		List<ObjectEntity> entities;
		if (user.getRole().equals(Role.MINIAPP_USER)) {
			entities = this.objectCrud
					.findAllByAliasLikeIgnoreCaseAndActive(pattern, true,
							PageRequest.of(page, size, Direction.ASC, "creationTimestamp", "objectID"))
					.stream().toList();
		} else {
			entities = this.objectCrud
					.findAllByAliasLikeIgnoreCase(pattern,
							PageRequest.of(page, size, Direction.ASC, "creationTimestamp", "objectID"))
					.stream().toList();
		}

		List<ObjectBoundary> rv = new ArrayList<>();

		for (ObjectEntity entity : entities) {
			rv.add(this.objectConverter.toBoundary(entity));
		}

		return rv;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllByLocation(double lat, double lng, double distance, String distanceUnits,
			int size, int page, String userSuperapp, String userEmail) {
		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));

		if (!user.getRole().equals(Role.SUPERAPP_USER) && !user.getRole().equals(Role.MINIAPP_USER)) {
			throw new MyForbiddenException("User is not authorized");
		}
		distanceUnits = distanceUnits.toUpperCase();
		Double units= null;
		for(Metrics val : Metrics.values()) {
			if(val.toString().equals(distanceUnits))
				units=Metrics.valueOf(distanceUnits).getMultiplier();
		}
		if(units==null)
			throw new MyBadRequestException("Distance Units is not authorized");

		List<ObjectEntity> entities;
		if (user.getRole().equals(Role.MINIAPP_USER)) {
			
			entities = this.objectCrud
					.findAllByLocationWithinAndActive(lat, lng, distance, units,
							true, PageRequest.of(page, size, Direction.ASC,"objectID"))
					.stream()
					.toList();
		} else {
			entities = this.objectCrud
					.findAllByLocationWithin(lat, lng, distance, units,
						PageRequest.of(page, size, Direction.ASC,"objectID"))
					.stream()
					.toList();
		}

		List<ObjectBoundary> rv = new ArrayList<>();
		for (ObjectEntity entity : entities) {
			rv.add(this.objectConverter.toBoundary(entity));
		}

		return rv;
	}

//	@Override
//	@Transactional(readOnly = true)
//	public List<ObjectBoundary> getAllByLocation(double lat, double lng, double distance, String distanceUnits,
//			int size, int page, String userSuperapp, String userEmail) {
//		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
//				.orElseThrow(() -> new MyForbiddenException("User not authorized"));
//
//		if (!user.getRole().equals(Role.SUPERAPP_USER) && !user.getRole().equals(Role.MINIAPP_USER)) {
//			throw new MyForbiddenException("User is not authorized");
//		}
//
//		// Convert distance to neutral units (assume kilometers for simplicity)
//		final double radius;
//		if ("MILES".equalsIgnoreCase(distanceUnits)) {
//			radius = distance * 1.60934; // Convert miles to kilometers
//		} else {
//			radius = distance; // Assume the distance is in kilometers
//		}
//
//		List<ObjectEntity> entities;
//		if (user.getRole().equals(Role.MINIAPP_USER)) {
//			entities = this.objectCrud
//					.findAllByLocationWithinAndActive(
//							lat, lng, radius, true, PageRequest.of(page, size, Direction.ASC, "location", "objectID"))
//					.stream()
//					.filter(entity -> calculateDistance(lat, lng,
//							Double.parseDouble(entity.getLocation().split("_")[0]),
//							Double.parseDouble(entity.getLocation().split("_")[1])) <= radius)
//					.toList();
//		} else {
//			entities = this.objectCrud
//					.findAllByLocationWithin(
//							lat, lng, radius, PageRequest.of(page, size, Direction.ASC, "location", "objectID"))
//					.stream()
//					.filter(entity -> calculateDistance(lat, lng,
//							Double.parseDouble(entity.getLocation().split("_")[0]),
//							Double.parseDouble(entity.getLocation().split("_")[1])) <= radius)
//					.toList();
//		}
//
//		List<ObjectBoundary> rv = new ArrayList<>();
//		for (ObjectEntity entity : entities) {
//			rv.add(this.objectConverter.toBoundary(entity));
//		}
//
//		return rv;
//	}

	// Utility method to calculate distance between two coordinates in kilometers
//	private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
//		final int EARTH_RADIUS = 6371; // Radius of the earth in kilometers
//		double latDistance = Math.toRadians(lat2 - lat1);
//		double lngDistance = Math.toRadians(lng2 - lng1);
//		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
//				* Math.cos(Math.toRadians(lat2)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
//		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//		return EARTH_RADIUS * c; // Distance in kilometers
//	}

}
