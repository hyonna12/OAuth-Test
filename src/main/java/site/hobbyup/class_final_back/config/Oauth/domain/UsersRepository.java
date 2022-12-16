package site.hobbyup.class_final_back.config.Oauth.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Query("select u from User u where u.username = :username")
    Optional<Users> findByUsername(String username);

}
