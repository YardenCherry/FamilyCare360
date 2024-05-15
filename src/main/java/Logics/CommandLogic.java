package Logics;

import java.util.List;
import java.util.Optional;

import Boundaries.MiniAppCommandBoundary;


public interface CommandLogic {
	public Optional<MiniAppCommandBoundary> getSpecificDemoFromDatabase(String id);
	public Optional<Void> updateObject(MiniAppCommandBoundary miniAppCommandBoundary, String id);
	public List<MiniAppCommandBoundary> getAll();
	public MiniAppCommandBoundary storeInDatabase(MiniAppCommandBoundary miniAppCommandBoundary);
}
