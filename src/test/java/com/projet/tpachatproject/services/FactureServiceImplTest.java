package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.CategorieProduit;
import com.projet.tpachatproject.entities.DetailFacture;
import com.projet.tpachatproject.entities.Facture;
import com.projet.tpachatproject.entities.Produit;
import com.projet.tpachatproject.repositories.FactureRepository;
import com.projet.tpachatproject.services.FactureServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FactureServiceImplTest{

    @Mock
    private FactureRepository factureRepository;

    @InjectMocks
    private FactureServiceImpl factureService;

    private Facture testFacture;
    private Set<DetailFacture> detailFactures;
    private Produit regularProduct;
    private Produit premiumProduct;
    private CategorieProduit regularCategory;
    private CategorieProduit premiumCategory;

    @BeforeEach
    void setUp() {
        // Set up test data
        regularCategory = new CategorieProduit();
        regularCategory.setLibelleCategorie("REGULAR");

        premiumCategory = new CategorieProduit();
        premiumCategory.setLibelleCategorie("PREMIUM");

        regularProduct = new Produit();
        regularProduct.setCategorieProduit(regularCategory);

        premiumProduct = new Produit();
        premiumProduct.setCategorieProduit(premiumCategory);

        detailFactures = new HashSet<>();
        testFacture = new Facture();
        testFacture.setDetailsFacture(detailFactures);
        testFacture.setMontantRemise(0f);
        testFacture.setDateCreationFacture(new Date());
    }

    @Test
    void testApplySpecialDiscounts_VolumeDiscount() {



        testFacture.setMontantFacture(2000f);
        when(factureRepository.save(any(Facture.class))).thenReturn(testFacture);


        Facture result = factureService.applySpecialDiscounts(testFacture);


        float expectedDiscount = 100f;
        float expectedFinalAmount = 1900f;
        System.out.println("Expectedd discount: " + expectedDiscount);
        System.out.println("Actual discount: " + result.getMontantRemise());
        System.out.println("Expected final amount: " + expectedFinalAmount);
        System.out.println("Actual final amount: " + result.getMontantFacture());

        assertEquals(expectedDiscount, result.getMontantRemise(), 0.01f);
        assertEquals(expectedFinalAmount, result.getMontantFacture(), 0.01f);
        verify(factureRepository).save(testFacture);
    }

    @Test
    void testApplySpecialDiscounts_AllRulesCombined() {

        testFacture.setMontantFacture(2000f);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        testFacture.setDateCreationFacture(cal.getTime());

        for (int i = 0; i < 6; i++) {
            DetailFacture detail = new DetailFacture();
            detail.setProduit(i < 5 ? regularProduct : premiumProduct);
            detailFactures.add(detail);
        }


        when(factureRepository.save(any(Facture.class))).thenReturn(testFacture);
        Facture result = factureService.applySpecialDiscounts(testFacture);

        float expectedDiscount = 180f;
        float expectedFinalAmount = 1820f;
        System.out.println("Expected total discount: " + expectedDiscount);
        System.out.println("Actual total discount: " + result.getMontantRemise());
        System.out.println("Expected final amount: " + expectedFinalAmount);
        System.out.println("Actual final amount: " + result.getMontantFacture());

        assertEquals(expectedDiscount, result.getMontantRemise(), 0.01f);
        assertEquals(expectedFinalAmount, result.getMontantFacture(), 0.01f);
        verify(factureRepository).save(testFacture);
    }

    @Test
    void testApplySpecialDiscounts_QuantityDiscount() {
        testFacture.setMontantFacture(500f);


        for (int i = 0; i < 6; i++) {
            DetailFacture detail = new DetailFacture();
            detail.setProduit(regularProduct);
            detailFactures.add(detail);
        }


        when(factureRepository.save(any(Facture.class))).thenReturn(testFacture);

        Facture result = factureService.applySpecialDiscounts(testFacture);

        float expectedDiscount = 10f;
        float expectedFinalAmount = 490f;
        System.out.println("Expected discount: " + expectedDiscount);
        System.out.println("Actual discount: " + result.getMontantRemise());
        System.out.println("Expected final amount: " + expectedFinalAmount);
        System.out.println("Actual final amount: " + result.getMontantFacture());

        assertEquals(expectedDiscount, result.getMontantRemise(), 0.01f);
        assertEquals(expectedFinalAmount, result.getMontantFacture(), 0.01f);
        verify(factureRepository).save(testFacture);
    }

    @Test
    void testApplySpecialDiscounts_WeekendDiscount() {

        testFacture.setMontantFacture(1000f);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        testFacture.setDateCreationFacture(cal.getTime());

        when(factureRepository.save(any(Facture.class))).thenReturn(testFacture);
        Facture result = factureService.applySpecialDiscounts(testFacture);

        float expectedDiscount = 30f;
        float expectedFinalAmount = 970f;
        System.out.println("Expected weekend discount: " + expectedDiscount);
        System.out.println("Actual discount: " + result.getMontantRemise());
        System.out.println("Expected final amount: " + expectedFinalAmount);
        System.out.println("Actual final amount: " + result.getMontantFacture());

        assertEquals(expectedDiscount, result.getMontantRemise(), 0.01f);
        assertEquals(expectedFinalAmount, result.getMontantFacture(), 0.01f);
        verify(factureRepository).save(testFacture);
    }


    @Test
    void testApplySpecialDiscounts_PremiumCategoryAdjustment() {

        testFacture.setMontantFacture(2000f);
        DetailFacture premiumDetail = new DetailFacture();
        premiumDetail.setProduit(premiumProduct);
        detailFactures.add(premiumDetail);


        when(factureRepository.save(any(Facture.class))).thenReturn(testFacture);

        Facture result = factureService.applySpecialDiscounts(testFacture);

        float volumeDiscount = 100f;
        float premiumAdjustment = 20f;
        float expectedNetDiscount = 80f;
        float expectedFinalAmount = 1920f;

        System.out.println("Expected volume discount: " + volumeDiscount);
        System.out.println("Expected premium adjustment: -" + premiumAdjustment);
        System.out.println("Expected net discount: " + expectedNetDiscount);
        System.out.println("Actual discount: " + result.getMontantRemise());
        System.out.println("Expected final amount: " + expectedFinalAmount);
        System.out.println("Actual final amount: " + result.getMontantFacture());

        assertEquals(expectedNetDiscount, result.getMontantRemise(), 0.01f);
        assertEquals(expectedFinalAmount, result.getMontantFacture(), 0.01f);
        verify(factureRepository).save(testFacture);
    }



}