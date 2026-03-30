package com.example._026javag03.domein;

import com.example._026javag03.exceptions.SiteException;
import com.example._026javag03.util.site.ProductieStatus;
import com.example._026javag03.util.site.SiteAttributes;
import com.example._026javag03.util.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "sites")
@Getter
@Setter
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String naam;
    private String locatie;
    private int capaciteit;

    @Enumerated(EnumType.STRING)
    @Column(name = "operationele_status")
    private Status operationeleStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "productie_status")
    private ProductieStatus productieStatus;

    public Site() {
    }

    private Site(SiteBuilder builder) {
        this.naam = builder.naam;
        this.locatie = builder.locatie;
        this.capaciteit = builder.capaciteit;
        this.operationeleStatus = builder.operationeleStatus;
        this.productieStatus = builder.productieStatus;
    }

    public static SiteBuilder siteBuilder() {
        return new SiteBuilder();
    }

    public static class SiteBuilder {
        private String naam;
        private String locatie;
        private int capaciteit;
        private Status operationeleStatus;
        private ProductieStatus productieStatus;

        public SiteBuilder buildNaam(String naam) {
            this.naam = naam;
            return this;
        }

        public SiteBuilder buildLocatie(String locatie) {
            this.locatie = locatie;
            return this;
        }

        public SiteBuilder buildCapaciteit(int capaciteit) {
            this.capaciteit = capaciteit;
            return this;
        }

        public SiteBuilder buildOperationeleStatus(Status status) {
            this.operationeleStatus = status;
            return this;
        }

        public SiteBuilder buildProductieStatus(ProductieStatus status) {
            this.productieStatus = status;
            return this;
        }

        protected Map<SiteAttributes,String> reqAttributes = new HashMap<>();
        public Site buildSite() throws SiteException {

            // Validatie naam
            if (naam == null || naam.isBlank()) {
                reqAttributes.put(SiteAttributes.NAAM,"Naam is leeg");
            }

            // Validatie locatie
            if (locatie == null || locatie.isBlank()) {
                reqAttributes.put(SiteAttributes.LOCATIE,"Locatie is leeg");
            }

            // Validatie capaciteit
            if (capaciteit <= 0) {
                reqAttributes.put(SiteAttributes.CAPACITEIT,"Capaciteit is leeg");
            }

            // Validatie operationele status
            if (operationeleStatus == null) {
                reqAttributes.put(SiteAttributes.OPERATIONELE_STATUS,"Operationele status is leeg");
            }

            // Validatie productie status
            if (productieStatus == null) {
                reqAttributes.put(SiteAttributes.PRODUCTIE_STATUS,"Productie status is leeg");
            }

            if(!reqAttributes.isEmpty()){
                throw new SiteException(reqAttributes);
            }

            return new Site(this);
        }
    }

    @Override
    public String toString() {
        return "Site{naam='%s', locatie='%s', capaciteit=%d, operationeleStatus=%s, productieStatus=%s}"
                .formatted(naam, locatie, capaciteit, operationeleStatus, productieStatus);
    }
}