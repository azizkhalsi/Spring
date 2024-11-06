package com.projet.tpachatproject.services;

import com.projet.tpachatproject.entities.Facture;
import com.projet.tpachatproject.entities.Reglement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReglementServiceImplTest {

    @Autowired
    private ReglementServiceImpl reglementService;

    @Test
    public void testRetrieveReglement() {
        assertNull(reglementService.retrieveReglement(999L));
    }

    @Test
    public void testGetChiffreAffaireEntreDeuxDate(){
        Reglement r = new Reglement();
        r.setMontantPaye(100);
        r.setMontantRestant(50);
        r.setPayee(true);
        r.setDateReglement(new Date(2020, 10, 15));

        Facture facture = new Facture();
        facture.setArchivee(false);
        facture.setDateCreationFacture(new Date());
        facture.setDateDerniereModificationFacture(new Date());
        r.setFacture(facture);
        Reglement reg= reglementService.addReglement(r);

        float f = reglementService.getChiffreAffaireEntreDeuxDate(
                new Date(2020, 10, 14),
                new Date(2020, 10, 16)
        );

        assertEquals(f, 300);


    }
}
