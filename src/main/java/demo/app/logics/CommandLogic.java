package demo.app.logics;

import java.util.List;
import java.util.Optional;

import demo.app.boundaries.*;


public interface CommandLogic {
	public Optional<MiniAppCommandBoundary> getSpecificDemoFromDatabase(String id);
	public Optional<Void> updateObject(MiniAppCommandBoundary miniAppCommandBoundary, String id);
	public List<MiniAppCommandBoundary> getAll();
	public MiniAppCommandBoundary storeInDatabase(MiniAppCommandBoundary miniAppCommandBoundary);
}
