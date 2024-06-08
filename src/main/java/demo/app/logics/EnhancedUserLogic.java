package demo.app.logics;

import java.util.List;

import demo.app.boundaries.UserBoundary;

public interface EnhancedUserLogic extends UserLogic {
	public List<UserBoundary> getAll(int size, int page);

}
