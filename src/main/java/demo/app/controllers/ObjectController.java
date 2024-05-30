package demo.app.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.app.boundaries.ObjectBoundary;
import demo.app.logics.ObjectLogic;

@RestController
@RequestMapping(path = { "/objects" })
public class ObjectController {

	private ObjectLogic objectLogic;

	public ObjectController(ObjectLogic objectLogic) {
		this.objectLogic = objectLogic;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary storeInDatabase(@RequestBody ObjectBoundary objectBoundary) {
		System.err.println(objectBoundary);
		return this.objectLogic.storeInDatabase(objectBoundary);
	}

	@GetMapping(path = { "/{superapp}/{id}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary getSpecificObject(@PathVariable("id") String id, @PathVariable("superapp") String superapp) {
		return this.objectLogic.getSpecificObject(id, superapp)
				.orElseThrow(() -> new MyNotFoundException("could not find object in database"));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjects() {
		return this.objectLogic.getAll().toArray(new ObjectBoundary[0]);
	}

	@PutMapping(path = { "/{superapp}/{id}" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void update(@PathVariable("id") String id, @PathVariable("superapp") String superapp,
			@RequestBody ObjectBoundary update) {
		this.objectLogic.updateById(id, superapp, update);
	}
}
