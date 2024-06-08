package demo.app.crud;

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
import demo.app.entities.UserEntity;
import demo.app.logics.AdminLogic;

@Service
public class AdminCrudImplementation implements AdminLogic {

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

	public AdminCrudImplementation(UserCrud userCrud, UserConverter userConverter, ObjectCrud objectCrud,
			CommandCrud commandCrud, CommandConverter commandConverter) {
		this.userCrud = userCrud;
		this.userConverter = userConverter;
		this.objectCrud = objectCrud;
		this.commandCrud = commandCrud;
		this.commandConverter = commandConverter;
	}

	@Override
	@Transactional
	public void deleteAllUsers() {
		this.userCrud.deleteAll();
		System.err.println("All user entries Deleted");
	}

	@Override
	@Transactional
	public void deleteAllObjects() {
		this.objectCrud.deleteAll();
		System.err.println("All object entries Deleted");
	}

	@Override
	@Transactional
	public void deleteAllCommandsHistory() {

		this.commandCrud.deleteAll();
		System.err.println("All commands entries Deleted");
	}

	@Override
	@Deprecated
	public List<UserBoundary> getAllUsers() {

		throw new MyBadRequestException("Deprecated opeation");
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(int size, int page) {
		List<UserEntity> entities = this.userCrud.findAll(PageRequest.of(page, size, Direction.ASC, "id")).toList();
		List<UserBoundary> rv = new ArrayList<>();
		for (UserEntity entity : entities)
			rv.add(this.userConverter.toBoundary(entity));

		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommands() {
		return this.commandCrud.findAll().stream().peek(entity -> System.err.println("* " + entity))
				.map(this.commandConverter::toBoundary).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommandsByMiniAppName(String miniAppName) {
		if (miniAppName == null || miniAppName.trim().isEmpty()) {
			throw new MyBadRequestException("Invalid miniAppName");
		}
		return this.commandCrud.findAllByMiniAppName(miniAppName).stream().map(this.commandConverter::toBoundary)
				.peek(System.err::println).toList();
	}

}
