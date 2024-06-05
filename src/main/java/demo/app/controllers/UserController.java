package demo.app.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.logics.EnhancedUserLogic;

@RestController
@RequestMapping(path = { "/users" })
public class UserController {

	private EnhancedUserLogic userLogic;

	public UserController(EnhancedUserLogic userLogic) {
		this.userLogic = userLogic;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createUser(@RequestBody NewUserBoundary userBoundary) {
		return this.userLogic.createNewUser(userBoundary);
	}

	@GetMapping(path = { "/{superapp}/{email}/" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getSpecificUser(@PathVariable("superapp") String superapp,
			@PathVariable("email") String email) {
		return this.userLogic.getSpecificUser(superapp, email)
				.orElseThrow(() -> new MyNotFoundException("coulde not find user in database"));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getManyMessages(
		@RequestParam(name = "size", defaultValue = "5", required = false) int size,
		@RequestParam(name = "page", defaultValue = "0", required = false) int page) {
		return this.userLogic.getAll(size, page).toArray(new UserBoundary[0]);
	}

	@PutMapping(path = { "/{superapp}/{email}" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void update(@PathVariable("superapp") String superapp, @PathVariable("email") String email,
			@RequestBody UserBoundary update) {
		this.userLogic.updateById(superapp, email, update).orElseThrow(() -> new MyNotFoundException(
				"UserEntity with email: " + email + " and superapp " + superapp + " does not exist in database"));
	}

}
