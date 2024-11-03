package com.projet.tpachatproject.repositories;
import com.projet.tpachatproject.entities.Operateur;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperateurRepository extends CrudRepository<Operateur, Long> {

}
