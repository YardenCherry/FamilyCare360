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
import demo.app.logics.ObjectLogic;
import demo.app.objects.ObjectId;
import jakarta.annotation.PostConstruct;

@Service
public class ObjectCrudImplementation implements ObjectLogic {
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
		System.err.println("*** " + name);
	}

	@PostConstruct
	public void setupIsDone() {
		System.err.println("demo logic implementation is ready");
	}

	@Override
	@Transactional(readOnly = false)
	public ObjectBoundary storeInDatabase(ObjectBoundary objectBoundary) {
		validateObjectBoundary(objectBoundary);
		ObjectId objectId = new ObjectId();
		objectId.setId(UUID.randomUUID().toString());
		objectId.setSuperApp(springApplicationName);
		objectBoundary.setObjectId(objectId);
		objectBoundary.setCreationTimestamp(new Date());
		objectBoundary.getCreatedBy().getUserId().setSuperapp(springApplicationName);
		ObjectEntity entity = this.objectConverter.toEntity(objectBoundary);

		entity = this.objectCrud.save(entity);
		return this.objectConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ObjectBoundary> getSpecificObject(String id, String superapp) {
		String objectId = id + "_" + superapp;
		return this.objectCrud.findById(objectId).map(entity -> this.objectConverter.toBoundary(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAll() {
		List<ObjectEntity> entities = this.objectCrud.findAll();

		List<ObjectBoundary> rv = new ArrayList<>();

		for (ObjectEntity entity : entities) {
			rv.add(this.objectConverter.toBoundary(entity));
		}

		return rv;
	}

	@Override
	public Optional<ObjectBoundary> updateById(String id, String superapp, ObjectBoundary update) {
		validateObjectBoundary(update);
		String objectId = id + "_" + superapp;
		ObjectEntity existing = this.objectCrud.findById(objectId)
				.orElseThrow(() -> new RuntimeException("could not find demo with id: " + id));
		ObjectEntity temp = objectConverter.toEntity(update);
		if (update.getActive() != null)
			existing.setActive(temp.getActive());
		if (update.getType() != null)
			existing.setType(temp.getType());
		if (update.getAlias() != null)
			existing.setAlias(temp.getAlias());
		if (update.getLocation() != null)
			existing.setLocation(temp.getLocation());
		if (update.getObjectDetails() != null )
			existing.setObjectDetails(temp.getObjectDetails());
		this.objectCrud.save(existing);
		
		System.err.println("Updated in database: " + existing);
        return Optional.of(update);
	}
	
	private void validateObjectBoundary(ObjectBoundary objectBoundary) {
        if (objectBoundary == null) {
            throw new MyBadRequestException("ObjectBoundary cannot be null.");
        }
        if (objectBoundary.getObjectId() == null || objectBoundary.getObjectId().getSuperApp() == null) {
            throw new MyBadRequestException("Object ID and SuperApp cannot be null.");
        }
	}

}

