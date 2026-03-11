package com.example._026javag03.domein.controller;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.domein.beheerder.GebruikerBeheerder;
import com.example._026javag03.dto.GebruikerDTO;
import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.exceptions.ValidatieException;
import com.example._026javag03.util.gebruiker.Rol;
import com.example._026javag03.util.WachtwoordGenerator;
import com.example._026javag03.repository.gebruiker.GebruikerDaoJpa;

import java.util.List;
import java.util.stream.Collectors;

public class GebruikerController {

    private final GebruikerBeheerder beheerder;

    public GebruikerController() {
        beheerder = new GebruikerBeheerder(new GebruikerDaoJpa());
    }

    public List<GebruikerDTO> getGebruikers() {
        return beheerder.getGebruikerList()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void insertGebruiker(GebruikerDTO dto) {

        try {
            Gebruiker gebruiker = mapToEntity(dto);

            String wachtwoord = WachtwoordGenerator.genereerWachtwoord(16);
            gebruiker.setWachtwoord(wachtwoord);

            beheerder.insertGebruiker(gebruiker);

            String jaar = String.valueOf(java.time.Year.now().getValue());
            String pnr = String.format("DLW-%s-%04d", jaar, gebruiker.getId());

            gebruiker.setPersoneelsnummer(pnr);
            beheerder.updateGebruiker(gebruiker);

            System.out.println("Gegenereerd wachtwoord: " + wachtwoord);
        } catch (AdresException | ValidatieException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateGebruiker(GebruikerDTO dto) {

        try {

            Gebruiker gevalideerd = new Gebruiker.Builder(
                    dto.naam(),
                    dto.voornaam(),
                    dto.geboorteDatum(),
                    dto.straat(),
                    dto.nummer(),
                    dto.postbus(),
                    dto.postcode(),
                    dto.stad(),
                    dto.email(),
                    dto.rol(),
                    dto.status()
            )
                    .gsm(dto.telefoonnummer())
                    .build();

            Gebruiker bestaande = beheerder.getGebruikerList()
                    .stream()
                    .filter(g -> g.getId().equals(dto.id()))
                    .findFirst()
                    .orElseThrow();

            bestaande.setNaam(gevalideerd.getNaam());
            bestaande.setVoornaam(gevalideerd.getVoornaam());
            bestaande.setEmail(gevalideerd.getEmail());
            bestaande.setGsm(gevalideerd.getGsm());
            bestaande.setGeboortedatum(gevalideerd.getGeboortedatum());
            bestaande.setRol(gevalideerd.getRol());
            bestaande.setStatus(gevalideerd.getStatus());

            bestaande.getAdres().setStraat(gevalideerd.getAdres().getStraat());
            bestaande.getAdres().setHuisnr(gevalideerd.getAdres().getHuisnr());
            bestaande.getAdres().setPostbus(gevalideerd.getAdres().getPostbus());
            bestaande.getAdres().setPostcode(gevalideerd.getAdres().getPostcode());
            bestaande.getAdres().setStad(gevalideerd.getAdres().getStad());

            beheerder.updateGebruiker(bestaande);

        } catch (AdresException | ValidatieException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private GebruikerDTO mapToDTO(Gebruiker g) {
        return new GebruikerDTO(
                g.getId(),
                g.getPersoneelsnummer(),
                g.getNaam(),
                g.getVoornaam(),
                g.getEmail(),
                g.getGsm(),
                g.getAdres().getStraat(),
                g.getAdres().getHuisnr(),
                g.getAdres().getPostbus(),
                g.getAdres().getStad(),
                g.getAdres().getPostcode(),
                g.getGeboortedatum(),
                g.getRol(),
                g.getStatus()
        );
    }

    private Gebruiker mapToEntity(GebruikerDTO dto) throws AdresException {

        return new Gebruiker.Builder(
                dto.naam(),
                dto.voornaam(),
                dto.geboorteDatum(),
                dto.straat(),
                dto.nummer(),
                dto.postbus(),
                dto.postcode(),
                dto.stad(),
                dto.email(),
                dto.rol(),
                dto.status()
        )
                .gsm(dto.telefoonnummer())
                .build();
    }

    public List<GebruikerDTO> getVerantwoordelijken() {
        return beheerder.getGebruikerList()
                .stream()
                .filter(g -> g.getRol() == Rol.VERANTWOORDELIJKE)
                .map(this::mapToDTO)
                .toList();
    }

    public GebruikerDTO login(String email, String wachtwoord) {

        Gebruiker gebruiker = beheerder.login(email, wachtwoord);

        if (gebruiker == null) {
            throw new IllegalArgumentException("Gebruiker bestaat niet.");
        }

        return mapToDTO(gebruiker);
    }

    public void close() {
        beheerder.closePersistency();
    }
}