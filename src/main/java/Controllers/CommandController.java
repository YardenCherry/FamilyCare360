package Controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Boundaries.MiniAppCommandBoundary;
import Logics.CommandLogic;


@RestController
@RequestMapping(path = {"/miniapp"})
public class CommandController {

    private CommandLogic commandLogic;

    public CommandController(CommandLogic commandLogic) {
        this.commandLogic = commandLogic;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public MiniAppCommandBoundary storeInDatabase(@RequestBody MiniAppCommandBoundary miniAppCommandBoundary) {
		System.err.println(miniAppCommandBoundary);
		return this.commandLogic.storeInDatabase(miniAppCommandBoundary);
	}
    
    @GetMapping(
			path = { "/{id}" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public MiniAppCommandBoundary getSpecificMessage(@PathVariable("id") String id) {
		return this.commandLogic
			.getSpecificDemoFromDatabase(id)
			.orElseThrow(()->new RuntimeException("coulde not find demo in database"));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public MiniAppCommandBoundary[] getManyMessages() {
		return this.commandLogic
			.getAll()
			.toArray(new MiniAppCommandBoundary[0]);
	}

    
}
