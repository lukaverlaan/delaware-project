package com.example._026javag03.domein;

import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.util.gebruiker.AdresAttributes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Embeddable
@Getter
@Setter
public class Adres {
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

        protected Map<AdresAttributes,String> reqAttributes = new HashMap<>();
        public Adres buildAdres() throws AdresException {

            if (straat == null || straat.isBlank()){
                reqAttributes.put(AdresAttributes.STRAAT,"Straat is leeg");
            }
            if ( huisnr == null || huisnr.isEmpty()){
                reqAttributes.put(AdresAttributes.HUISNUMMER,"Huisnummer is leeg");
            } else if(huisnr.charAt(0) == '0' || huisnr.charAt(0) == '-') {
                reqAttributes.put(AdresAttributes.HUISNUMMER,"Huisnummer begint niet met een 0");
            }
            if (postcode <= 0 || String.valueOf(postcode).length() != 4){
                reqAttributes.put(AdresAttributes.POSTCODE,"Postcode moet een geldig zijn");
            }
            if (stad == null || stad.isBlank()){
                reqAttributes.put(AdresAttributes.STAD,"Stad moet ingevuld zijn");
            }

            if(!reqAttributes.isEmpty()){
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