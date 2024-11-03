package com.projet.tpachatproject.repositories;

import com.projet.tpachatproject.entities.DetailFacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailFactureRepository extends JpaRepository<DetailFacture, Long> {

}
