package demo.app.logics;

import java.util.List;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.UserBoundary;

public interface AdminLogic {

	public void deleteAllUsers(String userSuperapp, String userEmail);

	public void deleteAllObjects(String userSuperapp, String userEmail);

	public void deleteAllCommandsHistory(String userSuperapp, String userEmail);

	@Deprecated
	public List<UserBoundary> getAllUsers();

	@Deprecated
	public List<MiniAppCommandBoundary> getAllCommands();

	@Deprecated
	public List<MiniAppCommandBoundary> getAllCommandsByMiniAppName(String miniAppName);

	public List<UserBoundary> getAllUsers(int size, int page, String userSuperapp, String userEmail);

	public List<MiniAppCommandBoundary> getAllCommands(int size, int page, String userSuperapp, String userEmail);

	public List<MiniAppCommandBoundary> getAllCommandsByMiniAppName(String miniAppName, int size, int page,
			String userSuperapp, String userEmail);

}
