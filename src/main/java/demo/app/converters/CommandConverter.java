package demo.app.converters;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.entities.MiniAppCommandEntity;
import demo.app.objects.CommandId;

public class CommandConverter {

    public MiniAppCommandBoundary toBoundary(MiniAppCommandEntity entity) {
        MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();
        CommandId commandId=new CommandId();
        commandId.setId(entity.getCommandId().split("_")[0]);
        commandId.setMiniApp(entity.getCommandId().split("_")[1]);
        commandId.setSuperApp(entity.getCommandId().split("_")[2]);
        boundary.setCommandId(commandId);
        boundary.setCommand(entity.getCommand());
        boundary.setTargetObject(entity.getTargetObject());
        boundary.setInvocationTimeStamp(entity.getInvocationTimeStamp());
        boundary.setInvokedBy(entity.getInvokedBy());
        boundary.setCommandAttributes(entity.getCommandAttributes());
        return boundary;
    }

    public MiniAppCommandEntity toEntity(MiniAppCommandBoundary boundary) {
        MiniAppCommandEntity entity = new MiniAppCommandEntity();
        entity.setCommandId(String.join("_", boundary.getCommandId().getSuperApp(), boundary.getCommandId().getMiniApp(), boundary.getCommandId().getId()));
        entity.setMiniAppName(boundary.getCommandId().getMiniApp());
        entity.setCommand(boundary.getCommand());
        entity.setTargetObject(boundary.getTargetObject());
        entity.setInvocationTimeStamp(boundary.getInvocationTimeStamp());
        entity.setInvokedBy(boundary.getInvokedBy());
        entity.setCommandAttributes(boundary.getCommandAttributes());
        return entity;
    }
}
