package com.projet.tpachatproject.services;


import com.projet.tpachatproject.entities.Operateur;
import com.projet.tpachatproject.repositories.OperateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OperateurServiceImpl implements IOperateurService {

	private final OperateurRepository operateurRepository;

	@Override
	public List<Operateur> retrieveAllOperateurs() {
		return (List<Operateur>) operateurRepository.findAll();
	}

	@Override
	public Operateur addOperateur(Operateur o) {
		operateurRepository.save(o);
		return o;
	}

	@Override
	public void deleteOperateur(Long id) {
		operateurRepository.deleteById(id);
		
	}

	@Override
	public Operateur updateOperateur(Operateur o1) {
		operateurRepository.save(o1);
		return o1;
	}

	@Override
	public Operateur retrieveOperateur(Long id) {
		return operateurRepository.findById(id).orElse(null);

	}

}
