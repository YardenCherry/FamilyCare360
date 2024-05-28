package demo.app.crud;

import java.util.ArrayList;
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
	private ObjectCrud objectCrud ;
	private ObjectConverter objectConverter;

	public ObjectCrudImplementation(ObjectCrud objectCrud,ObjectConverter objectConverter ) {
		this.objectCrud = objectCrud;
		this.objectConverter = objectConverter;
	}
	
	@Value("${spring.application.name:supperApp}")
	public void setup(String name) {
		System.err.println("*** " + name);
	}

	@PostConstruct  
	public void setupIsDone() {
		System.err.println("demo logic implementation is ready");
	}
	
	@Override
	@Transactional(readOnly = false)
	public ObjectBoundary storeInDatabase(ObjectBoundary objectBoundary)
	{
		ObjectId objectId=new ObjectId();
		objectId.setId(UUID.randomUUID().toString());
		objectBoundary.setObjectId(objectId);
		ObjectEntity entity = this.objectConverter.toEntity(objectBoundary);
		
		entity = this.objectCrud.save(entity);
		return this.objectConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ObjectBoundary> getSpecificDemoFromDatabase(String id) {
		return this.objectCrud
			.findById(id)
			.map(entity->this.objectConverter.toBoundary(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAll() {
		List<ObjectEntity> entities = 
		  this.objectCrud
			.findAll();
		
		List<ObjectBoundary> rv = new ArrayList<>();
		
		for (ObjectEntity entity : entities) {
			rv.add(this.objectConverter.toBoundary(entity));
		}
		
		return rv;
	}

	@Override
	@Transactional//(readOnly = false)
	public void deleteAll() {
		this.objectCrud.deleteAll();
		
	}

	@Override
	public void updateById(String id, ObjectBoundary update) {
		ObjectEntity existing = 
				  this.objectCrud
					.findById(id)
					.orElseThrow(()->new RuntimeException("could not find demo with id: " + id));
		
		// add the checks after ask eyal
		
		this.objectCrud
		.save(existing);
		
	}
	
	
}