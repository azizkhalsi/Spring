package com.projet.tpachatproject.repositories;

import com.projet.tpachatproject.entities.DetailFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailFournisseurRepository  extends JpaRepository<DetailFournisseur, Long>{

}
