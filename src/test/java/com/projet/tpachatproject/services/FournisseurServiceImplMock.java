package com.projet.tpachatproject.services;


import com.projet.tpachatproject.entities.*;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.projet.tpachatproject.repositories.FournisseurRepository;
import com.projet.tpachatproject.services.FournisseurServiceImplMock;
import java.util.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@AllArgsConstructor
public class FournisseurServiceImplMock {
    @Mock
    FournisseurRepository fournisseurRepository;

    @InjectMocks
    FournisseurServiceImpl fournisseurService;

    Set<Facture> factures = new HashSet<Facture>();
    Set<SecteurActivite> secteurActivites = new HashSet<SecteurActivite>();
    DetailFournisseur detailFournisseurs = new DetailFournisseur();


    Fournisseur fournisseur = new Fournisseur((long)1,"code","libelle", CategorieFournisseur.CONVENTIONNE,factures,secteurActivites,detailFournisseurs);


    List<Fournisseur> listFournisseurs = new ArrayList<Fournisseur>() {
        {
            add(new Fournisseur((long)2,"code2","libelle2", CategorieFournisseur.CONVENTIONNE,factures,secteurActivites,detailFournisseurs));
            add(new Fournisseur((long)3,"code3","libelle3", CategorieFournisseur.CONVENTIONNE,factures,secteurActivites,detailFournisseurs));
        }
    };


    @Test
    public void testRetrieveBloc() {
        Mockito.when(fournisseurRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(fournisseur));
        Fournisseur fournisseur1 = fournisseurRepository.getReferenceById((long)1);
        Assertions.assertNotNull(fournisseur1);
    }

}
