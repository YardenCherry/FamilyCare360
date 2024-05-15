package Crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Boundaries.MiniAppCommandBoundary;
import Entities.MiniAppCommandEntity;
import Logics.CommandLogic;
import Objects.CommandId;

@Service
public class CommandCrudImplementation implements CommandLogic {
	private CommandCrud commandCrud ;

	public CommandCrudImplementation(CommandCrud commandCrud) {
		this.commandCrud = commandCrud;
	}
	
	@Override
	@Transactional(readOnly = false)
	public MiniAppCommandBoundary storeInDatabase(MiniAppCommandBoundary miniAppCommandBoundary)
	{
		CommandId commandId=new CommandId();
		commandId.setId(UUID.randomUUID().toString());
		miniAppCommandBoundary.setCommandId(commandId);
		MiniAppCommandEntity entity = miniAppCommandBoundary.toEntity();
		
		entity = this.commandCrud.save(entity);
		return new MiniAppCommandBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<MiniAppCommandBoundary> getSpecificDemoFromDatabase(String id) {
		return this.commandCrud
			.findById(id)
			.map(entity->new MiniAppCommandBoundary(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAll() {
		List<MiniAppCommandEntity> entities = 
		  this.commandCrud
			.findAll();
		
		List<MiniAppCommandBoundary> rv = new ArrayList<>();
		
		for (MiniAppCommandEntity entity : entities) {
			rv.add(new MiniAppCommandBoundary(entity));
		}
		
		return rv;
	}

	@Override
	public Optional<Void> updateObject(MiniAppCommandBoundary miniAppCommandBoundary, String id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	
	
}
