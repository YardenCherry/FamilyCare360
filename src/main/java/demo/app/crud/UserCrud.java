package demo.app.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.app.entities.UserEntity;

public interface UserCrud extends JpaRepository<UserEntity, String> {

}
