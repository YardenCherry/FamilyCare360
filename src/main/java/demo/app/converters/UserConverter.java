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
		userId.setEmail(idParts[1]);
		userId.setSuperapp(idParts[0]);

		boundary.setUserId(userId);
		boundary.setUsername(entity.getUserName());
		boundary.setRole(entity.getRole());
		boundary.setAvatar(entity.getAvatar());

		return boundary;
	}

	public UserEntity toEntity(UserBoundary boundary) {
		UserEntity entity = new UserEntity();

		entity.setId(boundary.getUserId().getSuperapp() + "_" + boundary.getUserId().getEmail() + "_");
		entity.setUserName(boundary.getUsername());
		entity.setRole(boundary.getRole());
		entity.setAvatar(boundary.getAvatar());

		return entity;
	}

	public UserEntity toEntity(NewUserBoundary boundary) {

		UserEntity entity = new UserEntity();
		entity.setUserName(boundary.getUsername());
		entity.setAvatar(boundary.getAvatar() != null ? boundary.getAvatar() : "");
		entity.setRole(boundary.getRole());

		return entity;

	}
}
