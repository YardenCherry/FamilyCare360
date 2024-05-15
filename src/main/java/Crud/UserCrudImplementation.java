package Crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Boundaries.UserBoundary;
import Entities.UserEntity;
import Logics.UserLogic;

@Service
public class UserCrudImplementation implements UserLogic {
	private UserCrud userCrud ;

	public UserCrudImplementation(UserCrud userCrud) {
		this.userCrud = userCrud;
	}
	
	@Override
	@Transactional(readOnly = false)
	public UserBoundary storeInDatabase(UserBoundary userBoundary)
	{
//		UserId userId=new UserId();
//		userId.setId(UUID.randomUUID().toString());
//		userBoundary.setUserId(userId);
		UserEntity entity = userBoundary.toEntity();
		
		entity = this.userCrud.save(entity);
		return new UserBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserBoundary> getSpecificDemoFromDatabase(String id) {
		return this.userCrud
			.findById(id)
			.map(entity->new UserBoundary(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAll() {
		List<UserEntity> entities = 
		  this.userCrud
			.findAll();
		
		List<UserBoundary> rv = new ArrayList<>();
		
		for (UserEntity entity : entities) {
			rv.add(new UserBoundary(entity));
		}
		
		return rv;
	}

	@Override
	public Optional<Void> updateObject(UserBoundary userBoundary, String id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	
	
}
