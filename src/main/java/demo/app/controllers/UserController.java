package demo.app.controllers;



import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import demo.app.boundaries.*;
import demo.app.logics.UserLogic;
import demo.app.objects.UserId;


@RestController
@RequestMapping(path ={"/users"})
public class UserController {

    private  UserLogic userLogic;


    public UserController(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createUser(@RequestBody NewUserBoundary userBoundary) {
		return this.userLogic.createNewUser(userBoundary);
	}
    
    @GetMapping(
			path = { "/{superapp}/{email}/" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getSpecificUser(@PathVariable("superapp") String superapp , @PathVariable("email") String email) {
		return this.userLogic
			.getSpecificUser(superapp,email)
			.orElseThrow(()->new MyNotFoundException("coulde not find user in database"));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getManyMessages() {
		return this.userLogic
			.getAll()
			.toArray(new UserBoundary[0]);
	}
	
	@PutMapping(
		path = {"/{superapp}/{email}"},
		consumes = {MediaType.APPLICATION_JSON_VALUE})
	public void update (
			@PathVariable("superapp") String superapp , @PathVariable("email") String email,
			@RequestBody UserBoundary update) {
		this.userLogic
			.updateById(superapp,email, update);
	}
	
}
