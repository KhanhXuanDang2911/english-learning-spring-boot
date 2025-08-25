package elearningspringboot.repository;

import elearningspringboot.entity.Permission;
import elearningspringboot.enumeration.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    boolean existsByName(String name);
    @Query("select case when count(p) > 0 then true else false end from Permission p where p.name = :name and p.id != :id")
    boolean existsByNameExceptForId(@Param("name") String name, @Param("id") Long id);
}
