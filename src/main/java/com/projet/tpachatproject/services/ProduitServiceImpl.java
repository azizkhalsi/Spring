package com.projet.tpachatproject.services;



import com.projet.tpachatproject.entities.Produit;
import com.projet.tpachatproject.entities.Stock;
import com.projet.tpachatproject.repositories.CategorieProduitRepository;
import com.projet.tpachatproject.repositories.ProduitRepository;
import com.projet.tpachatproject.repositories.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ProduitServiceImpl implements IProduitService {

 	IStockService stockService;
	ProduitRepository produitRepository;
	StockRepository stockRepository;
	CategorieProduitRepository categorieProduitRepository;

	@Override
	public List<Produit> retrieveAllProduits() {
		List<Produit> produits = produitRepository.findAll();
		for (Produit produit : produits) {
			log.info(" Produit : " + produit);
		}
		return produits;
	}

	@Transactional
	public Produit addProduit(Produit p) {
		produitRepository.save(p);
		return p;
	}

	

	@Override
	public void deleteProduit(Long produitId) {
		produitRepository.deleteById(produitId);
	}

	@Override
	public Produit updateProduit(Produit p) {
		return produitRepository.save(p);
	}

	@Override
	public Produit retrieveProduit(Long produitId) {
		Produit produit = produitRepository.findById(produitId).orElse(null);
		log.info("produit :" + produit);
		return produit;
	}

	@Override
	public void assignProduitToStock(Long idProduit, Long idStock) {
		Produit produit = produitRepository.findById(idProduit)
				.orElseThrow(() -> new EntityNotFoundException("Produit with ID " + idProduit + " not found"));

		Stock stock = stockRepository.findById(idStock)
				.orElseThrow(() -> new EntityNotFoundException("Stock with ID " + idStock + " not found"));

		produit.setStock(stock);
		produitRepository.save(produit);
	}

	@Override
	public boolean verifierDates(Produit produit) {
			return produit.getDateCreation().before(produit.getDateDerniereModification());
	}
	@Override
	public List<Produit> findByStock(Stock stock) {


		return produitRepository.findByStock(stock);


	}


}