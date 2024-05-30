package demo.app.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import demo.app.boundaries.ObjectBoundary;
import demo.app.entities.ObjectEntity;
import demo.app.objects.CreatedBy;
import demo.app.objects.Location;
import demo.app.objects.ObjectId;
import demo.app.objects.UserId;

@Component
public class ObjectConverter {

	private static final Logger logger = LoggerFactory.getLogger(ObjectConverter.class);

	public ObjectBoundary toBoundary(ObjectEntity entity) {
		ObjectBoundary boundary = new ObjectBoundary();
		String[] objectIdParts = entity.getObjectID().split("_");
		if (objectIdParts.length != 2) {
			throw new IllegalArgumentException("Invalid object ID format");
		}

		ObjectId ob = new ObjectId();
		ob.setId(objectIdParts[0]);
		ob.setSuperApp(objectIdParts[1]);
		boundary.setObjectId(ob);

		boundary.setType(entity.getType());
		boundary.setAlias(entity.getAlias());
		boundary.setActive(entity.getActive());
		boundary.setCreationTimestamp(entity.getCreationTimestamp());
		boundary.setObjectDetails(entity.getObjectDetails());

		String[] locationParts = entity.getLocation().split("_");
		if (locationParts.length == 2) {
			try {
				double lat = Double.parseDouble(locationParts[0]);
				double lng = Double.parseDouble(locationParts[1]);
				boundary.setLocation(new Location(lat, lng));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid location format", e);
			}
		}

		String[] createdByParts = entity.getCreatedBy().split("_");
		if (createdByParts.length == 2) {
			UserId userId = new UserId();
			userId.setSuperapp(createdByParts[0]);
			userId.setEmail(createdByParts[1]);
			boundary.setCreatedBy(new CreatedBy(userId));
		}

		logger.debug("Converted to boundary: {}", boundary);
		return boundary;
	}

	public ObjectEntity toEntity(ObjectBoundary boundary) {
		ObjectEntity entity = new ObjectEntity();

		if (boundary.getObjectId() != null || boundary.getObjectId().getId() != null
				|| boundary.getObjectId().getSuperApp() != null) {
			entity.setObjectID(boundary.getObjectId().getId() + "_" + boundary.getObjectId().getSuperApp());
		} else {
			throw new IllegalArgumentException("Object ID and SuperApp cannot be null");
		}

		entity.setType(boundary.getType());
		entity.setAlias(boundary.getAlias());
		entity.setCreationTimestamp(boundary.getCreationTimestamp());
		entity.setObjectDetails(boundary.getObjectDetails());
		entity.setActive(boundary.getActive() != null ? boundary.getActive() : false);

		CreatedBy createdBy = boundary.getCreatedBy();
		if (createdBy != null && createdBy.getUserId() != null) {
			entity.setCreatedBy(createdBy.getUserId().getSuperapp() + "_" + createdBy.getUserId().getEmail());
		}

		Location location = boundary.getLocation();
		if (location != null) {
			entity.setLocation(location.getLat() + "_" + location.getLng());
		} else {
			entity.setLocation("0_0");
		}

		logger.debug("Converted to entity: {}", entity);
		return entity;
	}
}
