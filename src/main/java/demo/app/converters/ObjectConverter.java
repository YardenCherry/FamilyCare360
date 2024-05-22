package demo.app.converters;

import demo.app.boundaries.ObjectBoundary;
import demo.app.entities.ObjectEntity;
import demo.app.objects.CreatedBy;
import demo.app.objects.ObjectId;

public class ObjectConverter {
	public ObjectBoundary toBounday (ObjectEntity entity) {
		ObjectBoundary rv = new ObjectBoundary();
		
		ObjectId ob= new ObjectId();
		ob.setId(entity.getObjectID().split("_")[0]);
		ob.setSuperApp(entity.getObjectID().split("_")[1]);
		rv.setObjectID(ob);
		rv.setType(entity.getType());
		rv.setAlias(entity.getAlias());
		rv.setLocation(entity.getLocation());
		rv.setActive(entity.getActive());
		rv.setCreationTimeStamp(entity.getCreationTimeStamp());
		rv.setCreatedBy(entity.getCreatedBy());
		rv.setObjectDetails(entity.getObjectDetails());
	
		return rv;
	}
	

	public ObjectEntity toEntity (ObjectBoundary boundary) {
		ObjectEntity rv = new ObjectEntity();
		
		rv.setObjectID(String.join("_",boundary.getObjectID().getId(), boundary.getObjectID().getSuperApp()));
		rv.setType(boundary.getType());
		rv.setAlias(boundary.getAlias());
		rv.setCreationTimeStamp(boundary.getCreationTimeStamp());
		rv.setObjectDetails(boundary.getObjectDetails());
		rv.setLocation(boundary.getLocation());
		if (boundary.getActive() != null) {
			rv.setActive(boundary.getActive());
		}else {
			rv.setActive(false);
		}
		
		if (boundary.getCreatedBy() != null) {
			rv.setCreatedBy(boundary.getCreatedBy());
		}else {
			rv.setCreatedBy(new CreatedBy());
		}
		
		if (boundary.getCreatedBy() != null) {
			rv.setCreatedBy(boundary.getCreatedBy());
		}else {
			CreatedBy cb = new CreatedBy();
			/// check with eyal
			rv.setCreatedBy(new CreatedBy());
		}
		
		return rv;

	}
	
}