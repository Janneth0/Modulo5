package com.codeoftheweb.salvo.repositorios;

import java.util.Optional;

import com.codeoftheweb.salvo.modelos.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player,Long> {
    Optional<Player> findByUserName (@Param("email") String email);
}
