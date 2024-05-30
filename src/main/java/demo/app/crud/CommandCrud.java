package demo.app.crud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.app.entities.MiniAppCommandEntity;

public interface CommandCrud extends JpaRepository<MiniAppCommandEntity, String> {

	List<MiniAppCommandEntity> findAllByMiniAppName(String miniAppName);

}
