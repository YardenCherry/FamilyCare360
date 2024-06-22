package demo.app.logics;

import java.util.List;

import demo.app.boundaries.MiniAppCommandBoundary;

public interface CommandLogic {
	public List<Object> storeInDatabase(String miniAppName, MiniAppCommandBoundary commandBoundary);
}
