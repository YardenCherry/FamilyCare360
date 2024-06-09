package demo.app.crud;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.converters.CommandConverter;
import demo.app.entities.MiniAppCommandEntity;
import demo.app.entities.ObjectEntity;
import demo.app.entities.UserEntity;
import demo.app.enums.Role;
import demo.app.logics.CommandLogic;
import demo.app.objects.InputValidation;
import jakarta.annotation.PostConstruct;

@Service
public class CommandCrudImplementation implements CommandLogic {
	private CommandCrud commandCrud;
	private CommandConverter commandConverter;
	private String springApplicationName;
	private UserCrud userCrud;
	private ObjectCrud objectCrud;

	public CommandCrudImplementation(CommandCrud commandCrud, CommandConverter commandConverter, UserCrud userCrud,
			ObjectCrud objectCrud) {
		this.commandCrud = commandCrud;
		this.commandConverter = commandConverter;
		this.userCrud = userCrud;
		this.objectCrud = objectCrud;
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

		// Verify user permissions
		UserEntity user = userCrud
				.findById(commandBoundary.getInvokedBy().getUserId().getSuperapp() + "_"
						+ commandBoundary.getInvokedBy().getUserId().getEmail())
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));
		if (!user.getRole().equals(Role.MINIAPP_USER)) {
			throw new MyForbiddenException("User not authorized");
		}

		// Verify target object existence and active status
		ObjectEntity targetObject = objectCrud
				.findById(commandBoundary.getTargetObject().getObjectId().getId() + "_"
						+ commandBoundary.getTargetObject().getObjectId().getSuperApp())
				.orElseThrow(() -> new MyBadRequestException("Target object not found in the database."));
		if (!targetObject.getActive()) {
			throw new MyBadRequestException("Target object is not active.");
		}
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
