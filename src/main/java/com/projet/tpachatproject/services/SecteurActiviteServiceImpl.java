package com.projet.tpachatproject.services;


import com.projet.tpachatproject.entities.SecteurActivite;
import com.projet.tpachatproject.repositories.SecteurActiviteRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SecteurActiviteServiceImpl implements ISecteurActiviteService{


	SecteurActiviteRepository secteurActiviteRepository;
	@Override
	public List<SecteurActivite> retrieveAllSecteurActivite() {
		return (List<SecteurActivite>) secteurActiviteRepository.findAll();
	}

	@Override
	public SecteurActivite addSecteurActivite(SecteurActivite sa) {
		secteurActiviteRepository.save(sa);
		return sa;
	}

	@Override
	public void deleteSecteurActivite(Long id) {
		secteurActiviteRepository.deleteById(id);
		
	}

	@Override
	public SecteurActivite updateSecteurActivite(SecteurActivite saa) {
		secteurActiviteRepository.save(saa);
		return saa;
	}

	@Override
	public SecteurActivite retrieveSecteurActivite(Long id) {
			return secteurActiviteRepository.findById(id).orElse(null);

	}

}
