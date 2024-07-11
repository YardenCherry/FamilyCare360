package demo.app.converters;

import org.springframework.stereotype.Component;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.entities.MiniAppCommandEntity;
import demo.app.objects.CommandId;
import demo.app.objects.CreatedBy;
import demo.app.objects.ObjectId;
import demo.app.objects.TargetObject;
import demo.app.objects.UserId;

@Component
public class CommandConverter {

	public MiniAppCommandBoundary toBoundary(MiniAppCommandEntity entity) {
		MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();

		// Split commandId into its parts
		String[] commandIdParts = entity.getCommandId().split("_");
		if (commandIdParts.length != 3) {
			throw new IllegalArgumentException("Invalid command ID format");
		}

		CommandId commandId = new CommandId();
		commandId.setId(commandIdParts[2]);
		commandId.setMiniapp(commandIdParts[1]);
		commandId.setSuperapp(commandIdParts[0]);
		boundary.setCommandId(commandId);

		boundary.setCommand(entity.getCommand());
		boundary.setInvocationTimeStamp(entity.getInvocationTimeStamp());

		// Handle TargetObject
		String[] targetObjectParts = entity.getTargetObject().split("_");
		if (targetObjectParts.length == 2) {
			ObjectId objectId = new ObjectId();
			objectId.setId(targetObjectParts[1]);
			objectId.setSuperapp(targetObjectParts[0]);
			TargetObject targetObject = new TargetObject();
			targetObject.setObjectId(objectId);

			boundary.setTargetObject(targetObject);
		}

		// Handle InvokedBy
		String[] invokedByParts = entity.getInvokedBy().split("_");
		if (invokedByParts.length == 2) {
			UserId userId = new UserId();
			userId.setSuperapp(invokedByParts[0]);
			userId.setEmail(invokedByParts[1]);
			boundary.setInvokedBy(new CreatedBy(userId));
		}

		boundary.setCommandAttributes(entity.getCommandAttributes());

		return boundary;
	}

	public MiniAppCommandEntity toEntity(MiniAppCommandBoundary boundary) {
		MiniAppCommandEntity entity = new MiniAppCommandEntity();

		entity.setCommandId(String.join("_", boundary.getCommandId().getSuperapp(),
				boundary.getCommandId().getMiniapp(), boundary.getCommandId().getId()));
		entity.setMiniAppName(boundary.getCommandId().getMiniapp());

		entity.setCommand(boundary.getCommand());
		entity.setInvocationTimeStamp(boundary.getInvocationTimeStamp());

		TargetObject targetObject = boundary.getTargetObject();
		entity.setTargetObject(
				String.join("_", targetObject.getObjectId().getSuperapp(), targetObject.getObjectId().getId()));

		UserId userId = boundary.getInvokedBy().getUserId();
		entity.setInvokedBy(String.join("_", userId.getSuperapp(), userId.getEmail()));

		entity.setCommandAttributes(boundary.getCommandAttributes());

		return entity;
	}
}
