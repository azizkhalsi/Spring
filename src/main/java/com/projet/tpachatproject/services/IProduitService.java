package com.projet.tpachatproject.services;


import com.projet.tpachatproject.entities.Produit;
import com.projet.tpachatproject.entities.Stock;

import java.util.List;

public interface IProduitService {

	List<Produit> retrieveAllProduits();

	Produit addProduit(Produit p);

	void deleteProduit(Long id);

	Produit updateProduit(Produit p);

	Produit retrieveProduit(Long id);

	void assignProduitToStock(Long idProduit, Long idStock);
	public boolean verifierDates(Produit produit);
	public List<Produit> findByStock(Stock stock);

}
