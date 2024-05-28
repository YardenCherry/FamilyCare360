package demo.app.converters;

import org.springframework.stereotype.Component;

import demo.app.boundaries.UserBoundary;
import demo.app.entities.UserEntity;
import demo.app.objects.UserId;
@Component
public class UserConverter {

    public UserBoundary toBoundary(UserEntity entity) {
        UserBoundary rv = new UserBoundary();
        UserId userId = new UserId();
        
        String[] idParts = entity.getId().split("_");
        userId.setEmail(idParts[0]);
        userId.setSuperapp(idParts[1]);
        
        rv.setUserId(userId);
        rv.setUserName(entity.getUserName());
        rv.setRole(entity.getRole());
        rv.setAvatar(entity.getAvatar());
        
        return rv;
    }

    public UserEntity toEntity(UserBoundary boundary) {
        UserEntity rv = new UserEntity();
        
        rv.setId(boundary.getUserId().getEmail() + "_" + boundary.getUserId().getSuperapp());
        rv.setUserName(boundary.getUserName());
        rv.setRole(boundary.getRole());
        
        if (boundary.getAvatar() != null) {
            rv.setAvatar(boundary.getAvatar());
        } else {
            rv.setAvatar("");
        }
        
        return rv;
    }
}
