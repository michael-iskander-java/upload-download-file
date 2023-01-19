package sa.com.stc.file.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import sa.com.stc.file.entity.PermissionGroup;

public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Integer> {
	Optional<PermissionGroup> findByGroupName(String groupName);
}
