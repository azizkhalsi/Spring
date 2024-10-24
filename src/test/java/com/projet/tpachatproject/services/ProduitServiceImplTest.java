package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.Produit;
import com.projet.tpachatproject.entities.Stock;

import com.projet.tpachatproject.repositories.ProduitRepository;
import com.projet.tpachatproject.repositories.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@Slf4j
@ExtendWith(MockitoExtension.class)
 class ProduitServiceImplTest {

    @Mock
	private ProduitRepository produitRepository;

	@Mock
	private StockRepository stockRepository;

	@InjectMocks
	private StockServiceImpl stockService;

	@InjectMocks
	private ProduitServiceImpl produitService;

	@BeforeEach
	public void setup() {
		// This is optional if using @ExtendWith(MockitoExtension.class)
		MockitoAnnotations.openMocks(this);
	}


	//@Test
	public void testDateValidation() throws ParseException {

		Stock stock = new Stock();
		stock.setLibelleStock("Libelle Stock Test");
		stock.setQte(100);
		stock.setQteMin(10);

		// Sauvegarder le Stock
		Stock savedStock = stockService.addStock(stock);

		// Définir les dates de création et de modification
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date dateCreation = dateFormat.parse("30/09/2000");
		Date dateModification1 = dateFormat.parse("01/10/2000");
		Date dateModification2 = dateFormat.parse("20/09/2000");


		// Créer et ajouter un nouveau produit
		Produit produit1 = new Produit(
				null,
				"code 1",
				"Produit 2",
				200.0f,
				dateCreation,
				dateModification1,
				savedStock,
				null,
				null
		);
		Produit produit2 = new Produit(
				null,
				"code 2",
				"Produit 2",
				200.0f,
				dateCreation,
				dateModification2,
				savedStock,
				null,
				null
		);

		// Sauvegarder les produits
		Produit savedProduit1 = produitService.addProduit(produit1);
		Produit savedProduit2 = produitService.addProduit(produit2);


		List<Produit> produits = produitService.findByStock(savedStock);


		assertTrue(produits.contains(savedProduit1));
		assertTrue(produits.contains(savedProduit2));


		assertTrue(produitService.verifierDates(savedProduit2));

		// Nettoyer les données en supprimant les produits et le stock
		produitService.deleteProduit(savedProduit1.getIdProduit());
		produitService.deleteProduit(savedProduit2.getIdProduit());
		stockService.deleteStock(savedStock.getIdStock());
	}

	@Test
	 void testDateValidationMockito() throws ParseException {

		//ARRANGE
		Stock stock = new Stock();
		stock.setLibelleStock("Libelle Stock Test");
		stock.setQte(100);
		stock.setQteMin(10);
		stock.setIdStock(1L);

		when(stockRepository.save(any(Stock.class))).thenReturn(stock);

		Stock savedStock = stockService.addStock(stock);


		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date dateCreation = dateFormat.parse("30/09/2000");
		Date dateModification1 = dateFormat.parse("01/10/2000");
		Date dateModification2 = dateFormat.parse("20/09/2000");

		// Créer les produits
		Produit produit1 = new Produit(1L, "code 1", "Produit 1", 200.0f, dateCreation, dateModification1, savedStock, null, null);
		Produit produit2 = new Produit(2L, "code 2", "Produit 2", 200.0f, dateCreation, dateModification2, savedStock, null, null);

		when(produitRepository.save(produit1)).thenReturn(produit1);
		when(produitRepository.save(produit2)).thenReturn(produit2);

		Produit savedProduit1 = produitService.addProduit(produit1);
		Produit savedProduit2 = produitService.addProduit(produit2);

		List<Produit> listproduits = new ArrayList<>(Arrays.asList(produit1, produit2));
		// Simuler la récupération des produits par stock
		when(produitRepository.findByStock(savedStock)).thenReturn(listproduits);


		List<Produit> produits = produitService.findByStock(savedStock);

		// Assert
		assertEquals(2, produits.size());
		assertTrue(produits.contains(savedProduit1));
		assertTrue(produits.contains(savedProduit2));



		assertFalse(produitService.verifierDates(savedProduit2));

		// Vérifier les interactions avec les repository
		verify(produitRepository, times(2)).save(any(Produit.class));
		verify(stockRepository).save(any(Stock.class));
		verify(produitRepository, times(1)).findByStock(savedStock);
	}


	//@Test
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
	 void testDeleteProduit() throws ParseException {

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

	//@Test
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




}