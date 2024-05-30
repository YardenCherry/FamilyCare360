package demo.app.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.app.entities.ObjectEntity;

public interface ObjectCrud extends JpaRepository<ObjectEntity, String> {

}
