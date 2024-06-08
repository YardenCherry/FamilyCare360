package demo.app.logics;

import java.util.List;
import java.util.Optional;

import demo.app.boundaries.ObjectBoundary;

public interface ObjectLogic {
	public ObjectBoundary storeInDatabase(ObjectBoundary objectBoundary);

	public Optional<ObjectBoundary> getSpecificObject(String id, String superapp, String userSuperapp,
			String userEmail);

	public List<ObjectBoundary> getAll();

	public Optional<ObjectBoundary> updateById(String id, String superapp, String userSuperapp, String userEmail,
			ObjectBoundary update);
}
