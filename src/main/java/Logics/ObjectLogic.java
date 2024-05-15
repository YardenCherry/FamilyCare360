package Logics;

import java.util.List;
import java.util.Optional;

import Boundaries.ObjectBoundary;


public interface ObjectLogic {
	public ObjectBoundary storeInDatabase(ObjectBoundary objectBoundary);
	public Optional<ObjectBoundary> getSpecificDemoFromDatabase(String id);
	public Optional<Void> updateObject(ObjectBoundary objectBoundary, String id);
	public List<ObjectBoundary> getAll();
}
