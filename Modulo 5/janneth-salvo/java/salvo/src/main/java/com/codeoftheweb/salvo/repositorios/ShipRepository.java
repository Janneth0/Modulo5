package com.codeoftheweb.salvo.repositorios;

import com.codeoftheweb.salvo.modelos.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource
public interface ShipRepository extends JpaRepository<Ship,Long> {
    Set<Ship> findByType (String type);
}