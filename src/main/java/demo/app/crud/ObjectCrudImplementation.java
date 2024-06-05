package demo.app.crud;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.app.boundaries.ObjectBoundary;
import demo.app.converters.ObjectConverter;
import demo.app.entities.ObjectEntity;
import demo.app.logics.EnhancedObjectLogic;
import demo.app.logics.ObjectLogic;
import demo.app.objects.InputValidation;
import demo.app.objects.ObjectId;
import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
@Service
public class ObjectCrudImplementation implements EnhancedObjectLogic {
	private ObjectCrud objectCrud;
	private ObjectConverter objectConverter;
	private String springApplicationName;

	public ObjectCrudImplementation(ObjectCrud objectCrud, ObjectConverter objectConverter) {
		this.objectCrud = objectCrud;
		this.objectConverter = objectConverter;
	}

	@Value("${spring.application.name:supperapp}")
	public void setup(String name) {
		this.springApplicationName = name;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}

	@PostConstruct
	public void setupIsDone() {
		System.err.println("demo logic implementation is ready");
	}

	@Override
	@Transactional(readOnly = false)
	public ObjectBoundary storeInDatabase(ObjectBoundary objectBoundary) {
		ObjectId objectId = new ObjectId();
		objectId.setId(UUID.randomUUID().toString());
		objectId.setSuperApp(springApplicationName);
		objectBoundary.setObjectId(objectId);

		objectBoundary.setCreationTimestamp(new Date());
		objectBoundary.getCreatedBy().getUserId().setSuperapp(springApplicationName);

		validateObjectBoundary(objectBoundary);

		ObjectEntity entity = this.objectConverter.toEntity(objectBoundary);

		entity = this.objectCrud.save(entity);
		System.err.println("Saved in DB the object: " + entity);
		return this.objectConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ObjectBoundary> getSpecificObject(String id, String superapp) {
		String objectId = id + "_" + superapp;
		return this.objectCrud.findById(objectId).map(entity -> this.objectConverter.toBoundary(entity));
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
	public List<ObjectBoundary> getAll(int size, int page) {
		List<ObjectEntity> entities =
				this.objectCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, "objectID"))
				.toList();
	
				List<ObjectBoundary> rv = new ArrayList<>();
		
				for (ObjectEntity entity : entities) {
					rv.add(this.objectConverter.toBoundary(entity));
				}
		
				return rv;
	}
	@Override
	public Optional<ObjectBoundary> updateById(String id, String superapp, ObjectBoundary update) {
		String objectId = id + "_" + superapp;
		ObjectEntity existing = this.objectCrud.findById(objectId)
				.orElseThrow(() -> new RuntimeException("could not find demo with id: " + id));
		ObjectEntity temp = objectConverter.toEntity(update);
		if (update.getActive() != null)
			existing.setActive(temp.getActive());
		if (update.getLocation() != null)
			existing.setLocation(temp.getLocation());
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
	public List<ObjectBoundary> getObjectByType(String type, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ObjectBoundary> getObjectByAlias(String alias, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ObjectBoundary> getObjectByAliasPattern(String aliasPattern, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ObjectBoundary> getObjectByLocation(String location, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

}
