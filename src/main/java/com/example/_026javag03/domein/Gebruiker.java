package com.example._026javag03.domein;

import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.exceptions.ValidatieException;
import com.example._026javag03.util.gebruiker.Rol;
import com.example._026javag03.util.Status;
import com.example._026javag03.util.gebruiker.ValidatieUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "gebruikers")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "personeelsnummer")
public class Gebruiker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(unique = true)
    private String personeelsnummer;
    private String naam;
    private String voornaam;
    @Embedded
    private Adres adres;
    @Column(unique = true)
    private String email;
    private String gsm;
    private LocalDate geboortedatum;
    @Enumerated(EnumType.STRING)
    private Rol rol;
    @Enumerated(EnumType.STRING)
    private Status status;

    private Gebruiker(Builder builder) {
        this.naam = builder.naam;
        this.voornaam = builder.voornaam;
        this.adres = builder.adres;
        this.email = builder.email;
        this.gsm = builder.gsm;
        this.geboortedatum = builder.geboortedatum;
        this.rol = builder.rol;
        this.status = builder.status;
    }

    public static class Builder {
        private final String naam;
        private final String voornaam;
        private final LocalDate geboortedatum;
        private final Adres adres;
        private final String email;
        private final Rol rol;
        private final Status status;
        private String gsm = null;

        public Builder(String naam, String voornaam, LocalDate geboortedatum, String straat, String huisnr, String postbus, int postcode, String stad, String email, Rol rol, Status status) throws AdresException {
            this.naam = naam;
            this.voornaam = voornaam;
            this.geboortedatum = geboortedatum;
            this.adres = Adres.adresBuilder().buildStraat(straat).buildHuisnr(huisnr).buildPostbus(postbus).buildPostcode(postcode).buildStad(stad).buildAdres();
            this.email = email;
            this.rol = rol;
            this.status = status;
        }

        public Builder gsm(String gsm) {
            this.gsm = gsm;
            return this;
        }

        public Gebruiker build() {
            Map<String, String> fouten = new HashMap<>();
            if (isLeeg(naam)) fouten.put("naam", "Naam is verplicht.");
            if (isLeeg(voornaam)) fouten.put("voornaam", "Voornaam is verplicht.");
            if (geboortedatum == null) fouten.put("geboortedatum", "Geboortedatum is verplicht.");
            if (!ValidatieUtil.isGeldigEmail(email)) fouten.put("email", "Ongeldig e-mailadres.");
            if (rol == Rol.WERKNEMER && isLeeg(gsm)) fouten.put("gsm", "Gsm is verplicht voor werknemers.");
            if (!fouten.isEmpty()) throw new ValidatieException(fouten);
            return new Gebruiker(this);
        }

        private boolean isLeeg(String s) {
            return s == null || s.isBlank();
        }
    }
}