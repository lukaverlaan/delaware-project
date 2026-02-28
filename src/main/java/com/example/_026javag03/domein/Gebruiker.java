package com.example._026javag03.domein;

import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.util.Rol;
import com.example._026javag03.util.Status;
import com.example._026javag03.util.ValidatieUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Adres adres;

    @Column(unique = true) // 🔥 NIEUW
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

        public Builder(String naam, String voornaam,
                       LocalDate geboortedatum,
                       String straat, String huisnr, String postbus,
                       int postcode, String stad,
                       String email,
                       Rol rol, Status status) throws AdresException {

            this.naam = naam;
            this.voornaam = voornaam;
            this.geboortedatum = geboortedatum;
            this.adres = Adres.adresBuilder()
                    .buildStraat(straat)
                    .buildHuisnr(huisnr)
                    .buildPostbus(postbus)
                    .buildPostcode(postcode)
                    .buildStad(stad)
                    .buildAdres();
            this.email = email;
            this.rol = rol;
            this.status = status;
        }

        public Builder gsm(String gsm) {
            this.gsm = gsm;
            return this;
        }

        public Gebruiker build() {
            controleerVerplichteVelden();
            controleerLeeftijd();
            controleerEmail();
            controleerGsmVerplichting();
            controleerGsmFormaat();
            return new Gebruiker(this);
        }

        private void controleerVerplichteVelden() {
            if (isLeeg(naam) || isLeeg(voornaam) ||
                    geboortedatum == null || isLeeg(email) ||
                    rol == null || status == null) {
                throw new IllegalArgumentException("Niet alle verplichte velden zijn ingevuld.");
            }
        }

        private void controleerLeeftijd() {
            if (Period.between(geboortedatum, LocalDate.now()).getYears() < 18) {
                throw new IllegalArgumentException("Gebruiker moet minstens 18 jaar oud zijn.");
            }
        }

        private void controleerGsmVerplichting() {
            if (rol == Rol.WERKNEMER && isLeeg(gsm)) {
                throw new IllegalArgumentException("Gsm is verplicht voor werknemers.");
            }
        }

        private void controleerEmail() {
            if (!ValidatieUtil.isGeldigEmail(email)) {
                throw new IllegalArgumentException("Ongeldig e-mailadres.");
            }
        }

        private void controleerGsmFormaat() {
            if (gsm != null && !gsm.isBlank()) {
                if (!ValidatieUtil.isGeldigGsm(gsm)) {
                    throw new IllegalArgumentException("Ongeldig gsm-nummer.");
                }
            }
        }

        private boolean isLeeg(String s) {
            return s == null || s.isBlank();
        }
    }
}