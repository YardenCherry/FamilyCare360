package Controllers;



import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import Boundaries.ObjectBoundary;
import Boundaries.UserBoundary;
import Logics.UserLogic;


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
	public UserBoundary getSpecificMessage(@PathVariable("id") String id) {
		return this.userLogic
			.getSpecificDemoFromDatabase(id)
			.orElseThrow(()->new RuntimeException("coulde not find demo in database"));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getManyMessages() {
		return this.userLogic
			.getAll()
			.toArray(new ObjectBoundary[0]);
	}
}
