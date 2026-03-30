package com.example._026javag03.domein.controller;

import com.example._026javag03.domein.Notificatie;
import com.example._026javag03.domein.beheerder.NotificatieBeheerder;
import com.example._026javag03.dto.NotificatieDTO;
import com.example._026javag03.exceptions.NotificatieException;
import com.example._026javag03.repository.notificatie.NotificatieDaoJpa;

import java.util.List;
import java.util.stream.Collectors;

public class NotificatieController {

    private final NotificatieBeheerder beheerder;

    public NotificatieController() {
        this.beheerder = new NotificatieBeheerder(new NotificatieDaoJpa());
    }

    public List<NotificatieDTO> getNotificaties() {
        return beheerder.getNotificaties()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private NotificatieDTO mapToDto(Notificatie n) {
        return new NotificatieDTO(
                n.getCode(),
                n.getDatum(),
                n.getType(),
                n.getInhoud(),
                n.getStatus()
        );
    }

    public void insertNotificatie(NotificatieDTO dto) {

        try {

            Notificatie notificatie = mapToEntity(dto);

            beheerder.insertNotificatie(notificatie);

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateNotificatie(NotificatieDTO dto) {

        try {

            Notificatie bestaande = beheerder.getNotificaties()
                    .stream()
                    .filter(n -> n.getCode().equals(dto.code()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Notificatie niet gevonden"));

            // update velden
            bestaande.setType(dto.type());
            bestaande.setInhoud(dto.inhoud());
            bestaande.setStatus(dto.status());

            beheerder.updateNotificatie(bestaande);

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void verwijderNotificatie(String code) {

        Notificatie notificatie = beheerder.getNotificaties()
                .stream()
                .filter(n -> n.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Notificatie niet gevonden"));

        beheerder.deleteNotificatie(notificatie);
    }

    private Notificatie mapToEntity(NotificatieDTO dto) throws NotificatieException {

        return Notificatie.notificatieBuilder()
                .buildCode(dto.code())
                .buildDatum(dto.datum())
                .buildType(dto.type())
                .buildInhoud(dto.inhoud())
                .buildStatus(dto.status())
                .buildNotificatie();
    }

    public void close() {
        beheerder.closePersistency();
    }
}