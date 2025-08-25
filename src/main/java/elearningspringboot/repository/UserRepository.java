package elearningspringboot.repository;

import elearningspringboot.entity.User;
import elearningspringboot.enumeration.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.status = :status AND u.role.role = :role")
    List<User> findByStatusAndRole(@Param("status") Status status, @Param("role") String role);

    @Query("SELECT u FROM User u WHERE lower(u.fullName) LIKE %:keyword% OR lower(u.email) LIKE %:keyword%")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);
}
