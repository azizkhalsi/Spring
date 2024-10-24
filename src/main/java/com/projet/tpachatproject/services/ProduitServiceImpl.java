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

import java.util.Date;
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
		Date dateCreation = produit.getDateCreation();
		Date dateModification = produit.getDateDerniereModification();

		// 1. Vérification si la date de création ou de modification est nulle
		if (dateCreation == null || dateModification == null) {
			return false;
		}

		// 2. Vérification si la date de modification est après la date de création
		if (dateModification.before(dateCreation)) {
			return false;
		}

		// 3. Vérification si la date de modification est dans le futur
		Date today = new Date();
		if (dateModification.after(today)) {
			return false;
		}

		// 4. Vérification si la modification est dans les 30 jours suivant la création
		long diffInMillies = Math.abs(dateModification.getTime() - dateCreation.getTime());
		long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
		if (diffInDays > 30) {
			return false;
		}



		// Si toutes les validations passent
		return true;
	}


	@Override
	public List<Produit> findByStock(Stock stock) {

		List<Produit> produits = produitRepository.findByStock(stock);

		return produits ;
	}


}