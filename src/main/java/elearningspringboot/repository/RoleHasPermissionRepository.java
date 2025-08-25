package elearningspringboot.repository;

import elearningspringboot.entity.Permission;
import elearningspringboot.entity.Role;
import elearningspringboot.entity.RoleHasPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleHasPermissionRepository extends JpaRepository<RoleHasPermission, Long> {
    boolean existsByRoleAndPermission(Role role, Permission permission);
}
