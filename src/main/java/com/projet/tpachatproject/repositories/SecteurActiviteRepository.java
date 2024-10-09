package com.projet.tpachatproject.repositories;


import com.projet.tpachatproject.entities.SecteurActivite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecteurActiviteRepository extends CrudRepository<SecteurActivite, Long> {

}
