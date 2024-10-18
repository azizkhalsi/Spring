// src/main/java/com/projet/tpachatproject/services/FournisseurServiceImpl.java
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
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class FournisseurServiceImpl implements IFournisseurService {

	@Autowired
	private FournisseurRepository fournisseurRepository;

	@Autowired
	private DetailFournisseurRepository detailFournisseurRepository;

	@Autowired
	private ProduitRepository produitRepository;

	@Autowired
	private SecteurActiviteRepository secteurActiviteRepository;

	@Override
	public List<Fournisseur> retrieveAllFournisseurs() {
		List<Fournisseur> fournisseurs = (List<Fournisseur>) fournisseurRepository.findAll();
		for (Fournisseur fournisseur : fournisseurs) {
			log.info("Fournisseur: {}", fournisseur);
		}
		return fournisseurs;
	}

	/**
	 * Creates a Fournisseur with associated DetailFournisseur and assigns multiple SecteurActivites.
	 *
	 * @param fournisseur          The Fournisseur entity to create.
	 * @param detailFournisseur    The DetailFournisseur entity to associate.
	 * @param secteurActiviteIds   List of SecteurActivite IDs to assign.
	 * @return The saved Fournisseur entity with associations.
	 */
	public Fournisseur createFournisseurWithDetailsAndSecteur(Fournisseur fournisseur, DetailFournisseur detailFournisseur, List<Long> secteurActiviteIds) {
		// Set the detail fournisseur
		fournisseur.setDetailFournisseur(detailFournisseur);

		// Initialize secteurActivites if null
		if (fournisseur.getSecteurActivites() == null) {
			fournisseur.setSecteurActivites(new HashSet<>());
		}

		// Save the fournisseur to get its ID
		Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);

		// Assign secteur activities
		for (Long idSecteurActivite : secteurActiviteIds) {
			SecteurActivite secteurActivite = secteurActiviteRepository.findById(idSecteurActivite)
					.orElseThrow(() -> new RuntimeException("SecteurActivite not found with ID: " + idSecteurActivite));
			savedFournisseur.getSecteurActivites().add(secteurActivite);
		}

		// Save the fournisseur again to persist the associations
		fournisseurRepository.save(savedFournisseur);

		return savedFournisseur;
	}

	/**
	 * Adds a new Fournisseur with a default DetailFournisseur.
	 *
	 * @param f The Fournisseur entity to add.
	 * @return The saved Fournisseur entity.
	 */
	public Fournisseur addFournisseur(Fournisseur f /*Master*/) {
		DetailFournisseur df = new DetailFournisseur(); // Slave
		df.setDateDebutCollaboration(new Date()); // util
		// Assign the "Slave" to the "Master"
		f.setDetailFournisseur(df);
		// Initialize secteurActivites if null
		if (f.getSecteurActivites() == null) {
			f.setSecteurActivites(new HashSet<>());
		}
		fournisseurRepository.save(f);
		return f;
	}

	/**
	 * Saves the DetailFournisseur associated with a Fournisseur.
	 *
	 * @param f The Fournisseur entity containing the DetailFournisseur.
	 * @return The saved DetailFournisseur entity.
	 */
	private DetailFournisseur saveDetailFournisseur(Fournisseur f) {
		DetailFournisseur df = f.getDetailFournisseur();
		detailFournisseurRepository.save(df);
		return df;
	}

	/**
	 * Updates an existing Fournisseur along with its DetailFournisseur.
	 *
	 * @param f The Fournisseur entity to update.
	 * @return The updated Fournisseur entity.
	 */
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

	/**
	 * Assigns a SecteurActivite to a Fournisseur.
	 *
	 * @param idSecteurActivite The ID of the SecteurActivite to assign.
	 * @param idFournisseur     The ID of the Fournisseur to whom the SecteurActivite is assigned.
	 */
	@Override
	public void assignSecteurActiviteToFournisseur(Long idSecteurActivite, Long idFournisseur) {
		log.info("Assigning SecteurActivite ID {} to Fournisseur ID {}", idSecteurActivite, idFournisseur);

		Fournisseur fournisseur = fournisseurRepository.findById(idFournisseur)
				.orElseThrow(() -> new RuntimeException("Fournisseur not found"));

		SecteurActivite secteurActivite = secteurActiviteRepository.findById(idSecteurActivite)
				.orElseThrow(() -> new RuntimeException("SecteurActivite not found"));

		// Initialize the set if it's null to prevent NullPointerException
		if (fournisseur.getSecteurActivites() == null) {
			fournisseur.setSecteurActivites(new HashSet<>());
		}

		boolean added = fournisseur.getSecteurActivites().add(secteurActivite);
		if (added) {
			log.info("SecteurActivite ID {} added to Fournisseur ID {}", idSecteurActivite, idFournisseur);
		} else {
			log.info("SecteurActivite ID {} was already assigned to Fournisseur ID {}", idSecteurActivite, idFournisseur);
		}

		fournisseurRepository.save(fournisseur);
	}
}
