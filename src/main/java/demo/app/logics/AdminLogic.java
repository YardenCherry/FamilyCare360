package demo.app.logics;

import java.util.List;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.UserBoundary;

public interface AdminLogic {

	public void deleteAllUsers();

	public void deleteAllObjects();

	public void deleteAllCommandsHistory();

	@Deprecated
	public List<UserBoundary> getAllUsers();

	@Deprecated
	public List<MiniAppCommandBoundary> getAllCommands();

	@Deprecated
	public List<MiniAppCommandBoundary> getAllCommandsByMiniAppName(String miniAppName);

	public List<UserBoundary> getAllUsers(int size, int page);

}
