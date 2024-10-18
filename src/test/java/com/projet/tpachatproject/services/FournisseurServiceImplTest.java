// src/test/java/com/projet/tpachatproject/services/FournisseurServiceImplTest.java
package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.CategorieFournisseur;
import com.projet.tpachatproject.entities.Fournisseur;
import com.projet.tpachatproject.entities.SecteurActivite;
import com.projet.tpachatproject.repositories.FournisseurRepository;
import com.projet.tpachatproject.repositories.SecteurActiviteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FournisseurServiceImplTest {

    @Mock
    private FournisseurRepository fournisseurRepository;

    @Mock
    private SecteurActiviteRepository secteurActiviteRepository;

    @InjectMocks
    private FournisseurServiceImpl fournisseurService;

    private Fournisseur fournisseur;
    private SecteurActivite secteurActivite;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Fournisseur
        fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(1L);
        fournisseur.setCode("F001");
        fournisseur.setLibelle("Fournisseur Test");
        fournisseur.setCategorieFournisseur(CategorieFournisseur.ORDINAIRE);
        fournisseur.setSecteurActivites(new HashSet<>());

        // Initialize SecteurActivite
        secteurActivite = new SecteurActivite();
        secteurActivite.setIdSecteurActivite(1L);
        secteurActivite.setCodeSecteurActivite("SA001");
        secteurActivite.setLibelleSecteurActivite("Secteur Test");
    }

    @Test
    public void testAssignSecteurActiviteToFournisseur_ComplexScenario() {
        // Arrange
        Long fournisseurId = 1L;
        Long secteurActiviteId = 1L;
        Long invalidSecteurActiviteId = 999L;
        Long invalidFournisseurId = 888L;

        System.out.println("=== Starting testAssignSecteurActiviteToFournisseur_ComplexScenario ===");

        // Mocking repository responses
        when(fournisseurRepository.findById(fournisseurId)).thenReturn(Optional.of(fournisseur));
        when(secteurActiviteRepository.findById(secteurActiviteId)).thenReturn(Optional.of(secteurActivite));
        when(secteurActiviteRepository.findById(invalidSecteurActiviteId)).thenReturn(Optional.empty());
        when(fournisseurRepository.findById(invalidFournisseurId)).thenReturn(Optional.empty());

        // Using ArgumentCaptor to capture the Fournisseur passed to save
        ArgumentCaptor<Fournisseur> fournisseurCaptor = ArgumentCaptor.forClass(Fournisseur.class);
        when(fournisseurRepository.save(any(Fournisseur.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });

        System.out.println("Mocks set up: Fournisseur and SecteurActivite found, invalid IDs prepared");

        // Act & Assert

        // Step 1: Assign SecteurActivite to Fournisseur
        System.out.println("\n--- Step 1: Assign SecteurActivite to Fournisseur ---");
        fournisseurService.assignSecteurActiviteToFournisseur(secteurActiviteId, fournisseurId);
        System.out.println("Assigned SecteurActivite with ID " + secteurActiviteId + " to Fournisseur with ID " + fournisseurId);

        // Verify interactions for Step 1
        verify(fournisseurRepository, times(1)).findById(fournisseurId);
        verify(secteurActiviteRepository, times(1)).findById(secteurActiviteId);
        verify(fournisseurRepository, times(1)).save(fournisseurCaptor.capture());

        // Capture the Fournisseur object after assignment
        Fournisseur capturedFournisseur = fournisseurCaptor.getValue();
        assertNotNull(capturedFournisseur, "Captured Fournisseur should not be null");
        assertTrue(capturedFournisseur.getSecteurActivites().contains(secteurActivite), "SecteurActivite should be assigned to Fournisseur");
        System.out.println("Assertion passed: SecteurActivite is assigned to Fournisseur");

        // Step 2: Attempt to reassign the same SecteurActivite
        System.out.println("\n--- Step 2: Attempt to Reassign the Same SecteurActivite ---");
        fournisseurService.assignSecteurActiviteToFournisseur(secteurActiviteId, fournisseurId);
        System.out.println("Attempted to assign SecteurActivite with ID " + secteurActiviteId + " again to Fournisseur with ID " + fournisseurId);

        // Verify interactions for Step 2
        verify(fournisseurRepository, times(2)).findById(fournisseurId);
        verify(secteurActiviteRepository, times(2)).findById(secteurActiviteId);
        verify(fournisseurRepository, times(2)).save(any(Fournisseur.class));

        // Verify no duplication
        assertEquals(1, capturedFournisseur.getSecteurActivites().size(), "SecteurActivite should not be duplicated");
        System.out.println("Assertion passed: SecteurActivite was not duplicated");

        // Step 3: Attempt to assign a non-existent SecteurActivite
        System.out.println("\n--- Step 3: Attempt to Assign Non-existent SecteurActivite ---");
        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> {
            fournisseurService.assignSecteurActiviteToFournisseur(invalidSecteurActiviteId, fournisseurId);
        }, "Expected RuntimeException for non-existent SecteurActivite");

        assertEquals("SecteurActivite not found", exception1.getMessage(), "Exception message should indicate SecteurActivite not found");
        System.out.println("Caught expected exception: " + exception1.getMessage());

        // Verify interactions for Step 3
        verify(fournisseurRepository, times(3)).findById(fournisseurId);
        verify(secteurActiviteRepository, times(2)).findById(secteurActiviteId);
        verify(secteurActiviteRepository, times(1)).findById(invalidSecteurActiviteId);
        verify(fournisseurRepository, times(2)).save(any(Fournisseur.class)); // No save on exception

        // Step 4: Attempt to assign SecteurActivite to a non-existent Fournisseur
        System.out.println("\n--- Step 4: Attempt to Assign SecteurActivite to Non-existent Fournisseur ---");
        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> {
            fournisseurService.assignSecteurActiviteToFournisseur(secteurActiviteId, invalidFournisseurId);
        }, "Expected RuntimeException for non-existent Fournisseur");

        assertEquals("Fournisseur not found", exception2.getMessage(), "Exception message should indicate Fournisseur not found");
        System.out.println("Caught expected exception: " + exception2.getMessage());
    }
}
