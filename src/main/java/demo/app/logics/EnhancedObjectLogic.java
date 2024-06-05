package demo.app.logics;

import demo.app.boundaries.ObjectBoundary;

import java.util.List;

public interface EnhancedObjectLogic extends ObjectLogic {
	public List<ObjectBoundary> getAll(
			int size, int page);
	public List<ObjectBoundary> getObjectByType (
			String type, 
			int size, int page);
	public List<ObjectBoundary> getObjectByAlias (
			String alias, 
			int size, int page);
	public List<ObjectBoundary> getObjectByAliasPattern (
			String aliasPattern, 
			int size, int page);
	public List<ObjectBoundary> getObjectByLocation (
			String location, 
			int size, int page);
}
