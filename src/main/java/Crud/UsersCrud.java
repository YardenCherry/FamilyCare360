package Crud;

import org.springframework.data.jpa.repository.JpaRepository;

import Entities.UserEntity;

public interface UsersCrud extends JpaRepository<UserEntity, String> {

}
