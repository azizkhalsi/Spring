package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.Produit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j

public class ProduitServiceImplTest {

	@Autowired
	IProduitService produitService;

	@Test
	public void testAddProduit() throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date dateCreation = dateFormat.parse("30/09/2000");

		Produit produit = new Produit(
				null,
				"P001",
				"Produit A",
				100.0f,
				dateCreation,
				new Date(),
				null,
				null,
				null
		);

		// Ajouter le produit
		Produit savedProduit = produitService.addProduit(produit);
		System.out.print("Produit " + savedProduit);

		// Vérifier que le produit a bien été ajouté
		assertNotNull(savedProduit.getIdProduit());
		assertNull(savedProduit.getCategorieProduit());  // Pas de catégorie produit pour l'instant
		assertTrue(savedProduit.getLibelleProduit().length() > 0);

		// Supprimer le produit
		produitService.deleteProduit(savedProduit.getIdProduit());
	}

	@Test
	public void testDeleteProduit() throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date dateCreation = dateFormat.parse("30/09/2000");

		// Créer un produit
		Produit produit = new Produit(
				null,
				"P001",
				"Produit B",
				100.0f,
				dateCreation,
				new Date(),
				null,
				null,
				null
		);


		// Ajouter le produit
		Produit savedProduit = produitService.addProduit(produit);

		// Supprimer le produit
		produitService.deleteProduit(savedProduit.getIdProduit());

		// Vérifier que le produit n'existe plus
		assertNull(produitService.retrieveProduit(savedProduit.getIdProduit()));
	}

	@Test
	public void testRetrieveAllProduits() throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date dateCreation = dateFormat.parse("30/09/2000");

		// Récupérer tous les produits avant d'ajouter un nouveau produit
		List<Produit> produits = produitService.retrieveAllProduits();
		int expected = produits.size();

		// Créer et ajouter un nouveau produit
		Produit produit = new Produit(
				null,                // idProduit
				"P003",              // codeProduit
				"Produit C",         // libelleProduit
				200.0f,              // prix
				dateCreation,        // dateCreation
				new Date(),          // dateDerniereModification
				null,                // stock
				null,                // detailFacture
				null                 // categorieProduit
		);
		Produit savedProduit = produitService.addProduit(produit);

		// Vérifier que la taille de la liste des produits a augmenté
		assertEquals(expected + 1, produitService.retrieveAllProduits().size());

		// Supprimer le produit ajouté
		produitService.deleteProduit(savedProduit.getIdProduit());
	}

	/*@Test
	public void testGetProduitsByDateCreation() throws ParseException {
		// Formatter la date
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = dateFormat.parse("28/09/2000");
		Date endDate = dateFormat.parse("30/09/2005");

		// Récupérer les produits créés entre les dates spécifiées
		List<Produit> produits = produitService.getProduitsByDateCreation(startDate, endDate);
		log.info("Nombre de produits : " + produits.size());

		// Afficher les produits récupérés
		for (Produit produit : produits) {
			log.info("Produit : " + produit.getLibelleProduit() + " créé le " + produit.getDateCreation());
		}
	}*/

}
