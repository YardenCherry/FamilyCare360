package Crud;


import org.springframework.data.jpa.repository.JpaRepository;

import Entities.ObjectEntity;


public interface ObjectCrud extends JpaRepository<ObjectEntity, String> {

}
