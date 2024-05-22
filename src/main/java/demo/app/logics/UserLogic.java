package demo.app.logics;

import java.util.List;
import java.util.Optional;

import demo.app.boundaries.UserBoundary;

public interface UserLogic {
	public UserBoundary storeInDatabase(UserBoundary userBoundary);
	public Optional<UserBoundary> getSpecificDemoFromDatabase(String id);
	public List<UserBoundary> getAll();
	public void deleteAll();
	public void updateById(String id, UserBoundary update);
}
