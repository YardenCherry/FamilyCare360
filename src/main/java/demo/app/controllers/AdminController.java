package demo.app.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.app.boundaries.MiniAppCommandBoundary;
import demo.app.boundaries.ObjectBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.logics.AdminLogic;

@RestController
@RequestMapping(path ={"/superapp/admin"})
public class AdminController {

		private AdminLogic adminLogic;

		public AdminController(AdminLogic adminLogic) {
			this.adminLogic = adminLogic;
		}
		
		@DeleteMapping (
				path = { "/superapp/admin/users" })
		public void deleteAllUsers() {
			this.adminLogic.deleteAllUsers();
		}

		@DeleteMapping (
				path = { "/superapp/admin/objects" })
		public void deleteAllObjects() {
			this.adminLogic.deleteAllObjects();
			
		}

		@DeleteMapping (
				path = { "/superapp/admin/miniapp" })
		public void deleteAllCommandsHistory() {
			this.adminLogic.deleteAllCommandsHistory();
		}

		@GetMapping(
				path = {"/superapp/admin/users"},
				produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[] getAllUsers() {
			return adminLogic
					.getAllUsers()
					.toArray(new UserBoundary[0]);
		}

		@GetMapping(
				path = {"/superapp/admin/miniapp"},
				produces = MediaType.APPLICATION_JSON_VALUE)
		public MiniAppCommandBoundary[] getAllCommands() {
			return adminLogic
					.getAllCommands()
					.toArray(new MiniAppCommandBoundary[0]);
		}
		
		@GetMapping(
				path = {"/superapp/admin/objects"},
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ObjectBoundary[] getAllObjects() {
			return adminLogic
					.getAllObjects()
					.toArray(new ObjectBoundary[0]);
		}

//		@Override
//		@Transactional(readOnly = true)
//		public Optional<MiniAppCommandBoundary> getCommandsOfSpecificMiniApp(String miniAppName) {
//			
//		//	String id = superapp + "#" + email;
//			Optional<UserEntity> optionalEntity = this.commandCrud.findBy(id);	
//			
//			if (!optionalEntity.isEmpty()) {
//				System.err.println("Read from Database: " + optionalEntity.get());
//			}else {
//				System.err.println("UserEntity with email: " + email 
//						+ " and superapp " + superapp + " Does not exist in database");
//			}
//			
//			return optionalEntity
//					.map(this.userConverter::toBoundary);
//			return Optional.empty();
//		}
}
