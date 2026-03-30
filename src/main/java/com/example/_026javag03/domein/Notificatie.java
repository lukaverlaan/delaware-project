package com.example._026javag03.domein;

import com.example._026javag03.exceptions.NotificatieException;
import com.example._026javag03.util.notificatie.NotificatieAttributes;
import com.example._026javag03.util.notificatie.NotificatieStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notificaties")
public class Notificatie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private LocalDateTime datum;
    private String type;
    private String inhoud;

    @Enumerated(EnumType.STRING)
    private NotificatieStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gebruiker_id", nullable = false)
    private Gebruiker gebruiker;

    private Notificatie(NotificatieBuilder builder) {
        this.code = builder.code;
        this.datum = builder.datum;
        this.type = builder.type;
        this.inhoud = builder.inhoud;
        this.status = builder.status;
        this.gebruiker = builder.gebruiker;
    }

    public static NotificatieBuilder notificatieBuilder() {
        return new NotificatieBuilder();
    }

    public static class NotificatieBuilder {
        private String code;
        private LocalDateTime datum;
        private String type;
        private String inhoud;
        private NotificatieStatus status;
        private Gebruiker gebruiker;

        public NotificatieBuilder buildCode(String code) {
            this.code = code;
            return this;
        }

        public NotificatieBuilder buildDatum(LocalDateTime datum) {
            this.datum = datum;
            return this;
        }

        public NotificatieBuilder buildType(String type) {
            this.type = type;
            return this;
        }

        public NotificatieBuilder buildInhoud(String inhoud) {
            this.inhoud = inhoud;
            return this;
        }

        public NotificatieBuilder buildStatus(NotificatieStatus status) {
            this.status = status;
            return this;
        }

        public NotificatieBuilder buildGebruiker(Gebruiker gebruiker) {
            this.gebruiker = gebruiker;
            return this;
        }

        protected Map<NotificatieAttributes, String> reqAttributes = new HashMap<>();

        public Notificatie buildNotificatie() throws NotificatieException {

            if (datum == null) {
                reqAttributes.put(NotificatieAttributes.DATUM, "Datum moet ingevuld zijn");
            }

            if (type == null || type.isBlank()) {
                reqAttributes.put(NotificatieAttributes.TYPE, "Type moet ingevuld zijn");
            }

            if (inhoud == null || inhoud.isBlank()) {
                reqAttributes.put(NotificatieAttributes.INHOUD, "Inhoud moet ingevuld zijn");
            }

            if (gebruiker == null) {
                reqAttributes.put(NotificatieAttributes.GEBRUIKER, "Gebruiker is verplicht");
            }

            if (!reqAttributes.isEmpty()) {
                throw new NotificatieException(reqAttributes);
            }

            return new Notificatie(this);
        }
    }

    @Override
    public String toString() {
        return "Notificatie{" +
                "code=" + code +
                ", datum=" + datum +
                ", type='" + type + '\'' +
                ", inhoud='" + inhoud + '\'' +
                ", status=" + status + '\'' +
                ", gebruker=" + gebruiker.getId() +
                '}';
    }
}