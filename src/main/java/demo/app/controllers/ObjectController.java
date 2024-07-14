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

import demo.app.boundaries.ObjectBoundary;
import demo.app.logics.EnhancedObjectLogic;

@RestController
@RequestMapping(path = { "/superapp/objects" })
public class ObjectController {

	private EnhancedObjectLogic objectLogic;

	public ObjectController(EnhancedObjectLogic objectLogic) {
		this.objectLogic = objectLogic;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary storeInDatabase(@RequestBody ObjectBoundary objectBoundary) {
		System.err.println(objectBoundary);
		return this.objectLogic.storeInDatabase(objectBoundary);
	}

	@GetMapping(path = { "/{superapp}/{id}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary getSpecificObject(@PathVariable("id") String id, @PathVariable("superapp") String superapp,
			@RequestParam("userSuperapp") String userSuperapp, @RequestParam("userEmail") String userEmail) {
		return this.objectLogic.getSpecificObject(id, superapp, userSuperapp, userEmail)
				.orElseThrow(() -> new MyNotFoundException("could not find object in database"));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjects(@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", defaultValue = "10", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) {
		return this.objectLogic.getAll(size, page, userSuperapp, userEmail).toArray(new ObjectBoundary[0]);
	}

	@PutMapping(path = { "/{superapp}/{id}" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void update(@PathVariable("id") String id, @PathVariable("superapp") String superapp,
			@RequestParam("userSuperapp") String userSuperapp, @RequestParam("userEmail") String userEmail,
			@RequestBody ObjectBoundary update) {
		this.objectLogic.updateById(id, superapp, userSuperapp, userEmail, update)
				.orElseThrow(() -> new MyNotFoundException(
						"ObjectEntity with id: " + id + " and superapp " + superapp + " does not exist in database"));
	}

	@GetMapping(path = { "/search/byType/{type}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectsByType(@PathVariable("type") String type,
			@RequestParam(name = "size", defaultValue = "10", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail) {
		return this.objectLogic.getAllByType(type, size, page, userSuperapp, userEmail).toArray(new ObjectBoundary[0]);
	}

	@GetMapping(path = { "/search/byAlias/{alias}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectsByAlias(@PathVariable("alias") String alias,
			@RequestParam(name = "size", defaultValue = "10", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail) {
		return this.objectLogic.getAllByAlias(alias, size, page, userSuperapp, userEmail).toArray(new ObjectBoundary[0]);
	}

	@GetMapping(path = { "/search/byAliasPattern/{pattern}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectsByAliasPattern(@PathVariable("pattern") String pattern,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) {
		return this.objectLogic.getAllByAliasPattern(pattern, size, page, userSuperapp, userEmail)
				.toArray(new ObjectBoundary[0]);
	}

	@GetMapping(path = { "/search/byLocation/{lat}/{lng}/{distance}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectsByLocation(@PathVariable("lat") double lat, @PathVariable("lng") double lng,
			@PathVariable("distance") double distance,
			@RequestParam(name = "units", defaultValue = "NEUTRAL", required = false) String distanceUnits,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", defaultValue = "10", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) {
		return this.objectLogic.getAllByLocation(lat, lng, distance, distanceUnits, size, page, userSuperapp, userEmail)
				.toArray(new ObjectBoundary[0]);
	}

}
