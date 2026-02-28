package com.example._026javag03.dto;

import com.example._026javag03.util.Rol;
import com.example._026javag03.util.Status;

import java.time.LocalDate;

public record GebruikerDTO(
        Long id,
        String personeelsnummer,
        String naam,
        String voornaam,
        String email,
        String telefoonnummer,
        String straat,
        String nummer,
        String postbus,
        String stad,
        int postcode,
        LocalDate geboorteDatum,
        Rol rol,
        Status status
) {}