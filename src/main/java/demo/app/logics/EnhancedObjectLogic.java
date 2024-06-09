package demo.app.logics;

import java.util.List;

import demo.app.boundaries.ObjectBoundary;

public interface EnhancedObjectLogic extends ObjectLogic {
	public List<ObjectBoundary> getAll(int size, int page, String userSuperapp, String userEmail);

	public List<ObjectBoundary> getAllByType(String type, int size, int page, String userSuperapp, String userEmail);

	public List<ObjectBoundary> getAllByAlias(String alias, int size, int page, String userSuperapp, String userEmail);

	public List<ObjectBoundary> getAllByAliasPattern(String pattern, int size, int page, String userSuperapp,
			String userEmail);

	public List<ObjectBoundary> getAllByLocation(double lat, double lng, double distance, String distanceUnits,
			int size, int page, String userSuperapp, String userEmail);
}
