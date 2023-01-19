package sa.com.stc.file.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sa.com.stc.file.entity.Permission;
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer>{

	Optional<Permission> findByEmail(String email);

}
