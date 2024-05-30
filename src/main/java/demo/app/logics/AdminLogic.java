package demo.app.logics;

import java.util.List;
import java.util.Optional;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.ObjectBoundary;
import demo.app.boundaries.UserBoundary;

public interface AdminLogic {

	public void deleteAllUsers();
	
	public void deleteAllObjects();
	
	public void deleteAllCommandsHistory();
	
	public List<UserBoundary> getAllUsers();
	
	public List<MiniAppCommandBoundary> getAllCommands();
		
	public List<MiniAppCommandBoundary> getAllCommandsByMiniAppName(String miniAppName);

}
