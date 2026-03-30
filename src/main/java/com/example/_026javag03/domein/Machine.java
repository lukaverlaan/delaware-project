package com.example._026javag03.domein;

import com.example._026javag03.exceptions.MachineException;
import com.example._026javag03.util.machine.MachineAttributes;
import com.example._026javag03.util.machine.ProductieStatusMachine;
import com.example._026javag03.util.machine.StatusMachine;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Access;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "machines")
@Entity
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @ManyToOne
    private Site site;

    private String locatie;
    private String productinfo;

    @Enumerated(EnumType.STRING)
    private StatusMachine status;

    @Enumerated(EnumType.STRING)
    private ProductieStatusMachine productieStatus;

    private Time uptime;
    private LocalDate laatsteOnderhoud;

    @ManyToMany
    @JoinTable(
            name = "machine_werknemers",
            joinColumns = @JoinColumn(name = "machine_id"),
            inverseJoinColumns = @JoinColumn(name = "gebruiker_id")
    )
    private List<Gebruiker> werknemers = new ArrayList<>();

    private Machine(MachineBuilder builder) {
        this.site = builder.site;
        this.locatie = builder.locatie;
        this.productinfo = builder.productInfo;
        this.status = builder.status;
        this.productieStatus = builder.productieStatus;
        this.werknemers = builder.werknemers;
    }

    public static MachineBuilder machineBuilder() {
        return new MachineBuilder();
    }

    public static class MachineBuilder {

        private Site site;
        private String locatie;
        private String productInfo;
        private StatusMachine status;
        private ProductieStatusMachine productieStatus;
        private List<Gebruiker> werknemers = new ArrayList<>();

        public MachineBuilder buildSite(Site site){
            this.site = site;
            return this;
        }

        public MachineBuilder buildLocatie(String locatie){
            this.locatie = locatie;
            return this;
        }

        public MachineBuilder buildProductInfo(String info){
            this.productInfo = info;
            return this;
        }

        public MachineBuilder buildStatus(StatusMachine status){
            this.status = status;
            return this;
        }

        public MachineBuilder buildProductieStatus(ProductieStatusMachine productieStatus){
            this.productieStatus = productieStatus;
            return this;
        }

        public MachineBuilder buildWerknemers(List<Gebruiker> werknemers){
            this.werknemers = werknemers;
            return this;
        }

        protected Map<MachineAttributes,String> reqAttributes = new HashMap<>();

        public Machine build() throws MachineException {

            if(site == null){
                reqAttributes.put(MachineAttributes.SITE,"Er is geen site meegegeven");
            }

            if (locatie == null || locatie.isBlank()){
                reqAttributes.put(MachineAttributes.LOCATIE,"Geen locatie in site");
            }

            if (status == null){
                reqAttributes.put(MachineAttributes.STATUS,"Geen status voor de machine");
            }

            if (productieStatus == null){
                reqAttributes.put(MachineAttributes.PRODUCTIE_STATUS,"Geen productie status");
            }

            if (productInfo == null || productInfo.isBlank()){
                reqAttributes.put(MachineAttributes.PRODUCTIE_INFO,"Geen product beschrijving");
            }

            if(!reqAttributes.isEmpty()){
                throw new MachineException(reqAttributes);
            }

            return new Machine(this);
        }
    }

    @Override
    public String toString() {

        String werknemersString = werknemers == null || werknemers.isEmpty()
                ? "geen werknemers"
                : werknemers.stream()
                .map(Gebruiker::getNaam)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        return "Machine{Code='%d', Site='%s', Locatie=%s, Productinfo=%s, Status=%s, Productie status='%s', Uptime='%s', Laatste onderhoud='%s', Werknemers='%s'}"
                .formatted(
                        code,
                        site.getNaam(),
                        locatie,
                        productinfo,
                        status,
                        productieStatus,
                        uptime != null ? uptime.toString() : "-",
                        laatsteOnderhoud != null ? laatsteOnderhoud.toString() : "-",
                        werknemersString
                );
    }
}