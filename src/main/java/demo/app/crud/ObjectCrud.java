package demo.app.crud;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import demo.app.entities.ObjectEntity;

public interface ObjectCrud extends JpaRepository<ObjectEntity, String> {

	public List<ObjectEntity> findAllByActive(@Param("active") boolean active, Pageable pageable);

	public List<ObjectEntity> findAllByType(@Param("type") String type, Pageable pageable);

	public List<ObjectEntity> findAllByTypeAndActive(@Param("type") String type, @Param("active") boolean active,
			Pageable pageable);

	public List<ObjectEntity> findAllByAlias(@Param("alias") String alias, Pageable pageable);

	public List<ObjectEntity> findAllByAliasAndActive(@Param("alias") String alias, @Param("active") boolean active,
			Pageable pageable);

	public List<ObjectEntity> findAllByAliasContains(@Param("pattern") String pattern, Pageable pageable);

	public List<ObjectEntity> findAllByAliasContainsAndActive(@Param("pattern") String pattern,
			@Param("active") boolean active, Pageable pageable);

	public List<ObjectEntity> findAllByLocationWithin(@Param("lat") double lat, @Param("lng") double lng,
			@Param("distance") double distance, Pageable pageable);

	public List<ObjectEntity> findAllByLocationWithinAndActive(@Param("lat") double lat, @Param("lng") double lng,
			@Param("distance") double distance, @Param("active") boolean active, Pageable pageable);

}
