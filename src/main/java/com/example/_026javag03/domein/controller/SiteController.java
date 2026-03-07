package com.example._026javag03.domein.controller;

import com.example._026javag03.domein.Site;
import com.example._026javag03.domein.beheerder.SiteBeheerder;
import com.example._026javag03.dto.SiteDTO;
import com.example._026javag03.exceptions.SiteException;
import com.example._026javag03.repository.site.SiteDaoJpa;

import java.util.List;
import java.util.stream.Collectors;

public class SiteController {

    private final SiteBeheerder beheerder;

    public SiteController() {
        beheerder = new SiteBeheerder(new SiteDaoJpa());
    }

    public List<SiteDTO> getSites() {

        return beheerder.getSites()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void insertSite(SiteDTO dto) {

        try {

            Site site = mapToEntity(dto);

            beheerder.insertSite(site);

        } catch (SiteException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateSite(SiteDTO dto) {

        try {

            Site gevalideerd = mapToEntity(dto);

            Site bestaande = beheerder.getSites()
                    .stream()
                    .filter(s -> s.getId().equals(dto.id()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Site niet gevonden"));

            // eerst duplicate check
            boolean bestaat = beheerder.getSites()
                    .stream()
                    .anyMatch(s ->
                            !s.getId().equals(dto.id()) &&
                                    s.getNaam().equalsIgnoreCase(gevalideerd.getNaam())
                    );

            if (bestaat) {
                throw new IllegalArgumentException("Er bestaat al een site met deze naam.");
            }

            // pas NA validatie aanpassen
            bestaande.setNaam(gevalideerd.getNaam());
            bestaande.setLocatie(gevalideerd.getLocatie());
            bestaande.setCapaciteit(gevalideerd.getCapaciteit());
            bestaande.setOperationeleStatus(gevalideerd.getOperationeleStatus());
            bestaande.setProductieStatus(gevalideerd.getProductieStatus());

            beheerder.updateSite(bestaande);

        } catch (SiteException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void verwijderSite(Long id) {

        Site site = beheerder.getSites()
                .stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Site niet gevonden"));

        beheerder.deleteSite(site);
    }

    private SiteDTO mapToDTO(Site s) {

        return new SiteDTO(
                s.getId(),
                s.getNaam(),
                s.getLocatie(),
                s.getCapaciteit(),
                s.getOperationeleStatus(),
                s.getProductieStatus()
        );
    }

    private Site mapToEntity(SiteDTO dto) throws SiteException {

        return Site.siteBuilder()
                .buildNaam(dto.naam())
                .buildLocatie(dto.locatie())
                .buildCapaciteit(dto.capaciteit())
                .buildOperationeleStatus(dto.operationeleStatus())
                .buildProductieStatus(dto.productieStatus())
                .buildSite();
    }

    public void close() {
        beheerder.closePersistency();
    }
}