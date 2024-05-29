package demo.app.logics;

import java.util.List;
import java.util.Optional;

import demo.app.boundaries.*;


public interface CommandLogic {
	public MiniAppCommandBoundary storeInDatabase(String miniAppName,MiniAppCommandBoundary commandBoundary);
}

