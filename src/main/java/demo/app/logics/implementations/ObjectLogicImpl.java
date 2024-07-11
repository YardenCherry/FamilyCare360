package demo.app.logics.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.app.boundaries.ObjectBoundary;
import demo.app.converters.ObjectConverter;
import demo.app.crud.ObjectCrud;
import demo.app.crud.UserCrud;
import demo.app.entities.ObjectEntity;
import demo.app.entities.UserEntity;
import demo.app.enums.Role;
import demo.app.logics.EnhancedObjectLogic;
import demo.app.objects.InputValidation;
import demo.app.objects.ObjectId;
import jakarta.annotation.PostConstruct;

@Service
public class ObjectLogicImpl implements EnhancedObjectLogic {
	private ObjectCrud objectCrud;
	private ObjectConverter objectConverter;
	private String springApplicationName;
	private UserCrud userCrud;

	public ObjectLogicImpl(ObjectCrud objectCrud, ObjectConverter objectConverter, UserCrud userCrud) {
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
				.orElseThrow(() -> new MyForbiddenException("User not authorized1"));
		if (!user.getRole().equals(Role.SUPERAPP_USER)) {
			throw new MyForbiddenException("User not authorized2");
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
		ObjectEntity entity;
		if (user.getRole().equals(Role.SUPERAPP_USER))
			entity = this.objectCrud.findByObjectId(objectId).orElse(null);
		else
			entity = this.objectCrud.findByObjectIdAndActiveTrue(objectId).orElse(null);

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
			entities = this.objectCrud.findAllByActive(true, PageRequest.of(page, size, Direction.ASC, "objectId"))
					.stream().toList();
		} else {
			entities = this.objectCrud.findAll(PageRequest.of(page, size, Direction.ASC, "objectId")).toList();
		}
		List<ObjectBoundary> rv = new ArrayList<>();

		for (ObjectEntity entity : entities) {
			rv.add(this.objectConverter.toBoundary(entity));
		}

		return rv;
	}

	@Override
	@Transactional(readOnly = false)
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
		if (update.getLocation() != null)
			existing.setLocation(update.getLocation().getLat(), update.getLocation().getLng());
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
			throw new MyForbiddenException("User is not authorized2");
		}

		List<ObjectEntity> entities;
		if (user.getRole().equals(Role.MINIAPP_USER)) {
			entities = this.objectCrud.findAllByAliasAndActive(alias, true,
					PageRequest.of(page, size, Direction.ASC, "objectId")).stream().toList();
		} else {
			entities = this.objectCrud
					.findAllByAlias(alias, PageRequest.of(page, size, Direction.ASC,"objectId")).stream()
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
					.findAllByTypeAndActive(type, true, PageRequest.of(page, size, Direction.ASC, "objectId")).stream()
					.toList();
		} else {
			entities = this.objectCrud.findAllByType(type, PageRequest.of(page, size, Direction.ASC, "objectId"))
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
	public List<ObjectBoundary> getAllByAliasPattern(String pattern, int size, int page, String userSuperapp,
			String userEmail) {
		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));

		if (!user.getRole().equals(Role.SUPERAPP_USER) && !user.getRole().equals(Role.MINIAPP_USER)) {
			throw new MyForbiddenException("User is not authorized");
		}
		
		pattern="%"+pattern+"%";
		
		List<ObjectEntity> entities;
		if (user.getRole().equals(Role.MINIAPP_USER)) {
			entities = this.objectCrud
					.findAllByAliasLikeIgnoreCaseAndActive(pattern, true,
							PageRequest.of(page, size, Direction.ASC, "creationTimestamp", "objectId"))
					.stream().toList();
		} else {
			entities = this.objectCrud
					.findAllByAliasLikeIgnoreCase(pattern,
							PageRequest.of(page, size, Direction.ASC, "creationTimestamp", "objectId"))
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
		
		double radius= getUnit(distanceUnits)*distance;
		
		List<ObjectEntity> entities;
		if (user.getRole().equals(Role.MINIAPP_USER)) {
			entities = this.objectCrud.findAllByLocationWithinAndActiveTrue(lat, lng, radius,
					PageRequest.of(page, size, Direction.ASC, "object_id")).stream().toList();
		} else {
			entities = this.objectCrud.findAllByLocationWithin(lat, lng, radius,
					PageRequest.of(page, size, Direction.ASC, "object_id")).stream().toList();
		}

		List<ObjectBoundary> rv = new ArrayList<>();
		for (ObjectEntity entity : entities) {
			rv.add(this.objectConverter.toBoundary(entity));
		}

		return rv;
	}
	
	private double getUnit(String distanceUnits) {
		if(distanceUnits.equalsIgnoreCase("KILOMETERS"))
			return 1000;
		else if (distanceUnits.equalsIgnoreCase("MILES"))
			return 1609.344;
		else if (distanceUnits.equalsIgnoreCase("NEUTRAL"))
			return 111195;
		else
			throw new MyBadRequestException("Distance Units is not authorized");
	}

}
