package demo.app.converters;

import org.springframework.stereotype.Component;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.entities.UserEntity;
import demo.app.objects.UserId;
@Component
public class UserConverter {

    public UserBoundary toBoundary(UserEntity entity) {
        UserBoundary boundary = new UserBoundary();
        UserId userId = new UserId();
        
        String[] idParts = entity.getId().split("_");
        userId.setEmail(idParts[0]);
        userId.setSuperapp(idParts[1]);
        
        boundary.setUserId(userId);
        boundary.setUserName(entity.getUserName());
        boundary.setRole(entity.getRole());
        boundary.setAvatar(entity.getAvatar());
        
        return boundary;
    }

    public UserEntity toEntity(UserBoundary boundary) {
        UserEntity entity = new UserEntity();
        
        entity.setId(boundary.getUserId().getEmail() + "_" + boundary.getUserId().getSuperapp());
        entity.setUserName(boundary.getUserName());
        entity.setRole(boundary.getRole());
        
        if (boundary.getAvatar() != null) {
        	entity.setAvatar(boundary.getAvatar());
        } else {
        	entity.setAvatar("");
        }
        
        return entity;
    }
    
public UserEntity toEntity(NewUserBoundary boundary) {
		
		if ( boundary.getEmail().isBlank() ||
			 boundary.getUserName().isBlank() ||
			 boundary.getRole() == null ||
			 boundary.getAvatar().isBlank()
			) {
				throw new RuntimeException("You must enter all the details correct!");
			} 
		UserEntity entity = new UserEntity();		
		entity.setUserName(boundary.getUserName());
		entity.setAvatar(boundary.getAvatar());
		entity.setRole(boundary.getRole());

		return entity;

	}
}
