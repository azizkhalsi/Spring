package com.projet.tpachatproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fournisseur implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idFournisseur;
	private String code;
	private String libelle;

	@Enumerated(EnumType.STRING)
	private CategorieFournisseur  categorieFournisseur;

	@OneToMany(mappedBy="fournisseur")
	@JsonIgnore
	private Set<Facture> factures;

    @ManyToMany
    @JsonIgnore

    private Set<SecteurActivite> secteurActivites= new HashSet<>();
  
    @OneToOne(cascade= CascadeType.ALL,fetch=FetchType.EAGER)
    private DetailFournisseur detailFournisseur;
    

	
}
