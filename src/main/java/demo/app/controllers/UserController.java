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
	public UserBoundary storeInDatabase(@RequestBody UserBoundary userBoundary) {
		System.err.println(userBoundary);
		return this.userLogic.storeInDatabase(userBoundary);
	}
    
    @GetMapping(
			path = { "/{id}" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getSpecificMessage(@PathVariable("id") UserId id) {
		return this.userLogic
			.getSpecificDemoFromDatabase(id.getEmail()+"_"+id.getSuperapp())
			.orElseThrow(()->new RuntimeException("coulde not find demo in database"));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getManyMessages() {
		return this.userLogic
			.getAll()
			.toArray(new UserBoundary[0]);
	}
	@DeleteMapping
	public void deleteAll() {
		this.userLogic
			.deleteAll();
	}

	@PutMapping(
		path = {"/{id}"},
		consumes = {MediaType.APPLICATION_JSON_VALUE})
	public void update (
			@PathVariable("id") String id,
			@RequestBody UserBoundary update) {
		this.userLogic
			.updateById(id, update);
	}
	
}
