package demo.app.logics;

import demo.app.boundaries.MiniAppCommandBoundary;

public interface CommandLogic {
	public MiniAppCommandBoundary storeInDatabase(String miniAppName, MiniAppCommandBoundary commandBoundary);
}
