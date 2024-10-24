package com.projet.tpachatproject.services;


import com.projet.tpachatproject.entities.DetailFournisseur;
import com.projet.tpachatproject.entities.Fournisseur;
import com.projet.tpachatproject.entities.SecteurActivite;
import com.projet.tpachatproject.repositories.DetailFournisseurRepository;
import com.projet.tpachatproject.repositories.FournisseurRepository;
import com.projet.tpachatproject.repositories.ProduitRepository;
import com.projet.tpachatproject.repositories.SecteurActiviteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FournisseurServiceImpl implements IFournisseurService {

	private final FournisseurRepository fournisseurRepository;
	private final DetailFournisseurRepository detailFournisseurRepository;
	private final SecteurActiviteRepository secteurActiviteRepository;

	// Constructor Injection
	@Autowired
	public FournisseurServiceImpl(FournisseurRepository fournisseurRepository,
							DetailFournisseurRepository detailFournisseurRepository,
							SecteurActiviteRepository secteurActiviteRepository) {
		this.fournisseurRepository = fournisseurRepository;
		this.detailFournisseurRepository = detailFournisseurRepository;
		this.secteurActiviteRepository = secteurActiviteRepository;
	}

	@Override
	public List<Fournisseur> retrieveAllFournisseurs() {
		List<Fournisseur> fournisseurs = fournisseurRepository.findAll();
		for (Fournisseur fournisseur : fournisseurs) {
			log.info(" fournisseur : " + fournisseur);
		}
		return fournisseurs;
	}


	public Fournisseur addFournisseur(Fournisseur f) {
		// Save the DetailFournisseur first
		DetailFournisseur df = f.getDetailFournisseur();
		if(df != null) {
			detailFournisseurRepository.save(df);
		}

		// Save the Fournisseur
		return fournisseurRepository.save(f);
	}
	
	private DetailFournisseur saveDetailFournisseur(Fournisseur f){
		DetailFournisseur df = f.getDetailFournisseur();
		detailFournisseurRepository.save(df);
		return df;
	}

	public Fournisseur updateFournisseur(Fournisseur f) {
		DetailFournisseur df = saveDetailFournisseur(f);
		f.setDetailFournisseur(df);	
		fournisseurRepository.save(f);
		return f;
	}

	@Override
	public void deleteFournisseur(Long fournisseurId) {
		fournisseurRepository.deleteById(fournisseurId);

	}

	@Override
	public Fournisseur retrieveFournisseur(Long fournisseurId) {

		return fournisseurRepository.findById(fournisseurId).orElse(null);

	}


	@Override
	public void assignSecteurActiviteToFournisseur(Long idSecteurActivite, Long idFournisseur) {
		Fournisseur fournisseur = fournisseurRepository.findById(idFournisseur)
				.orElseThrow(() -> new RuntimeException("Fournisseur with id " + idFournisseur + " not found"));

		SecteurActivite secteurActivite = secteurActiviteRepository.findById(idSecteurActivite)
				.orElseThrow(() -> new RuntimeException("SecteurActivite with id " + idSecteurActivite + " not found"));

		fournisseur.getSecteurActivites().add(secteurActivite);
		fournisseurRepository.save(fournisseur);
	}




}