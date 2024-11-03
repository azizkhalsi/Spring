package com.projet.tpachatproject.controllers;


import com.projet.tpachatproject.entities.Reglement;
import com.projet.tpachatproject.services.IReglementService;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController

@Tag(name="Gestion des reglements")
@RequestMapping("/reglement")
@CrossOrigin("*")
@AllArgsConstructor
public class ReglementRestController {


    IReglementService reglementService;


    // http://localhost:8089/SpringMVC/reglement/add-reglement
    @PostMapping("/add-reglement")
    @ResponseBody
    public Reglement addReglement(@RequestBody Reglement r) {
        Reglement reglement = reglementService.addReglement(r);
        return reglement;
    }
    @GetMapping("/retrieve-all-reglements")
    @ResponseBody
    public List<Reglement> getReglement() {
        List<Reglement> list = reglementService.retrieveAllReglements();
        return list;
    }

    // http://localhost:8089/SpringMVC/reglement/retrieve-reglement/8
    @GetMapping("/retrieve-reglement/{reglement-id}")
    @ResponseBody
    public Reglement retrieveReglement(@PathVariable("reglement-id") Long reglementId) {
        return reglementService.retrieveReglement(reglementId);
    }

    // http://localhost:8089/SpringMVC/reglement/retrieveReglementByFacture/8
    @GetMapping("/retrieveReglementByFacture/{facture-id}")
    @ResponseBody
    public List<Reglement> retrieveReglementByFacture(@PathVariable("facture-id") Long factureId) {
        return reglementService.retrieveReglementByFacture(factureId);
    }

    // http://localhost:8089/SpringMVC/reglement/getChiffreAffaireEntreDeuxDate/{startDate}/{endDate}
    @GetMapping(value = "/getChiffreAffaireEntreDeuxDate/{startDate}/{endDate}")
    public float getChiffreAffaireEntreDeuxDate(
            @PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        try {
            return reglementService.getChiffreAffaireEntreDeuxDate(startDate, endDate);
        } catch (Exception e) {
            return 0;
        }
    }
}
