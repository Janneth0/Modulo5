package com.codeoftheweb.salvo.repositorios;

import com.codeoftheweb.salvo.modelos.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository<Score,Long> {
}
