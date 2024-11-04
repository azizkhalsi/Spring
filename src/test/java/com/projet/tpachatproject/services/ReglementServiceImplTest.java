package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.Reglement;
import com.projet.tpachatproject.repositories.FactureRepository;
import com.projet.tpachatproject.repositories.ReglementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReglementServiceImplTest {

    @Mock
    FactureRepository factureRepository;

    @Mock
    ReglementRepository reglementRepository;

    @InjectMocks
    ReglementServiceImpl underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveAllReglements() {
        Reglement reglement1 = new Reglement();
        reglement1.setIdReglement(1L);
        Reglement reglement2 = new Reglement();
        reglement2.setIdReglement(2L);
        List<Reglement> reglements = Arrays.asList(reglement1, reglement2);

        when(reglementRepository.findAll()).thenReturn(reglements);

        List<Reglement> result = underTest.retrieveAllReglements();
        assertEquals(2, result.size());
        verify(reglementRepository, times(1)).findAll();
    }

    @Test
    public void testAddReglement() {
        Reglement reglement = new Reglement();
        reglement.setIdReglement(1L);

        when(reglementRepository.save(reglement)).thenReturn(reglement);

        Reglement result = underTest.addReglement(reglement);
        assertEquals(reglement, result);
        verify(reglementRepository, times(1)).save(reglement);
    }

    @Test
    public void testRetrieveReglement_Found() {
        Long id = 1L;
        Reglement reglement = new Reglement();
        reglement.setIdReglement(id);
        when(reglementRepository.findById(id)).thenReturn(Optional.of(reglement));

        Reglement result = underTest.retrieveReglement(id);
        assertEquals(reglement, result);
        verify(reglementRepository, times(1)).findById(id);
    }

    @Test
    public void testRetrieveReglement_NotFound() {
        Long id = 1L;
        when(reglementRepository.findById(id)).thenReturn(Optional.empty());

        Reglement result = underTest.retrieveReglement(id);
        assertNull(result);
        verify(reglementRepository, times(1)).findById(id);
    }

    @Test
    public void testRetrieveReglementByFacture() {
        Long idFacture = 1L;
        Reglement reglement1 = new Reglement();
        reglement1.setIdReglement(1L);
        Reglement reglement2 = new Reglement();
        reglement2.setIdReglement(2L);
        List<Reglement> reglements = Arrays.asList(reglement1, reglement2);

        when(reglementRepository.retrieveReglementByFacture(idFacture)).thenReturn(reglements);

        List<Reglement> result = underTest.retrieveReglementByFacture(idFacture);
        assertEquals(2, result.size());
        verify(reglementRepository, times(1)).retrieveReglementByFacture(idFacture);
    }

    @Test
    public void testGetChiffreAffaireEntreDeuxDate() {
        Date startDate = new Date();
        Date endDate = new Date();
        float expectedChiffreAffaire = 1000.0f;

        when(reglementRepository.getChiffreAffaireEntreDeuxDate(startDate, endDate)).thenReturn(expectedChiffreAffaire);

        float result = underTest.getChiffreAffaireEntreDeuxDate(startDate, endDate);
        assertEquals(expectedChiffreAffaire, result);
        verify(reglementRepository, times(1)).getChiffreAffaireEntreDeuxDate(startDate, endDate);
    }
}