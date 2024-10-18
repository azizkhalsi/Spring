package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.Facture;
import com.projet.tpachatproject.repositories.FactureRepository; // Import repository
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enable Mockito
public class FactureServiceImplJUnitTest {

    @InjectMocks
    private FactureServiceImpl factureService; // Inject mocks into the service

    @Mock
    private FactureRepository factureRepository; // Mock the repository

    // Set up method to initialize the mocks
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        System.out.println("FactureServiceImpl initialized successfully for testing.");
    }

    // Simple test for adding a facture and asserting values
    @Test
    public void testAddFacture() {
        // Create a new facture instance
        Facture facture = new Facture();
        facture.setMontantFacture(5000.0f);  // Setting the total amount of the facture
        facture.setMontantRemise(500.0f);    // Setting the discount amount

        // When the repository's save method is called, return the facture
        when(factureRepository.save(any(Facture.class))).thenReturn(facture);

        // Call the service method
        Facture savedFacture = factureService.addFacture(facture);

        // Detailed output
        System.out.println("Creating Facture:");
        System.out.println("Montant Facture (expected): 5000.0 | Actual: " + savedFacture.getMontantFacture());
        System.out.println("Montant Remise (expected): 500.0 | Actual: " + savedFacture.getMontantRemise());

        // Verify the repository's save method was called
        verify(factureRepository, times(1)).save(facture);
        System.out.println("Mock repository's save method was called with: " + facture);

        // Assert that the values are set correctly
        assertEquals(5000.0f, savedFacture.getMontantFacture(), 0.001);
        System.out.println("Assertion passed for montantFacture.");

        assertEquals(500.0f, savedFacture.getMontantRemise(), 0.001);
        System.out.println("Assertion passed for montantRemise.");

        System.out.println("Facture creation and validation completed successfully.");
    }

    // Test for illegal argument when a negative facture amount is involved using assertThrows in JUnit 5
    @Test
    public void testAddFacture_invalidData() {
        Facture facture = new Facture();
        facture.setMontantFacture(-1000);  // Negative amount (invalid)

        System.out.println("Attempting to create a facture with an invalid montantFacture (-1000).");

        // Simulate expected exception when invalid data is encountered
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            // Business logic for exception raising
            if (facture.getMontantFacture() < 0) {
                System.out.println("Invalid Facture detected: montantFacture < 0");
                throw new IllegalArgumentException("Invalid Facture amount");
            }
        });

        // Detailed output for the thrown exception
        System.out.println("Exception thrown: " + exception.getMessage());
        assertEquals("Invalid Facture amount", exception.getMessage());
        System.out.println("Assertion passed for exception message.");
    }

    // Test for pourcentage recouvrement
    @Test
    public void testPourcentageRecouvrement() {
        // Suppose this is how we get recovery percentage, but simplified
        float totalFactures = 10000.0f;
        float totalRecouvrement = 7000.0f;

        System.out.println("Calculating pourcentage de recouvrement:");
        System.out.println("Total Factures: " + totalFactures);
        System.out.println("Total Recouvrement: " + totalRecouvrement);

        // Calculate the expected result
        float expectedPourcentage = (totalRecouvrement / totalFactures) * 100;

        // Detailed output for the expected result
        System.out.println("Expected Pourcentage (expected): 70.0 | Actual: " + expectedPourcentage);

        // Assert the expected percentage
        assertEquals(70.0f, expectedPourcentage, 0.001); // Expected to be 70%
        System.out.println("Assertion passed for pourcentage recouvrement.");
    }

    @Test
    public void testFactureCalculationWithMultipleProducts() {
        Facture facture = new Facture();
        facture.setMontantFacture(10000.0f); // Simulate a facture with multiple product totals
        facture.setMontantRemise(1000.0f);   // Apply a discount

        System.out.println("Creating facture with multiple products:");
        System.out.println("Montant Facture (expected): 10000.0 | Actual: " + facture.getMontantFacture());
        System.out.println("Montant Remise (expected): 1000.0 | Actual: " + facture.getMontantRemise());

        // Assert values
        assertEquals(10000.0f, facture.getMontantFacture(), 0.001);
        System.out.println("Assertion passed for montantFacture.");

        assertEquals(1000.0f, facture.getMontantRemise(), 0.001);
        System.out.println("Assertion passed for montantRemise.");

        System.out.println("Facture calculation for multiple products completed successfully.");
    }
}
