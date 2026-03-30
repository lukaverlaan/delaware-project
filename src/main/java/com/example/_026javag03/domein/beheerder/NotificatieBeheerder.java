package com.example._026javag03.domein.beheerder;

import com.example._026javag03.domein.Notificatie;
import com.example._026javag03.repository.GenericDao;

import java.util.List;

public class NotificatieBeheerder {

    private final GenericDao<Notificatie> repo;

    public NotificatieBeheerder(GenericDao<Notificatie> repo) {
        this.repo = repo;
    }

    public List<Notificatie> getNotificaties() {
        return repo.findAll();
    }

    public void insertNotificatie(Notificatie notificatie) {

        try {

            notificatie.setCode(genereerNotificatieCode());

            repo.startTransaction();

            repo.insert(notificatie);

            repo.commitTransaction();

        } catch (Exception e) {

            repo.rollbackTransaction();
            throw new IllegalArgumentException("Notificatie kon niet opgeslagen worden.");
        }
    }

    public void updateNotificatie(Notificatie notificatie) {

        try {

            repo.startTransaction();
            repo.update(notificatie);
            repo.commitTransaction();

        } catch (Exception e) {
            repo.rollbackTransaction();
            throw new IllegalArgumentException("Notificatie kon niet geüpdatet worden.");
        }
    }

    public void deleteNotificatie(Notificatie notificatie) {

        try {

            repo.startTransaction();
            repo.delete(notificatie);
            repo.commitTransaction();

        } catch (Exception e) {
            repo.rollbackTransaction();
            throw new IllegalArgumentException("Notificatie kon niet verwijderd worden.");
        }
    }

    public void closePersistency() {
        repo.closePersistency();
    }

    public String genereerNotificatieCode() {

        long max = repo.findAll()
                .stream()
                .map(Notificatie::getId)
                .max(Long::compareTo)
                .orElse(0L);

        return String.format("DLW-NOTI-%04d", max + 1);
    }
}