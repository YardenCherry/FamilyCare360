package demo.app.crud;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.entities.MiniAppCommandEntity;
import demo.app.logics.CommandLogic;
import demo.app.converters.CommandConverter;

@Service
public class CommandCrudImplementation implements CommandLogic {
    private CommandCrud commandCrud;
    private CommandConverter commandConverter;

    public CommandCrudImplementation(CommandCrud commandCrud, CommandConverter commandConverter) {
        this.commandCrud = commandCrud;
        this.commandConverter = commandConverter;
    }

    @Value("${spring.application.name:defaultName}")
    public void setup(String name) {
        System.err.println("*** " + name);
    }

    @PostConstruct
    public void setupIsDone() {
        System.err.println("Command logic implementation is ready");
    }

    @Override
    @Transactional(readOnly = false)
    public MiniAppCommandBoundary storeInDatabase(MiniAppCommandBoundary commandBoundary) {
        commandBoundary.getCommandId().setId(UUID.randomUUID().toString());
        commandBoundary.setInvocationTimeStamp(new Date());

        MiniAppCommandEntity entity = this.commandConverter.toEntity(commandBoundary);
        entity = this.commandCrud.save(entity);
        return this.commandConverter.toBoundary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MiniAppCommandBoundary> getSpecificDemoFromDatabase(String id) {
        return this.commandCrud.findById(id).map(this.commandConverter::toBoundary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MiniAppCommandBoundary> getAll() {
        List<MiniAppCommandEntity> entities = this.commandCrud.findAll();
        List<MiniAppCommandBoundary> boundaries = new ArrayList<>();
        for (MiniAppCommandEntity entity : entities) {
            boundaries.add(this.commandConverter.toBoundary(entity));
        }
        return boundaries;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteAll() {
        this.commandCrud.deleteAll();
    }

    @Override
    @Transactional(readOnly = false)
    public void updateById(String id, MiniAppCommandBoundary update) {
        MiniAppCommandEntity existing = this.commandCrud.findById(id).orElseThrow(() -> new RuntimeException("could not find command with id: " + id));

        // ignore input id and timestamp
        existing.setCommand(update.getCommand());
        existing.setTargetObject(update.getTargetObject());
        existing.setCommandAttributes(update.getCommandAttributes());
        existing.setInvokedBy(update.getInvokedBy());
        
        this.commandCrud.save(existing);
    }
}
