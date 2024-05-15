package Controllers;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import Boundaries.ObjectBoundary;
import Logics.ObjectLogic;


@RestController
@RequestMapping(path = {"/objects"})
public class ObjectController {

    private  ObjectLogic objectLogic;

    public ObjectController(ObjectLogic objectLogic) {
        this.objectLogic = objectLogic;
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary storeInDatabase(@RequestBody ObjectBoundary objectBoundary) {
		System.err.println(objectBoundary);
		return this.objectLogic.storeInDatabase(objectBoundary);
	}
    
    @GetMapping(
			path = { "/{id}" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary getSpecificMessage(@PathVariable("id") String id) {
		return this.objectLogic
			.getSpecificDemoFromDatabase(id)
			.orElseThrow(()->new RuntimeException("coulde not find demo in database"));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getManyMessages() {
		return this.objectLogic
			.getAll()
			.toArray(new ObjectBoundary[0]);
	}


}
