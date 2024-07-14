package demo.app.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import demo.app.entities.ObjectEntity;
import demo.app.entities.UserEntity;

public interface ObjectCrud extends JpaRepository<ObjectEntity, String> {

	public Optional<ObjectEntity> findByObjectIdAndActiveTrue(@Param("objectId") String objectId);

	public List<ObjectEntity> findAllByActive(@Param("active") boolean active, Pageable pageable);

	public List<ObjectEntity> findAllByType(@Param("type") String type, Pageable pageable);

	public List<ObjectEntity> findAllByTypeAndActive(@Param("type") String type, @Param("active") boolean active,
			Pageable pageable);

	public List<ObjectEntity> findAllByAlias(@Param("alias") String alias, Pageable pageable);

	public List<ObjectEntity> findAllByAliasAndActive(@Param("alias") String alias, @Param("active") boolean active,
			Pageable pageable);

	public List<ObjectEntity> findAllByAliasLikeIgnoreCase(@Param("pattern") String pattern, Pageable pageable);

	public List<ObjectEntity> findAllByAliasLikeIgnoreCaseAndActive(@Param("pattern") String pattern,
	@Param("active") boolean active, Pageable pageable);

	
	
	@Query(value = "SELECT * FROM objects o " 
	           + "WHERE ST_DWithin(ST_MakePoint(o.latitude, o.longitude)::geography, ST_MakePoint(:lat, :lng)::geography, :radius) "
				, nativeQuery = true)
		public List<ObjectEntity> findAllByLocationWithin(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") double radius, Pageable pageable);
		
		@Query(value = "SELECT * FROM objects o " 
				+ "WHERE ST_DWithin(ST_MakePoint(o.latitude, o.longitude)::geography, ST_MakePoint(:lat, :lng)::geography, :radius) "
				+ "AND o.active=true"
				, nativeQuery = true)
		public List<ObjectEntity> findAllByLocationWithinAndActiveTrue(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") double radius, Pageable pageable);

		
		@Query(value = "SELECT * FROM objects o " 
		        + "WHERE o.type = :type AND o.active = true "
		        + "ORDER BY ST_Distance(ST_MakePoint(o.latitude, o.longitude)::geography, ST_MakePoint(:latitude, :longitude)::geography) ASC", 
		        nativeQuery = true)
		public List<ObjectEntity> findAllByTypeAndLocationAndActiveTrue(
		        @Param("type") String type,
		        @Param("latitude") double latitude,
		        @Param("longitude") double longitude,
		        Pageable pageable);

//	 finds all entities with a latitude and longitude within a specified rectangular area
//	 public List<ObjectEntity> findAllByLatitudeBetweenAndLongitudeBetween(
//	            double startLat, double endLat, 
//	            double startLng, double endLng, 
//	            Pageable pageable);

	
	public List<ObjectEntity> findAllByTypeAndAliasAndActiveTrue(@Param("type") String type,
			@Param("alias") String alias, Pageable pageable);

	public Optional<ObjectEntity> findByObjectId(@Param("objectId") String objectId);
	
	
	@Query(value = "SELECT * FROM objects o WHERE " + "o.type = :type AND o.active = true AND o.alias = :alias"
			+ "ORDER BY o.objectDetails.get(selectedDate)) ASC", nativeQuery = true)
	public List<ObjectEntity> findAllByTypeAndAliasAndActiveTrueASC(@Param("type") String type,
			@Param("alias") String alias, Pageable pageable);
	
	@Query(value = "SELECT * FROM objects o WHERE " + "o.type = :type AND o.active = true AND o.alias = :alias"
			+ "ORDER BY o.objectDetails.get(selectedDate)) DESC", nativeQuery = true)
	public List<ObjectEntity> findAllByTypeAndAliasAndActiveTrueDESC(@Param("type") String type,
			@Param("alias") String alias, Pageable pageable);

}
