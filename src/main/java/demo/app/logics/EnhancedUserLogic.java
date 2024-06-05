package demo.app.logics;

import demo.app.boundaries.UserBoundary;

import java.util.List;


public interface EnhancedUserLogic extends UserLogic {
	public List<UserBoundary> getAll(
			int size, int page);

}
