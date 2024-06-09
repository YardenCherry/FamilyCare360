package demo.app.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.logics.AdminLogic;

@RestController
@RequestMapping(path = { "/superapp/admin" })
public class AdminController {

	private AdminLogic adminLogic;

	public AdminController(AdminLogic adminLogic) {
		this.adminLogic = adminLogic;
	}

	@DeleteMapping(path = { "/users" })
	public void deleteAllUsers(@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail) {
		this.adminLogic.deleteAllUsers(userSuperapp, userEmail);
	}

	@DeleteMapping(path = { "/objects" })
	public void deleteAllObjects(@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail) {
		this.adminLogic.deleteAllObjects(userSuperapp, userEmail);

	}

	@DeleteMapping(path = { "/miniapp" })
	public void deleteAllCommandsHistory(@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail) {
		this.adminLogic.deleteAllCommandsHistory(userSuperapp, userEmail);
	}

	@GetMapping(path = { "/users" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getAllUsers(@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) {

		return adminLogic.getAllUsers(size, page, userSuperapp, userEmail).toArray(new UserBoundary[0]);
	}

	@GetMapping(path = { "/miniapp" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public MiniAppCommandBoundary[] getAllCommands(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) {
		return adminLogic.getAllCommands(size, page, userSuperapp, userEmail).toArray(new MiniAppCommandBoundary[0]);
	}

	@GetMapping(path = { "/miniapp/{miniAppName}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public MiniAppCommandBoundary[] getCommandsOfSpecificMiniApp(@PathVariable("miniAppName") String miniAppName,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) {
		return adminLogic.getAllCommandsByMiniAppName(miniAppName, size, page, userSuperapp, userEmail)
				.toArray(new MiniAppCommandBoundary[0]);
	}

}
