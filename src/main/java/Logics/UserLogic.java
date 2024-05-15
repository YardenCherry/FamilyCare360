package Logics;

import java.util.List;
import java.util.Optional;

import Boundaries.UserBoundary;

public interface UserLogic {
	public UserBoundary storeInDatabase(UserBoundary userBoundary);
	public Optional<UserBoundary> getSpecificDemoFromDatabase(String id);
	public Optional<Void> updateObject(UserBoundary userBoundary, String id);
	public List<UserBoundary> getAll();

}
