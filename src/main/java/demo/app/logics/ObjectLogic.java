package demo.app.logics;

import java.util.List;
import java.util.Optional;

import demo.app.boundaries.ObjectBoundary;


public interface ObjectLogic {
	public ObjectBoundary storeInDatabase(ObjectBoundary objectBoundary);
	public Optional<ObjectBoundary> getSpecificDemoFromDatabase(String id);
	public List<ObjectBoundary> getAll();
	public void deleteAll();
	public void updateById(String id, ObjectBoundary update);
}
