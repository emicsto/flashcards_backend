package me.emicsto.flashcards.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRefreshTokenRepository extends CrudRepository<UserRefreshToken, Long> {
    Optional<UserRefreshToken> findByToken(String token);
}
