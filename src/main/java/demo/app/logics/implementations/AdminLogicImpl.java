package demo.app.logics.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.converters.CommandConverter;
import demo.app.converters.UserConverter;
import demo.app.crud.CommandCrud;
import demo.app.crud.ObjectCrud;
import demo.app.crud.UserCrud;
import demo.app.entities.MiniAppCommandEntity;
import demo.app.entities.UserEntity;
import demo.app.enums.Role;
import demo.app.logics.AdminLogic;

@Service
public class AdminLogicImpl implements AdminLogic {

	private UserCrud userCrud;
	private UserConverter userConverter;
	private ObjectCrud objectCrud;
	private CommandCrud commandCrud;
	private CommandConverter commandConverter;

	private String springApplicationName;

	@Value("${spring.application.name:supperApp}")
	public void setSpringApplicationName(String springApplicationName) {
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}

	public AdminLogicImpl(UserCrud userCrud, UserConverter userConverter, ObjectCrud objectCrud,
			CommandCrud commandCrud, CommandConverter commandConverter) {
		this.userCrud = userCrud;
		this.userConverter = userConverter;
		this.objectCrud = objectCrud;
		this.commandCrud = commandCrud;
		this.commandConverter = commandConverter;
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllUsers(String userSuperapp, String userEmail) {

		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));

		if (!user.getRole().equals(Role.ADMIN)) {
			throw new MyForbiddenException("User is not authorized");
		}

		this.userCrud.deleteAll();
		System.err.println("All user entries Deleted");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllObjects(String userSuperapp, String userEmail) {

		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));

		if (!user.getRole().equals(Role.ADMIN)) {
			throw new MyForbiddenException("User is not authorized");
		}

		this.objectCrud.deleteAll();
		System.err.println("All object entries Deleted");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllCommandsHistory(String userSuperapp, String userEmail) {

		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));

		if (!user.getRole().equals(Role.ADMIN)) {
			throw new MyForbiddenException("User is not authorized");
		}

		this.commandCrud.deleteAll();
		System.err.println("All commands entries Deleted");
	}

	@Deprecated
	public List<UserBoundary> getAllUsers() {

		throw new MyBadRequestException("Deprecated opeation");
	}

	@Deprecated
	public List<MiniAppCommandBoundary> getAllCommands() {
		throw new MyBadRequestException("Deprecated opeation");
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(int size, int page, String userSuperapp, String userEmail) {
		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized1"));

		if (!user.getRole().equals(Role.ADMIN)) {
			throw new MyForbiddenException("User is not authorized2");
		}
		List<UserEntity> entities = this.userCrud.findAll(PageRequest.of(page, size, Direction.ASC, "id")).toList();
		List<UserBoundary> rv = new ArrayList<>();
		for (UserEntity entity : entities)
			rv.add(this.userConverter.toBoundary(entity));

		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommands(int size, int page, String userSuperapp, String userEmail) {

		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));

		if (!user.getRole().equals(Role.ADMIN)) {
			throw new MyForbiddenException("User is not authorized");
		}

		List<MiniAppCommandEntity> entities = this.commandCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, "commandId")).getContent();
		List<MiniAppCommandBoundary> rv = new ArrayList<>();
		for (MiniAppCommandEntity entity : entities)
			rv.add(this.commandConverter.toBoundary(entity));
		return rv;

	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommandsByMiniAppName(String miniAppName, int size, int page,
			String userSuperapp, String userEmail) {

		UserEntity user = userCrud.findById(userSuperapp + "_" + userEmail)
				.orElseThrow(() -> new MyForbiddenException("User not authorized"));

		if (!user.getRole().equals(Role.ADMIN)) {
			throw new MyForbiddenException("User is not authorized");
		}

		if (miniAppName == null || miniAppName.trim().isEmpty()) {
			throw new MyBadRequestException("Invalid miniAppName");
		}
		List<MiniAppCommandEntity> entities = this.commandCrud.findAllByMiniAppName(miniAppName,
				PageRequest.of(page, size, Direction.ASC, "commandId"));

		return entities.stream().map(this.commandConverter::toBoundary).peek(System.err::println).toList();
	}

	@Deprecated
	public List<MiniAppCommandBoundary> getAllCommandsByMiniAppName(String miniAppName) {
		throw new MyBadRequestException("Deprecated opeation");
	}

}
