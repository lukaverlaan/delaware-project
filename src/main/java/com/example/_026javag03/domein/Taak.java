package com.example._026javag03.domein;

import com.example._026javag03.exceptions.TaakException;
import com.example._026javag03.util.taak.TaakAttributes;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@Table(name = "taken")
@Getter
@Setter
public class Taak {
    @Id
    @Setter(AccessLevel.NONE)
    private Long id;

    private String type;
    private String omschrijving;
    private LocalTime duurtijd;

    private Taak(TaakBuilder builder){
        this.type = builder.type;
        this.omschrijving = builder.omschrijving;
        this.duurtijd = builder.duurtijd;
    }

    private class TaakBuilder {
        private String type;
        private String omschrijving;
        private LocalTime duurtijd;

        public TaakBuilder buildType(String type){
            this.type = type;
            return this;
        }

        public TaakBuilder buildOmschrijving(String omschrijving){
            this.omschrijving = omschrijving;
            return this;
        }

        public TaakBuilder buildTDuurtijd(LocalTime duurtijd){
            this.duurtijd = duurtijd;
            return this;
        }

        Map<TaakAttributes,String> reqattributes = new HashMap<>();
        public Taak build() throws TaakException {

            if(type.isBlank()){
                reqattributes.put(TaakAttributes.TYPE,"Type mag niet leeg zijn");
            }

            if(omschrijving.isBlank()){
                reqattributes.put(TaakAttributes.OMSCHRIJVING,"Omschrijving mag niet leeg zijn");
            }

            if(duurtijd.isBefore(LocalTime.of(0,15)) && duurtijd.isBefore(LocalTime.of(4,0))){
                reqattributes.put(TaakAttributes.DUURTIJD,"Duurtijd moet minstens 15 minuten zijn en max 4 uur");
            }

            if(!reqattributes.isEmpty()){
                throw new TaakException(reqattributes);
            }

            return new Taak(this);
        }
    }
}
