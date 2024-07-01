package demo.app.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.logics.CommandLogic;

@RestController
@RequestMapping(path = { "/superapp/miniapp" })
public class CommandController {

	private CommandLogic commandLogic;

	public CommandController(CommandLogic commandLogic) {
		this.commandLogic = commandLogic;
	}

	@PostMapping(path = "/{miniAppName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object[] storeInDatabase(@PathVariable("miniAppName") String miniAppName,
			@RequestBody MiniAppCommandBoundary commandBoundary) {
		System.err.println(miniAppName);
		return this.commandLogic.storeInDatabase(miniAppName, commandBoundary).toArray(new Object[0]);
	}
}
