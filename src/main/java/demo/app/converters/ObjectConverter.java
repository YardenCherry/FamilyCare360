package demo.app.converters;

import org.springframework.stereotype.Component;

import demo.app.boundaries.ObjectBoundary;
import demo.app.entities.ObjectEntity;
import demo.app.objects.CreatedBy;
import demo.app.objects.Location;
import demo.app.objects.ObjectId;
import demo.app.objects.UserId;
@Component
public class ObjectConverter {
	public ObjectBoundary toBoundary(ObjectEntity entity) {
		ObjectBoundary boundary = new ObjectBoundary();
		
		ObjectId ob= new ObjectId();
		ob.setId(entity.getObjectID().split("_")[0]);
		ob.setSuperApp(entity.getObjectID().split("_")[1]);
		boundary.setObjectId(ob);
		boundary.setType(entity.getType());
		boundary.setAlias(entity.getAlias());
		boundary.setActive(entity.getActive());	
		boundary.setCreationTimesTamp(entity.getCreationTimeStamp());
		boundary.setObjectDetails(entity.getObjectDetails());
		String[] location = entity.getLocation().split("#");
		boundary.setLocation(new Location(Double.parseDouble(location[0]) 
				, Double.parseDouble(location[1])));
		String[] createdBy = entity.getCreatedBy().split("#");
		UserId userId= new UserId();
		userId.setSuperapp(createdBy[0]);
		userId.setEmail(createdBy[1]);
		boundary.setCreatedBy(new CreatedBy(userId));

		return boundary;
	}
	

	public ObjectEntity toEntity (ObjectBoundary boundary) {
		ObjectEntity entity = new ObjectEntity();
		
		entity.setObjectID(String.join("_",boundary.getObjectId().getId(), boundary.getObjectId().getSuperApp()));
		entity.setType(boundary.getType());
		entity.setAlias(boundary.getAlias());
		entity.setCreationTimeStamp(boundary.getCreationTimesTamp());
		entity.setObjectDetails(boundary.getObjectDetails());
		if (boundary.getActive() != null) {
			entity.setActive(boundary.getActive());
		}else {
			entity.setActive(false);
		}
		
		String createdBy = boundary
				.getCreatedBy()
				.getUserId()
				.getSuperapp()
				+ "#" 
				+ boundary
				.getCreatedBy()
				.getUserId()
				.getEmail();
		if (
				boundary.getLocation() != null) {
			entity.setLocation(
					boundary.getLocation().getLat()
					+ "#" 
					+ boundary.getLocation().getLng());
		} else {
			entity.setLocation("0#0");
		}
		entity.setActive(boundary.getActive());
		entity.setCreatedBy(createdBy);
		
		return entity;

	}
	
}