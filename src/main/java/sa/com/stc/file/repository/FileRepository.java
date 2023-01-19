package sa.com.stc.file.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sa.com.stc.file.entity.File;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<File, Long>{
	
	@Query("SELECT f FROM File f LEFT JOIN FETCH f.item WHERE f.item.id = :id")	
	Optional<File> findByItem(@Param("id") Long id);

}
