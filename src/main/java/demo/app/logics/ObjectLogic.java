package demo.app.logics;

import java.util.List;
import java.util.Optional;

import demo.app.boundaries.ObjectBoundary;


public interface ObjectLogic {
	public ObjectBoundary storeInDatabase(ObjectBoundary objectBoundary);
	public Optional<ObjectBoundary> getSpecificDemoFromDatabase(String id);
	public Optional<Void> updateObject(ObjectBoundary objectBoundary, String id);
	public List<ObjectBoundary> getAll();
}
