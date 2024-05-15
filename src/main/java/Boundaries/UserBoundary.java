package Boundaries;

import Enums.Role;
import Entities.UserEntity;
import Objects.UserId;

public class UserBoundary {


    private UserId userId;
    private Role role;
    private String userName;
    private String avatar;

    public UserBoundary(){}

    public UserBoundary(UserEntity userEntity) {
        this.userId = new UserId();
        String[] splitId = userEntity.getId().split("_");
        this.getUserId().setEmail(splitId[0]);
        this.getUserId().setSuperapp(splitId[1]);
        this.setUserName(userEntity.getUserName());
        this.setRole(userEntity.getRole());
        this.setAvatar(userEntity.getAvatar());

    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "UserBoundary{" +
                "userId=" + userId +
                ", role=" + role +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
