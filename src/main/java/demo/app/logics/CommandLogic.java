package demo.app.logics;

import java.util.List;
import java.util.Optional;

import demo.app.boundaries.*;


public interface CommandLogic {
	public Optional<MiniAppCommandBoundary> getSpecificDemoFromDatabase(String id);
	public List<MiniAppCommandBoundary> getAll();
	public MiniAppCommandBoundary storeInDatabase(MiniAppCommandBoundary miniAppCommandBoundary);
	public void deleteAll();
	public void updateById(String id, MiniAppCommandBoundary update);
}

