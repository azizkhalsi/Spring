package com.projet.tpachatproject.services;



import com.projet.tpachatproject.entities.Produit;
import com.projet.tpachatproject.entities.Stock;
import com.projet.tpachatproject.repositories.CategorieProduitRepository;
import com.projet.tpachatproject.repositories.ProduitRepository;
import com.projet.tpachatproject.repositories.StockRepository;
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
		List<Produit> produits = (List<Produit>) produitRepository.findAll();
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
		Produit produit = produitRepository.findById(idProduit).orElse(null);
		Stock stock = stockRepository.findById(idStock).orElse(null);
		produit.setStock(stock);
		produitRepository.save(produit);

	}

	@Override
	public boolean verifierDates(Produit produit) {
		return false;
	}

	@Override
	public List<Produit> findByStock(Stock stock) {
		return null;
	}


	public String checkAndUpdateProduitStock(Long idProduit, int quantityToAdd) {
		Produit produit = produitRepository.findById(idProduit).orElse(null);
		if (produit == null) {
			return "Produit not found";
		}

		Stock stock = produit.getStock();
		if (stock == null) {
			return "Produit has no associated stock";
		}

		int currentQuantity = stock.getQte();
		int newQuantity = currentQuantity + quantityToAdd;

		if (newQuantity < 0) {
			return "Cannot remove more items than available in stock";
		}

		if (newQuantity < stock.getQteMin()) {
			log.warn("Stock level below minimum for product: " + produit.getLibelleProduit());
		}

		stock.setQte(newQuantity);
		stockRepository.save(stock);

		if (newQuantity > stock.getQteMin() * 2) {
			return "Stock level high";
		} else if (newQuantity > stock.getQteMin()) {
			return "Stock level adequate";
		} else {
			return "Stock level low";
		}

	}

}