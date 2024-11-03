package com.projet.tpachatproject.services;


import com.projet.tpachatproject.entities.CategorieProduit;
import com.projet.tpachatproject.repositories.CategorieProduitRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategorieProduitServiceImpl implements ICategorieProduitService {


	CategorieProduitRepository categorieProduitRepository;
	@Override
	public List<CategorieProduit> retrieveAllCategorieProduits() {
		
		return categorieProduitRepository.findAll();
	}

	@Override
	public CategorieProduit addCategorieProduit(CategorieProduit cp) {
		categorieProduitRepository.save(cp);
		return cp;
	}

	@Override
	public void deleteCategorieProduit(Long id) {
		categorieProduitRepository.deleteById(id);
		
	}

	@Override
	public CategorieProduit updateCategorieProduit(CategorieProduit cp) {
		categorieProduitRepository.save(cp);
		return cp;
	}

	@Override
	public CategorieProduit retrieveCategorieProduit(Long id) {
		CategorieProduit categorieProduit = categorieProduitRepository.findById(id).orElse(null);
		return categorieProduit;
	}

}
