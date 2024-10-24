package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.*;
import com.projet.tpachatproject.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@Transactional
public class FactureServiceImpl implements IFactureService {

	private final FactureRepository factureRepository;
	private final OperateurRepository operateurRepository;
	private final FournisseurRepository fournisseurRepository;

	private final ReglementServiceImpl reglementService;

	@Autowired
	public FactureServiceImpl(FactureRepository factureRepository,
							OperateurRepository operateurRepository,
							FournisseurRepository fournisseurRepository,
							ReglementServiceImpl reglementService) {
		this.factureRepository = factureRepository;
		this.operateurRepository = operateurRepository;
		this.fournisseurRepository = fournisseurRepository;

		this.reglementService = reglementService;
	}



	@Override
	public List<Facture> retrieveAllFactures() {
		List<Facture> factures = factureRepository.findAll();
		for (Facture facture : factures) {
			log.info(" facture : " + facture);
		}
		return factures;
	}

	
	public Facture addFacture(Facture f) {
		return factureRepository.save(f);
	}




	@Override
	public void cancelFacture(Long factureId) {

		Facture facture = factureRepository.findById(factureId).orElse(new Facture());
		facture.setArchivee(true);
		factureRepository.save(facture);

		factureRepository.updateFacture(factureId);
	}

	@Override
	public Facture retrieveFacture(Long factureId) {

		Facture facture = factureRepository.findById(factureId).orElse(null);
		log.info("facture :" + facture);
		return facture;
	}

	@Override
	public List<Facture> getFacturesByFournisseur(Long idFournisseur) {
		Fournisseur fournisseur = fournisseurRepository.findById(idFournisseur).orElse(null);

		if (fournisseur == null) {

			return Collections.emptyList();


		}

		return (List<Facture>) fournisseur.getFactures();
	}

	@Override
	public void assignOperateurToFacture(Long idOperateur, Long idFacture) {

		Facture facture = factureRepository.findById(idFacture).orElse(null);

		if (facture == null) {
			throw new EntityNotFoundException("Facture with ID " + idFacture + " not found");

		}


		Operateur operateur = operateurRepository.findById(idOperateur).orElse(null);

		if (operateur == null) {
			throw new EntityNotFoundException("Operateur with ID " + idOperateur + " not found");

		}


		operateur.getFactures().add(facture);


		operateurRepository.save(operateur);
	}

	@Override
	public float pourcentageRecouvrement(Date startDate, Date endDate) {
		float totalFacturesEntreDeuxDates = factureRepository.getTotalFacturesEntreDeuxDates(startDate,endDate);
		float totalRecouvrementEntreDeuxDates =reglementService.getChiffreAffaireEntreDeuxDate(startDate,endDate);
		return ((totalRecouvrementEntreDeuxDates/totalFacturesEntreDeuxDates)*100);

	}

	public Facture getFactureById(Long factureId) {
		return factureRepository.findById(factureId).orElse(null);
	}



	public void deleteFacture(Long factureId) {
		factureRepository.deleteById(factureId);
	}



	public Facture applySpecialDiscounts(Facture facture) {
		if (facture == null) {
			throw new IllegalArgumentException("Facture cannot be null");
		}

		float currentDiscount = facture.getMontantRemise();
		float additionalDiscount = 0;


		if (facture.getMontantFacture() > 1000) {
			additionalDiscount += facture.getMontantFacture() * 0.05f;
		}


		if (facture.getDetailsFacture().size() > 5) {
			additionalDiscount += facture.getMontantFacture() * 0.02f;
		}


		Calendar cal = Calendar.getInstance();
		cal.setTime(facture.getDateCreationFacture());
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
				cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			additionalDiscount += facture.getMontantFacture() * 0.03f;
		}


		boolean hasPremiumProduct = facture.getDetailsFacture().stream()
				.anyMatch(detail -> detail.getProduit().getCategorieProduit().getLibelleCategorie().equals("PREMIUM"));

		if (hasPremiumProduct) {
			additionalDiscount -= facture.getMontantFacture() * 0.01f;
		}




		float totalDiscount = currentDiscount + additionalDiscount;
		facture.setMontantRemise(totalDiscount);
		facture.setMontantFacture(facture.getMontantFacture() - additionalDiscount);

		return factureRepository.save(facture);
	}











}