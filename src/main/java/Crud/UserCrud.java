package Crud;

import org.springframework.data.jpa.repository.JpaRepository;

import Entities.UserEntity;

public interface UserCrud extends JpaRepository<UserEntity, String> {

}
