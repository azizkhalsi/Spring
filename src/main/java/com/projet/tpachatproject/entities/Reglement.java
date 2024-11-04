package com.projet.tpachatproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reglement implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idReglement;
	private float montantPaye;
	private float montantRestant;
	private Boolean payee;
	@Temporal(TemporalType.DATE)
	private Date dateReglement;

	@ManyToOne
	@JsonIgnore
	private Facture facture;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Reglement reglement)) return false;
        return Objects.equals(getIdReglement(), reglement.getIdReglement());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getIdReglement());
	}
}
