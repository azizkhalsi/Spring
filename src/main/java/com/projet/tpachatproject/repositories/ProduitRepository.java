package com.projet.tpachatproject.repositories;


import com.projet.tpachatproject.entities.Produit;
import com.projet.tpachatproject.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {


	 List<Produit> findByStock(Stock stock);
	

	

}
