package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.DetailFournisseur;
import com.projet.tpachatproject.entities.Fournisseur;
import com.projet.tpachatproject.entities.SecteurActivite;
import com.projet.tpachatproject.repositories.DetailFournisseurRepository;
import com.projet.tpachatproject.repositories.FournisseurRepository;
import com.projet.tpachatproject.repositories.SecteurActiviteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

 class FournisseurServiceImplJUnitTest {

    @InjectMocks
    private FournisseurServiceImpl fournisseurService;

    @Mock
    private FournisseurRepository fournisseurRepository;

    @Mock
    private DetailFournisseurRepository detailFournisseurRepository;

    @Mock
    private SecteurActiviteRepository secteurActiviteRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println("[INFO] - Mocks initialized successfully.");
    }

    @Test
     void testAddFournisseur() {
        System.out.println("\n[TEST START] - testAddFournisseur");

        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setLibelle("Test Fournisseur");

        DetailFournisseur detailFournisseur = new DetailFournisseur();
        detailFournisseur.setEmail("test@example.com");
        fournisseur.setDetailFournisseur(detailFournisseur);

        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(fournisseur);

        Fournisseur savedFournisseur = fournisseurService.addFournisseur(fournisseur);

        System.out.println("[INFO] - Fournisseur created: " + savedFournisseur.getLibelle());
        System.out.println("[INFO] - DetailFournisseur email: " + savedFournisseur.getDetailFournisseur().getEmail());

        assertNotNull(savedFournisseur);
        assertNotNull(savedFournisseur.getDetailFournisseur());
        assertEquals("test@example.com", savedFournisseur.getDetailFournisseur().getEmail());

        verify(fournisseurRepository).save(fournisseur);
        verify(detailFournisseurRepository).save(detailFournisseur);

        System.out.println("[TEST END] - testAddFournisseur\n");
    }

    @Test
     void testAssignSecteurActiviteToFournisseur() {
        System.out.println("\n[TEST START] - testAssignSecteurActiviteToFournisseur");

        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(1L);
        fournisseur.setLibelle("Fournisseur with Secteur");
        fournisseur.setSecteurActivites(new HashSet<>());

        SecteurActivite secteurActivite = new SecteurActivite();
        secteurActivite.setIdSecteurActivite(100L);
        secteurActivite.setLibelleSecteurActivite("Tech");

        when(fournisseurRepository.findById(anyLong())).thenReturn(Optional.of(fournisseur));
        when(secteurActiviteRepository.findById(anyLong())).thenReturn(Optional.of(secteurActivite));

        fournisseurService.assignSecteurActiviteToFournisseur(secteurActivite.getIdSecteurActivite(), fournisseur.getIdFournisseur());

        System.out.println("[INFO] - SecteurActivite " + secteurActivite.getLibelleSecteurActivite() + " assigned to Fournisseur " + fournisseur.getLibelle());

        verify(fournisseurRepository).save(fournisseur);
        assertTrue(fournisseur.getSecteurActivites().contains(secteurActivite));

        System.out.println("[TEST END] - testAssignSecteurActiviteToFournisseur\n");
    }






    @Test
     void testAssignSecteurTwiceToFournisseur() {
        System.out.println("\n[TEST START] - testAssignSecteurTwiceToFournisseur");

        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(1L);
        fournisseur.setSecteurActivites(new HashSet<>());

        SecteurActivite secteurActivite = new SecteurActivite();
        secteurActivite.setIdSecteurActivite(100L);

        when(fournisseurRepository.findById(anyLong())).thenReturn(Optional.of(fournisseur));
        when(secteurActiviteRepository.findById(anyLong())).thenReturn(Optional.of(secteurActivite));

        // Act: First assignment
        fournisseurService.assignSecteurActiviteToFournisseur(secteurActivite.getIdSecteurActivite(), fournisseur.getIdFournisseur());
        System.out.println("[INFO] - First assignment performed successfully");

        // Act: Assign the same secteur again
        fournisseurService.assignSecteurActiviteToFournisseur(secteurActivite.getIdSecteurActivite(), fournisseur.getIdFournisseur());

        assertEquals(1, fournisseur.getSecteurActivites().size());

        System.out.println("[INFO] - SecteurActivite should not be duplicated. Total secteurs: " + fournisseur.getSecteurActivites().size());

        System.out.println("[TEST END] - testAssignSecteurTwiceToFournisseur\n");
    }
}
