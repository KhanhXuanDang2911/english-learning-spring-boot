package elearningspringboot.repository;

import elearningspringboot.entity.WhitelistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhitelistTokenRepository extends JpaRepository<WhitelistToken, Long> {
    void deleteByToken(String token);
    void deleteByEmail(String email);
    boolean existsByToken(String token);
}
