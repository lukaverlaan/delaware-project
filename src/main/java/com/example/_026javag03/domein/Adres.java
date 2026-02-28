package com.example._026javag03.domein;

import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.util.AdresAtrributes;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "adressen")
@Getter
@Setter
public class Adres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int id;

    private String straat;
    private String huisnr;
    private String postbus;
    private int postcode;
    private String stad;

    public Adres(){

    }

    private Adres(AdresBuilder builder) {
        this.straat = builder.straat;
        this.huisnr = builder.huisnr;
        this.postbus = builder.postbus;
        this.postcode = builder.postcode;
        this.stad = builder.stad;
    }

    public static AdresBuilder adresBuilder(){
        return new AdresBuilder();
    }

    public static class AdresBuilder {
        private String straat;
        private String huisnr;
        private String postbus;
        private int postcode;
        private String stad;

        public AdresBuilder buildStraat(String straat) {
            this.straat = straat;
            return this;
        }

        public AdresBuilder buildHuisnr(String huisnr) {
            this.huisnr = huisnr;
            return this;
        }

        public AdresBuilder buildPostbus(String postbus) {
            this.postbus = postbus;
            return this;
        }

        public AdresBuilder buildPostcode(int postcode) {
            this.postcode = postcode;
            return this;
        }

        public AdresBuilder buildStad(String stad) {
            this.stad = stad;
            return this;
        }

        protected Set<AdresAtrributes> reqAttributes = new HashSet<>();
        public Adres buildAdres() throws AdresException {

            if (straat == null || straat.isBlank()) {
                reqAttributes.add(AdresAtrributes.STRAAT);
            }

            if (huisnr == null || huisnr.isBlank()) {
                reqAttributes.add(AdresAtrributes.HUISNUMMER);
            }

            if (postcode <= 0 || String.valueOf(postcode).length() != 4) {
                reqAttributes.add(AdresAtrributes.POSTCODE);
            }

            if (stad == null || stad.isBlank()) {
                reqAttributes.add(AdresAtrributes.STAD);
            }

            if (!reqAttributes.isEmpty()) {
                throw new AdresException(reqAttributes);
            }

            return new Adres(this);
        }
    }

    @Override
    public String toString() {

        String basis = "%s %s".formatted(straat, huisnr);

        if (postbus != null && !postbus.isBlank()) {
            basis += " bus %s".formatted(postbus);
        }

        return "%s, %d %s"
                .formatted(basis, postcode, stad);
    }
}