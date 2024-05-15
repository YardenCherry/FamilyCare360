package Crud;

import org.springframework.data.jpa.repository.JpaRepository;

import Entities.MiniAppCommandEntity;


public interface CommandCrud extends JpaRepository<MiniAppCommandEntity, String> {
	

}
