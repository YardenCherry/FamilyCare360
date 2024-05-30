package demo.app.crud;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.converters.CommandConverter;
import demo.app.entities.MiniAppCommandEntity;
import demo.app.logics.CommandLogic;
import jakarta.annotation.PostConstruct;

@Service
public class CommandCrudImplementation implements CommandLogic {
	private CommandCrud commandCrud;
	private CommandConverter commandConverter;

	public CommandCrudImplementation(CommandCrud commandCrud, CommandConverter commandConverter) {
		this.commandCrud = commandCrud;
		this.commandConverter = commandConverter;
	}

	@Value("${spring.application.name:supperapp}")
	public void setup(String name) {
		System.err.println("*** " + name);
	}

	@PostConstruct
	public void setupIsDone() {
		System.err.println("Command logic implementation is ready");
	}

	@Override
	@Transactional(readOnly = false)
	public MiniAppCommandBoundary storeInDatabase(String miniAppName, MiniAppCommandBoundary commandBoundary) {
		commandBoundary.getCommandId().setId(UUID.randomUUID().toString());
		commandBoundary.getCommandId().setSuperApp(commandBoundary.getCommandId().getSuperApp());
		commandBoundary.getCommandId().setMiniApp(miniAppName);
		commandBoundary.setInvocationTimeStamp(new Date());

		MiniAppCommandEntity entity = this.commandConverter.toEntity(commandBoundary);
		entity = this.commandCrud.save(entity);

		return this.commandConverter.toBoundary(entity);
	}

}
