package demo.app.converters;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.entities.MiniAppCommandEntity;
import demo.app.entities.UserEntity;
import demo.app.objects.UserId;

public class UserConverter {
	public UserBoundary toBounday (UserEntity entity) {
		UserBoundary rv = new UserBoundary();
		UserId userId=new UserId();
		userId.setEmail(entity.getId().split("_")[0]);
		userId.setSuperapp(entity.getId().split("_")[0]);
		rv.setUserId(userId);
		rv.setUserName(entity.getUserName());
		rv.setRole(entity.getRole());
		rv.setAvatar(entity.getAvatar());
		return rv;
	}
	

	public UserEntity toEntity (UserBoundary boundary) {
		
		
		UserEntity rv = new UserEntity();
		rv.setId(boundary.getUserId().getEmail()+"_"+ boundary.getUserId().getSuperapp());
		rv.setUserName(boundary.getUserName());
		rv.setRole(boundary.getRole());
		if(boundary.getAvatar()!=null)
			rv.setAvatar(boundary.getAvatar());
		else 
			rv.setAvatar("");
		
		return rv;

	}
	
	

}