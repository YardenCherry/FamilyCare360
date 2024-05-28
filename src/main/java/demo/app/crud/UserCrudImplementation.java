package demo.app.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.app.boundaries.UserBoundary;
import demo.app.converters.UserConverter;
import demo.app.entities.UserEntity;
import demo.app.logics.UserLogic;

@Service
public class UserCrudImplementation implements UserLogic {
	private final UserCrud userCrud;
    private final UserConverter userConverter;

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
    public UserBoundary storeInDatabase(UserBoundary userBoundary) {
        UserEntity entity = userConverter.toEntity(userBoundary);
        entity = this.userCrud.save(entity);
        return userConverter.toBoundary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserBoundary> getSpecificDemoFromDatabase(String id) {
        return this.userCrud.findById(id)
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
    public void deleteAll() {
        this.userCrud.deleteAll();
    }

    @Override
    @Transactional
    public void updateById(String id, UserBoundary update) {
        // Implement the update logic as needed
    }
}
