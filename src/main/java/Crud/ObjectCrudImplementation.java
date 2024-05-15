package Crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Boundaries.ObjectBoundary;
import Entities.ObjectEntity;
import Logics.ObjectLogic;
import Objects.ObjectId;

@Service
public class ObjectCrudImplementation implements ObjectLogic {
	private ObjectCrud objectCrud ;

	public ObjectCrudImplementation(ObjectCrud objectCrud) {
		this.objectCrud = objectCrud;
	}
	
	@Override
	@Transactional(readOnly = false)
	public ObjectBoundary storeInDatabase(ObjectBoundary objectBoundary)
	{
		ObjectId objectId=new ObjectId();
		objectId.setId(UUID.randomUUID().toString());
		objectBoundary.setObjectID(objectId);
		ObjectEntity entity = objectBoundary.toEntity();
		
		entity = this.objectCrud.save(entity);
		return new ObjectBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ObjectBoundary> getSpecificDemoFromDatabase(String id) {
		return this.objectCrud
			.findById(id)
			.map(entity->new ObjectBoundary(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAll() {
		List<ObjectEntity> entities = 
		  this.objectCrud
			.findAll();
		
		List<ObjectBoundary> rv = new ArrayList<>();
		
		for (ObjectEntity entity : entities) {
			rv.add(new ObjectBoundary(entity));
		}
		
		return rv;
	}

	@Override
	public Optional<Void> updateObject(ObjectBoundary miniAppCommandBoundary, String id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	
	
}
