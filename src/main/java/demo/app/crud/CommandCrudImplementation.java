package demo.app.crud;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.entities.MiniAppCommandEntity;
import demo.app.logics.CommandLogic;
import demo.app.objects.CommandId;

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
		return this.commandCrud.findAll().stream()
                .map(MiniAppCommandBoundary::new)
                .collect(Collectors.toList());
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateById(String id, MiniAppCommandBoundary update) {
		// TODO Auto-generated method stub
		
	}
	
	
}
