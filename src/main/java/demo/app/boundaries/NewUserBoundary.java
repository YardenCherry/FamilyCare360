package demo.app.boundaries;

import demo.app.entities.UserEntity;
import demo.app.enums.Role;

public class NewUserBoundary {

    private String email;
    private Role role;
    private String userName;
    private String avatar;

    public NewUserBoundary() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
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

    public UserEntity toEntity() {
        UserEntity entity = new UserEntity();

        entity.setId(this.getEmail());
        entity.setRole(this.getRole());
        entity.setUserName(this.getUserName() == null ? "Anonymous" : this.getUserName());
        entity.setAvatar(this.getAvatar() == null ? "F" : this.getAvatar());

        return entity;

    }

    @Override
    public String toString() {
        return "NewUserBoundary{" +
                "email='" + email + '\'' +
                ", role=" + role +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}

