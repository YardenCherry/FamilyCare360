package demo.app.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.converters.UserConverter;
import demo.app.entities.UserEntity;
import demo.app.enums.Role;
import demo.app.logics.UserLogic;

@Service
public class UserCrudImplementation implements UserLogic {
    private final UserCrud userCrud;
    private final UserConverter userConverter;
    private String springApplicationName;

    public UserCrudImplementation(UserCrud userCrud, UserConverter userConverter) {
        this.userCrud = userCrud;
        this.userConverter = userConverter;
    }

    @Value("${spring.application.name:supperapp}")
    public void setup(String name) {
        this.springApplicationName = name;
        System.err.println("*** " + name);
    }

    @Override
    @Transactional
    public UserBoundary createNewUser(NewUserBoundary userBoundary) {
        validateNewUserBoundary(userBoundary);
        UserEntity entity = userConverter.toEntity(userBoundary);
        entity.setId(springApplicationName + "_" + userBoundary.getEmail());
        entity = userCrud.save(entity);
        return userConverter.toBoundary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserBoundary> getSpecificUser(String superapp, String email) {
        validateEmail(email);
        return this.userCrud.findById(superapp + "_" + email).map(userConverter::toBoundary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBoundary> getAll() {
        List<UserEntity> entities = this.userCrud.findAll();
        List<UserBoundary> rv = new ArrayList<>();
        for (UserEntity entity : entities) {
            rv.add(userConverter.toBoundary(entity));
        }
        return rv;
    }

    @Override
    @Transactional
    public Optional<UserBoundary> updateById(String superapp, String email, UserBoundary update) {
        validateEmail(email);
        validateUserBoundary(update);
        String id = superapp + "_" + email;
        UserEntity entity = this.userCrud.findById(id).orElseThrow(() -> new MyBadRequestException(
                "UserEntity with email: " + email + " and superapp " + superapp + " does not exist in database"));

        if (update.getUserName() != null && !update.getUserName().trim().isEmpty()) {
            entity.setUserName(update.getUserName());
        }

        if (update.getRole() != null && !isValidRole(update.getRole().toString())) {
            entity.setRole(update.getRole());
        }

        if (update.getAvatar() != null && !update.getAvatar().trim().isEmpty()) {
            entity.setAvatar(update.getAvatar());
        }

        this.userCrud.save(entity);

        System.err.println("Updated in database: " + entity);
        return Optional.of(update);
    }

    private void validateNewUserBoundary(NewUserBoundary userBoundary) {
        if (userBoundary.getEmail() == null || !isValidEmail(userBoundary.getEmail())) {
            throw new MyBadRequestException("Invalid email format");
        }
        if (userBoundary.getRole() == null || !isValidRole(userBoundary.getRole().toString())) {
            throw new MyBadRequestException("Invalid role");
        }
        if (userBoundary.getUserName() == null || userBoundary.getUserName().length() < 2) {
            throw new MyBadRequestException("Username must be at least 2 characters");
        }
        if (userBoundary.getAvatar() == null || userBoundary.getAvatar().isEmpty()) {
            throw new MyBadRequestException("Avatar is required");
        }
    }

    private void validateUserBoundary(UserBoundary userBoundary) {
        if (userBoundary.getUserId() == null || userBoundary.getUserId().getEmail() == null || !isValidEmail(userBoundary.getUserId().getEmail())) {
            throw new MyBadRequestException("Invalid email format");
        }
        if (userBoundary.getRole() == null || !isValidRole(userBoundary.getRole().toString())) {
            throw new MyBadRequestException("Invalid role");
        }
        if (userBoundary.getUserName() == null || userBoundary.getUserName().length() < 1) {
            throw new MyBadRequestException("Username must be at least 1 characters");
        }
        if (userBoundary.getAvatar() == null || userBoundary.getAvatar().isEmpty()) {
            throw new MyBadRequestException("Avatar is required");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !isValidEmail(email)) {
            throw new MyBadRequestException("Invalid email format");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    
    private boolean isValidRole(String role) {
        return Objects.equals(role, Role.ADMIN.toString()) ||
               Objects.equals(role, Role.MINIAPP_USER.toString()) ||
               Objects.equals(role, Role.SUPERAPP_USER.toString());
    }
}
