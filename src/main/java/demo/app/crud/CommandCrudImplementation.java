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
import demo.app.objects.InputValidation;
import jakarta.annotation.PostConstruct;

@Service
public class CommandCrudImplementation implements CommandLogic {
	private CommandCrud commandCrud;
	private CommandConverter commandConverter;
	private String springApplicationName;

	public CommandCrudImplementation(CommandCrud commandCrud, CommandConverter commandConverter) {
		this.commandCrud = commandCrud;
		this.commandConverter = commandConverter;
	}

	@Value("${spring.application.name:supperapp}")
	public void setup(String name) {
		this.springApplicationName = name;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}

	@PostConstruct
	public void setupIsDone() {
		System.err.println("Command logic implementation is ready");
	}

	@Override
	@Transactional(readOnly = false)
	public MiniAppCommandBoundary storeInDatabase(String miniAppName, MiniAppCommandBoundary commandBoundary) {
		validateCommandBoundary(commandBoundary);

		commandBoundary.getCommandId().setId(UUID.randomUUID().toString());
		commandBoundary.getCommandId().setSuperApp(springApplicationName);
		commandBoundary.getCommandId().setMiniApp(miniAppName);
		commandBoundary.setInvocationTimeStamp(new Date());
		commandBoundary.getTargetObject().getObjectId().setSuperApp(springApplicationName);
		commandBoundary.getInvokedBy().getUserId().setSuperapp(springApplicationName);
		MiniAppCommandEntity entity = this.commandConverter.toEntity(commandBoundary);
		entity = this.commandCrud.save(entity);

		return this.commandConverter.toBoundary(entity);
	}

	private void validateCommandBoundary(MiniAppCommandBoundary commandBoundary) {
		if (commandBoundary == null) {
			throw new MyBadRequestException("CommandBoundary cannot be null.");
		}
		if (commandBoundary.getCommandId() == null || commandBoundary.getCommandId().getMiniApp() == null) {
			throw new MyBadRequestException("Command ID and miniApp cannot be null.");
		}
		if (commandBoundary.getCommand() == null || commandBoundary.getCommand().trim().isEmpty()) {
			throw new MyBadRequestException("Command cannot be null or empty.");
		}
		if (commandBoundary.getTargetObject() == null || commandBoundary.getTargetObject().getObjectId() == null
				|| commandBoundary.getTargetObject().getObjectId().getId() == null) {
			throw new MyBadRequestException("Target object cannot be null or empty.");
		}
		if (commandBoundary.getInvokedBy() == null) {
			throw new MyBadRequestException("Invoke by cannot be null.");
		}
		if (commandBoundary.getInvokedBy() == null
				|| !InputValidation.isValidEmail(commandBoundary.getInvokedBy().getUserId().getEmail())) {
			throw new MyBadRequestException("You must enter valid email.");
		}
	}
}
