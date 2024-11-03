package com.projet.tpachatproject.repositories;

import com.projet.tpachatproject.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

	
	
	@Query("SELECT s FROM Stock s where s.qte< s.qteMin")
	List<Stock> retrieveStatusStock();
}
