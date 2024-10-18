// src/test/java/com/projet/tpachatproject/services/FactureServiceImplTest.java
package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.*;
import com.projet.tpachatproject.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FactureServiceImplTest {

    @Mock
    private FactureRepository factureRepository;

    @Mock
    private OperateurRepository operateurRepository;

    @Mock
    private DetailFactureRepository detailFactureRepository;

    @Mock
    private FournisseurRepository fournisseurRepository;

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private ReglementServiceImpl reglementService;

    @InjectMocks
    private FactureServiceImpl factureService;

    private Produit produit;
    private Fournisseur fournisseur;
    private Facture facture;
    private DetailFacture detail;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Produit
        produit = new Produit();
        produit.setIdProduit(1L);
        produit.setNom("Produit Unique");
        produit.setPrix(150.0f);

        // Initialize Fournisseur
        fournisseur = new Fournisseur();
        fournisseur.setIdFournisseur(1L);
        fournisseur.setCode("F001");
        fournisseur.setLibelle("Fournisseur Unique");
        fournisseur.setCategorieFournisseur(CategorieFournisseur.ORDINAIRE);
        fournisseur.setSecteurActivites(new HashSet<>());

        // Initialize Facture
        facture = new Facture();
        facture.setIdFacture(1L);
        facture.setMontantFacture(0.0f);
        facture.setMontantRemise(0.0f);
        facture.setDateCreationFacture(new Date());
        facture.setDateDerniereModificationFacture(new Date());
        facture.setArchivee(false);
        facture.setDetailsFacture(new LinkedHashSet<>()); // Preserve order
        facture.setFournisseur(fournisseur);
        facture.setReglements(new HashSet<>());

        // Initialize DetailFacture
        detail = new DetailFacture();
        detail.setIdDetailFacture(1L);
        detail.setProduit(produit);
        detail.setQteCommandee(3); // Quantity ordered
        detail.setPourcentageRemise(15); // 15% discount
        detail.setFacture(facture);

        // Assign detail to facture
        facture.getDetailsFacture().add(detail);
    }

    /**
     * Test the addFacture method to ensure that montantFacture and montantRemise are correctly calculated.
     */
    @Test
    public void testAddFacture_CalculationsAreCorrect() {
        // Arrange
        when(produitRepository.findById(produit.getIdProduit())).thenReturn(Optional.of(produit));

        // Mock saving of DetailFacture
        when(detailFactureRepository.save(any(DetailFacture.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock saving of Facture
        when(factureRepository.save(any(Facture.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Facture savedFacture = factureService.addFacture(facture);

        // Assert
        // Verify produitRepository.findById called once
        verify(produitRepository, times(1)).findById(produit.getIdProduit());

        // Verify detailFactureRepository.save called once
        verify(detailFactureRepository, times(1)).save(any(DetailFacture.class));

        // Verify factureRepository.save called once
        verify(factureRepository, times(1)).save(any(Facture.class));

        // Calculate expected values
        // Detail: 3 * 150 = 450 - 15% = 382.5
        // Total montantFacture = 382.5
        // Total montantRemise = 67.5

        assertEquals(382.5f, savedFacture.getMontantFacture(), 0.001, "Montant Facture should be correctly calculated");
        assertEquals(67.5f, savedFacture.getMontantRemise(), 0.001, "Montant Remise should be correctly calculated");

        // Verify individual detail calculations
        DetailFacture savedDetail = savedFacture.getDetailsFacture().iterator().next();
        assertEquals(382.5f, savedDetail.getPrixTotalDetail(), 0.001, "Prix Total Detail should be correctly calculated");
        assertEquals(67.5f, savedDetail.getMontantRemise(), 0.001, "Montant Remise should be correctly calculated");
    }

    /**
     * Test the addFacture method to ensure it throws an exception when a Produit is not found.
     */
    @Test
    public void testAddFacture_ProduitNotFound_ThrowsException() {
        // Arrange
        // Mock produitRepository.findById to return empty, simulating missing produit
        when(produitRepository.findById(produit.getIdProduit())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            factureService.addFacture(facture);
        }, "Expected RuntimeException for non-existent Produit");

        assertEquals("Produit not found with ID: " + produit.getIdProduit(), exception.getMessage(),
                "Exception message should indicate Produit not found");

        // Verify interactions
        verify(produitRepository, times(1)).findById(produit.getIdProduit());
        verify(detailFactureRepository, times(0)).save(any(DetailFacture.class));
        verify(factureRepository, times(0)).save(any(Facture.class));
    }

    /**
     * Test the cancelFacture method to ensure that a facture is correctly archived.
     */
    @Test
    public void testCancelFacture_ArchivesFacture() {
        // Arrange
        Long factureId = facture.getIdFacture();
        when(factureRepository.findById(factureId)).thenReturn(Optional.of(facture));
        when(factureRepository.save(any(Facture.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        factureService.cancelFacture(factureId);

        // Assert
        // Capture the Facture object saved
        ArgumentCaptor<Facture> factureCaptor = ArgumentCaptor.forClass(Facture.class);
        verify(factureRepository, times(1)).findById(factureId);
        verify(factureRepository, times(1)).save(factureCaptor.capture());

        Facture savedFacture = factureCaptor.getValue();
        assertTrue(savedFacture.getArchivee(), "Facture should be archived");
    }

    /**
     * Test the retrieveFacture method to ensure it retrieves an existing facture.
     */
    @Test
    public void testRetrieveFacture_FactureExists_ReturnsFacture() {
        // Arrange
        Long factureId = facture.getIdFacture();
        when(factureRepository.findById(factureId)).thenReturn(Optional.of(facture));

        // Act
        Facture retrievedFacture = factureService.retrieveFacture(factureId);

        // Assert
        assertNotNull(retrievedFacture, "Retrieved Facture should not be null");
        assertEquals(factureId, retrievedFacture.getIdFacture(), "Facture ID should match");
        verify(factureRepository, times(1)).findById(factureId);
    }

    /**
     * Test the retrieveFacture method when the facture does not exist.
     */
    @Test
    public void testRetrieveFacture_FactureDoesNotExist_ReturnsNull() {
        // Arrange
        Long invalidFactureId = 999L;
        when(factureRepository.findById(invalidFactureId)).thenReturn(Optional.empty());

        // Act
        Facture retrievedFacture = factureService.retrieveFacture(invalidFactureId);

        // Assert
        assertNull(retrievedFacture, "Retrieved Facture should be null for non-existent ID");
        verify(factureRepository, times(1)).findById(invalidFactureId);
    }

    /**
     * Test the getFacturesByFournisseur method to ensure it retrieves factures associated with a fournisseur.
     */
    @Test
    public void testGetFacturesByFournisseur_ReturnsList() {
        // Arrange
        Long fournisseurId = fournisseur.getIdFournisseur();
        Set<Facture> factures = new LinkedHashSet<>();
        factures.add(facture);
        fournisseur.setFactures(factures);

        when(fournisseurRepository.findById(fournisseurId)).thenReturn(Optional.of(fournisseur));
        when(factureRepository.findByFournisseur(fournisseur)).thenReturn(new ArrayList<>(factures));

        // Act
        List<Facture> retrievedFactures = factureService.getFacturesByFournisseur(fournisseurId);

        // Assert
        assertNotNull(retrievedFactures, "Retrieved Factures should not be null");
        assertEquals(1, retrievedFactures.size(), "Should retrieve exactly one Facture");
        assertTrue(retrievedFactures.contains(facture), "Retrieved Facture should match the mock");
        verify(fournisseurRepository, times(1)).findById(fournisseurId);
        verify(factureRepository, times(1)).findByFournisseur(fournisseur);
    }

    /**
     * Test the assignOperateurToFacture method to ensure an operateur is correctly assigned to a facture.
     */
    @Test
    public void testAssignOperateurToFacture_Success() {
        // Arrange
        Long operateurId = 1L;
        Long factureId = facture.getIdFacture();
        Operateur operateur = new Operateur();
        operateur.setIdOperateur(operateurId);
        operateur.setNom("Operateur Unique");
        operateur.setFactures(new HashSet<>());

        when(factureRepository.findById(factureId)).thenReturn(Optional.of(facture));
        when(operateurRepository.findById(operateurId)).thenReturn(Optional.of(operateur));
        when(operateurRepository.save(any(Operateur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        factureService.assignOperateurToFacture(operateurId, factureId);

        // Assert
        verify(factureRepository, times(1)).findById(factureId);
        verify(operateurRepository, times(1)).findById(operateurId);
        verify(operateurRepository, times(1)).save(any(Operateur.class));

        assertTrue(operateur.getFactures().contains(facture), "Operateur should be assigned to Facture");
    }

    /**
     * Test the pourcentageRecouvrement method to ensure the percentage is correctly calculated.
     */
    @Test
    public void testPourcentageRecouvrement_CalculatesCorrectly() {
        // Arrange
        Date startDate = new GregorianCalendar(2024, Calendar.MARCH, 1).getTime();
        Date endDate = new GregorianCalendar(2024, Calendar.MARCH, 31).getTime();

        float totalFactures = 1200.0f;
        float totalRecouvrement = 900.0f;

        when(factureRepository.getTotalFacturesEntreDeuxDates(startDate, endDate)).thenReturn(totalFactures);
        when(reglementService.getChiffreAffaireEntreDeuxDate(startDate, endDate)).thenReturn(totalRecouvrement);

        // Act
        float pourcentage = factureService.pourcentageRecouvrement(startDate, endDate);

        // Assert
        assertEquals(75.0f, pourcentage, 0.001, "Pourcentage de recouvrement devrait être de 75%");
        verify(factureRepository, times(1)).getTotalFacturesEntreDeuxDates(startDate, endDate);
        verify(reglementService, times(1)).getChiffreAffaireEntreDeuxDate(startDate, endDate);
    }

    /**
     * Test the pourcentageRecouvrement method when totalFactures is zero to prevent division by zero.
     */
    @Test
    public void testPourcentageRecouvrement_TotalFacturesZero_ReturnsZero() {
        // Arrange
        Date startDate = new GregorianCalendar(2024, Calendar.APRIL, 1).getTime();
        Date endDate = new GregorianCalendar(2024, Calendar.APRIL, 30).getTime();

        float totalFactures = 0.0f;
        float totalRecouvrement = 0.0f;

        when(factureRepository.getTotalFacturesEntreDeuxDates(startDate, endDate)).thenReturn(totalFactures);
        when(reglementService.getChiffreAffaireEntreDeuxDate(startDate, endDate)).thenReturn(totalRecouvrement);

        // Act
        float pourcentage = factureService.pourcentageRecouvrement(startDate, endDate);

        // Assert
        assertEquals(0.0f, pourcentage, 0.001, "Pourcentage de recouvrement devrait être de 0% lorsqu'il n'y a pas de factures");
        verify(factureRepository, times(1)).getTotalFacturesEntreDeuxDates(startDate, endDate);
        verify(reglementService, times(1)).getChiffreAffaireEntreDeuxDate(startDate, endDate);
    }

    /**
     * Test the addFacture method to ensure no calculations are performed when there are no DetailFactures.
     */
    @Test
    public void testAddFacture_NoDetailFacture_SetsMontantZero() {
        // Arrange
        facture.setDetailsFacture(new LinkedHashSet<>()); // Clear details
        when(factureRepository.save(any(Facture.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Facture savedFacture = factureService.addFacture(facture);

        // Assert
        assertEquals(0.0f, savedFacture.getMontantFacture(), 0.001, "Montant Facture should be zero when there are no DetailFactures");
        assertEquals(0.0f, savedFacture.getMontantRemise(), 0.001, "Montant Remise should be zero when there are no DetailFactures");
        verify(detailFactureRepository, times(0)).save(any(DetailFacture.class));
        verify(factureRepository, times(1)).save(any(Facture.class));
    }
}
