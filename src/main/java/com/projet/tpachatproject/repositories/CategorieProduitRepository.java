package com.projet.tpachatproject.repositories;


import com.projet.tpachatproject.entities.CategorieProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieProduitRepository extends JpaRepository<CategorieProduit, Long> {

}
