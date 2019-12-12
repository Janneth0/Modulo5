package com.codeoftheweb.salvo.repositorios;

import com.codeoftheweb.salvo.modelos.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game,Long> {


}
