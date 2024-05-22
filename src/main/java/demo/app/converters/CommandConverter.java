package demo.app.converters;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.entities.MiniAppCommandEntity;

public class CommandConverter {
	public MiniAppCommandBoundary toBounday (MiniAppCommandEntity entity) {
		MiniAppCommandBoundary rv = new MiniAppCommandBoundary();
		
		rv.setId(entity.getId());
		rv.setMessage(entity.getMessage());
		rv.setMessageTimestamp(entity.getMessageTimestamp());
		rv.setVersion(entity.getVersion());
		rv.setStatus(
				this.toBoundary(entity.getStatus()));
		rv.setDemoData(entity.getDemoData());
		
		rv.setName(new NameBoundary(
			entity.getFirstName(),
			entity.getLastName()));
		
		return rv;
	}
	

	public MiniAppCommandEntity toEntity (MiniAppCommandBoundary boundary) {
		MiniAppCommandEntity rv = new MiniAppCommandEntity();
		
		rv.setId(boundary.getId());
		rv.setMessage(boundary.getMessage());
		rv.setMessageTimestamp(boundary.getMessageTimestamp());
		if (boundary.getVersion() != null) {
			rv.setVersion(boundary.getVersion());
		}else {
			rv.setVersion(0);
		}
		if (boundary.getStatus() != null) {
			rv.setStatus(
				this.toEntity(boundary.getStatus()));
		}else {
			rv.setStatus(StatusEnumInDB.not_available);
		}
		rv.setDemoData(boundary.getDemoData());

		// convert name to first and last names 
		if (boundary.getName() != null) {
			rv.setFirstName(boundary.getName().getFirstName());
			rv.setLastName(boundary.getName().getLastName());
		}else {
			rv.setFirstName(null);
			rv.setLastName(null);
		}
		return rv;

	}
	
	public StatusEnum toBoundary(StatusEnumInDB status) {
		return 
			StatusEnum.valueOf(status.name().toUpperCase());
	}

	public StatusEnumInDB toEntity (StatusEnum status) {
		return 
			StatusEnumInDB.valueOf(status.name().toLowerCase());
	}
}
