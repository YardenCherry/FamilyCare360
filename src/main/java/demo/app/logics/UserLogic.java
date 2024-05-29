package demo.app.logics;

import java.util.List;
import java.util.Optional;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.UserBoundary;

public interface UserLogic {
	public UserBoundary createNewUser(NewUserBoundary userBoundary);
	public Optional<UserBoundary> getSpecificUser(String superapp,String email);
	public List<UserBoundary> getAll();
	public void updateById(String superapp,String email, UserBoundary update);
}
