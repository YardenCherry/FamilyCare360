package demo.app.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.app.boundaries.NewUserBoundary;
import demo.app.boundaries.UserBoundary;
import demo.app.converters.UserConverter;
import demo.app.entities.UserEntity;
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
    
	@Value("${spring.application.name:supperApp}")
    public void setup(String name) {
        System.err.println("*** " + name);
    }
	
	@Override
    @Transactional
    public UserBoundary createNewUser(NewUserBoundary userBoundary) {
		UserEntity entity = userConverter.toEntity(userBoundary);
		entity.setId(springApplicationName 
						+ "_" 
						+ userBoundary.getEmail());
		entity = userCrud.save(entity);
		return userConverter.toBoundary(entity);		
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserBoundary> getSpecificUser(String superapp,String email) {
        return this.userCrud.findById(email+ "_" + superapp)
            .map(userConverter::toBoundary);
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
    public void updateById(String superapp,String email, UserBoundary update) {
    		String id = email + "_" + superapp;
    		UserEntity entity = this.userCrud
    				.findById(id)
    				.orElseThrow(() -> new RuntimeException("UserEntity with email: " + email 
    						+ " and superapp " + superapp + " Does not exist in database"));

    		if (update.getUserName() != null)
    			entity.setUserName(update.getUserName());
    		
    		if (update.getRole() != null) 
    			entity.setRole(update.getRole());
    			
    		if (update.getAvatar() != null) 
    				entity.setAvatar(update.getAvatar());
    		
    		this.userCrud.save(entity);
    	
    		System.err.println("updated in database: " + entity );

    	}
}
