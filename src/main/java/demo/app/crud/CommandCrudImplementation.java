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
    private String springApplicationName;

	public CommandCrudImplementation(CommandCrud commandCrud, CommandConverter commandConverter) {
		this.commandCrud = commandCrud;
		this.commandConverter = commandConverter;
	}

	@Value("${spring.application.name:supperapp}")
	public void setup(String name) {
        this.springApplicationName = name;
		System.err.println("*** " + name);
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
	        if (commandBoundary.getCommandId() == null || commandBoundary.getCommandId().getSuperApp() == null) {
	            throw new MyBadRequestException("Command ID and SuperApp cannot be null.");
	        }
	        if (commandBoundary.getCommandId().getMiniApp() == null || commandBoundary.getCommandId().getMiniApp().trim().isEmpty()) {
	            throw new MyBadRequestException("MiniApp cannot be null or empty.");
	        }
	        if (commandBoundary.getInvokedBy() == null || commandBoundary.getInvokedBy().getUserId().getEmail().trim().isEmpty()) {
	            throw new MyBadRequestException("User's email cannot be null or empty.");
	        }
	    }
}
