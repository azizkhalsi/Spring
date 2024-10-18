// src/main/java/com/projet/tpachatproject/services/FactureServiceImpl.java
package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.*;
import com.projet.tpachatproject.repositories.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class FactureServiceImpl implements IFactureService {

	@Autowired
	FactureRepository factureRepository;

	@Autowired
	OperateurRepository operateurRepository;

	@Autowired
	DetailFactureRepository detailFactureRepository;

	@Autowired
	FournisseurRepository fournisseurRepository;

	@Autowired
	ProduitRepository produitRepository;

	@Autowired
	ReglementServiceImpl reglementService;

	@Override
	public List<Facture> retrieveAllFactures() {
		log.info("Retrieving all Factures");
		List<Facture> factures = factureRepository.findAll();
		for (Facture facture : factures) {
			log.info("Facture Retrieved: {}", facture);
		}
		return factures;
	}

	@Override
	public Facture addFacture(Facture f) {
		log.info("Adding Facture: {}", f);
		// Perform calculations before saving
		f = addDetailsFacture(f, f.getDetailsFacture());
		Facture savedFacture = factureRepository.save(f);
		log.info("Facture Saved: {}", savedFacture);
		return savedFacture;
	}

	/**
	 * Calculates and updates the montantFacture and montantRemise for a Facture based on its DetailFactures.
	 *
	 * @param f                The Facture entity to update.
	 * @param detailsFacture   The set of DetailFacture associated with the Facture.
	 * @return The updated Facture entity with calculated amounts.
	 */
	private Facture addDetailsFacture(Facture f, Set<DetailFacture> detailsFacture) {
		log.info("Calculating details for Facture ID: {}", f.getIdFacture());
		float montantFacture = 0;
		float montantRemise = 0;
		for (DetailFacture detail : detailsFacture) {
			log.info("Processing DetailFacture ID: {}", detail.getIdDetailFacture());
			// Retrieve the produit
			Long produitId = detail.getProduit().getIdProduit();
			log.info("Retrieving Produit with ID: {}", produitId);
			Produit produit = produitRepository.findById(produitId)
					.orElseThrow(() -> {
						log.error("Produit not found with ID: {}", produitId);
						return new RuntimeException("Produit not found with ID: " + produitId);
					});
			log.info("Produit Retrieved: {}", produit);

			// Calculate prixTotalDetail
			float prixTotalDetail = detail.getQteCommandee() * produit.getPrix();
			log.info("Calculating prixTotalDetail: QteCommandee={} * Prix={} = {}", detail.getQteCommandee(), produit.getPrix(), prixTotalDetail);

			// Calculate montantRemiseDetail
			float montantRemiseDetail = (prixTotalDetail * detail.getPourcentageRemise()) / 100;
			log.info("Calculating montantRemiseDetail: prixTotalDetail={} * PourcentageRemise={}% = {}", prixTotalDetail, detail.getPourcentageRemise(), montantRemiseDetail);

			// Calculate prixTotalDetailRemise
			float prixTotalDetailRemise = prixTotalDetail - montantRemiseDetail;
			log.info("Calculating prixTotalDetailRemise: prixTotalDetail={} - montantRemiseDetail={} = {}", prixTotalDetail, montantRemiseDetail, prixTotalDetailRemise);

			// Set calculated values
			detail.setMontantRemise(montantRemiseDetail);
			detail.setPrixTotalDetail(prixTotalDetailRemise);
			log.info("Updated DetailFacture: {}", detail);

			// Update totals
			montantFacture += prixTotalDetailRemise;
			montantRemise += montantRemiseDetail;
			log.info("Accumulated montantFacture: {}", montantFacture);
			log.info("Accumulated montantRemise: {}", montantRemise);

			// Save the detail
			detailFactureRepository.save(detail);
			log.info("DetailFacture Saved: {}", detail);
		}
		// Set totals in Facture
		f.setMontantFacture(montantFacture);
		f.setMontantRemise(montantRemise);
		log.info("Set montantFacture={} and montantRemise={} for Facture ID: {}", montantFacture, montantRemise, f.getIdFacture());
		return f;
	}

	@Override
	public void cancelFacture(Long factureId) {
		log.info("Cancelling Facture with ID: {}", factureId);
		// Retrieve the Facture or throw exception
		Facture facture = factureRepository.findById(factureId)
				.orElseThrow(() -> {
					log.error("Facture not found with ID: {}", factureId);
					return new RuntimeException("Facture not found");
				});
		facture.setArchivee(true);
		factureRepository.save(facture);
		log.info("Facture ID: {} has been archived", factureId);
		// Optionally, if you have a JPQL method to update, uncomment the following line
		// factureRepository.updateFacture(factureId);
	}

	@Override
	public Facture retrieveFacture(Long factureId) {
		log.info("Retrieving Facture with ID: {}", factureId);
		Facture facture = factureRepository.findById(factureId).orElse(null);
		log.info("Retrieved Facture: {}", facture);
		return facture;
	}

	@Override
	public List<Facture> getFacturesByFournisseur(Long idFournisseur) {
		log.info("Retrieving Factures for Fournisseur ID: {}", idFournisseur);
		Fournisseur fournisseur = fournisseurRepository.findById(idFournisseur)
				.orElseThrow(() -> {
					log.error("Fournisseur not found with ID: {}", idFournisseur);
					return new RuntimeException("Fournisseur not found with ID: " + idFournisseur);
				});
		List<Facture> factures = factureRepository.findByFournisseur(fournisseur);
		log.info("Retrieved {} Factures for Fournisseur ID: {}", factures.size(), idFournisseur);
		return factures;
	}

	@Override
	public void assignOperateurToFacture(Long idOperateur, Long idFacture) {
		log.info("Assigning Operateur ID: {} to Facture ID: {}", idOperateur, idFacture);
		Facture facture = factureRepository.findById(idFacture)
				.orElseThrow(() -> {
					log.error("Facture not found with ID: {}", idFacture);
					return new RuntimeException("Facture not found with ID: " + idFacture);
				});
		Operateur operateur = operateurRepository.findById(idOperateur)
				.orElseThrow(() -> {
					log.error("Operateur not found with ID: {}", idOperateur);
					return new RuntimeException("Operateur not found with ID: " + idOperateur);
				});
		operateur.getFactures().add(facture);
		operateurRepository.save(operateur);
		log.info("Operateur ID: {} has been assigned to Facture ID: {}", idOperateur, idFacture);
	}

	@Override
	public float pourcentageRecouvrement(Date startDate, Date endDate) {
		log.info("Calculating pourcentageRecouvrement from {} to {}", startDate, endDate);
		float totalFacturesEntreDeuxDates = factureRepository.getTotalFacturesEntreDeuxDates(startDate, endDate);
		log.info("Total Factures between dates: {}", totalFacturesEntreDeuxDates);
		float totalRecouvrementEntreDeuxDates = reglementService.getChiffreAffaireEntreDeuxDate(startDate, endDate);
		log.info("Total Recouvrement between dates: {}", totalRecouvrementEntreDeuxDates);
		if (totalFacturesEntreDeuxDates == 0) {
			log.warn("Total Factures is zero. Returning pourcentageRecouvrement as 0.");
			return 0;
		}
		float pourcentage = (totalRecouvrementEntreDeuxDates / totalFacturesEntreDeuxDates) * 100;
		log.info("Calculated pourcentageRecouvrement: {}%", pourcentage);
		return pourcentage;
	}
}
