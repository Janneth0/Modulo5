package com.codeoftheweb.salvo.repositorios;

import com.codeoftheweb.salvo.modelos.Salvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SalvoRepository extends JpaRepository<Salvo,Long> {
}
