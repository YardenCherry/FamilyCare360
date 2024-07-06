package demo.app.logics.implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.converters.UserConverter;
import demo.app.crud.UserCrud;
import demo.app.entities.UserEntity;
import demo.app.logics.UserLogic;
import demo.app.objects.InputValidation;

@Service
public class UserLogicImpl implements UserLogic {
	private final UserCrud userCrud;
	private final UserConverter userConverter;
	private String springApplicationName;

	public UserLogicImpl(UserCrud userCrud, UserConverter userConverter) {
		this.userCrud = userCrud;
		this.userConverter = userConverter;
	}

	@Value("${spring.application.name:supperapp}")
	public void setup(String name) {
		this.springApplicationName = name;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}

	@Override
	@Transactional(readOnly = false)
	public UserBoundary createNewUser(NewUserBoundary userBoundary) {
		validateNewUserBoundary(userBoundary);

		String userId = springApplicationName + "_" + userBoundary.getEmail();
		if (userCrud.existsById(userId)) {
			throw new MyBadRequestException("User with email " + userBoundary.getEmail() + " already exists.");
		}

		UserEntity entity = userConverter.toEntity(userBoundary);
		entity.setId(userId);
		entity = userCrud.save(entity);
		return userConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserBoundary> getSpecificUser(String superapp, String email) {
		if (!InputValidation.isValidEmail(email))
			throw new MyBadRequestException("Invalid email format");
		return this.userCrud.findById(superapp + "_" + email).map(userConverter::toBoundary);
	}

	@Override
	@Transactional(readOnly = false)
	public Optional<UserBoundary> updateById(String superapp, String email, UserBoundary update) {
		if (!InputValidation.isValidEmail(email))
			throw new MyBadRequestException("Invalid email format");

		String id = superapp + "_" + email;
		UserEntity entity = this.userCrud.findById(id).orElseThrow(() -> new MyBadRequestException(
				"UserEntity with email: " + email + " and superapp " + superapp + " does not exist in database"));

		if (update.getUsername() != null && !update.getUsername().trim().isEmpty()) {
			entity.setUserName(update.getUsername());
		}

		if (update.getRole() != null && InputValidation.isValidRole(update.getRole().toString())) {
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
		if (userBoundary == null) {
			throw new MyBadRequestException("UserBoundary cannot be null");
		}
		if (!InputValidation.isValidEmail(userBoundary.getEmail())) {
			throw new MyBadRequestException("Invalid email format");
		}
		if (userBoundary.getRole() == null || !InputValidation.isValidRole(userBoundary.getRole().toString())) {
			throw new MyBadRequestException("Invalid role");
		}
		if (userBoundary.getUsername() == null || userBoundary.getUsername().length() < 1) {
			throw new MyBadRequestException("Username must be at least 1 characters");
		}
		if (userBoundary.getAvatar() == null || userBoundary.getAvatar().length() < 1) {
			throw new MyBadRequestException("Avatar must be at least 1 characters");
		}
	}

}
