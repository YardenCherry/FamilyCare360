package demo.app.logics;

import java.util.List;
import java.util.Optional;

import demo.app.boundaries.ObjectBoundary;


public interface ObjectLogic {
	public ObjectBoundary storeInDatabase(ObjectBoundary objectBoundary);
	public Optional<ObjectBoundary> getSpecificObject(String id, String superapp);
	public List<ObjectBoundary> getAll();
	public void updateById(String id, String superapp, ObjectBoundary update);
}
