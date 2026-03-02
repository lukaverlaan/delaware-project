package com.example._026javag03.domein;

import com.example._026javag03.repository.GebruikerDao;
import com.example._026javag03.util.Status;

import java.util.List;

public class GebruikerBeheerder {

    private final GebruikerDao gebruikerRepo;

    public GebruikerBeheerder(GebruikerDao gebruikerDao) {
        this.gebruikerRepo = gebruikerDao;
    }

    public List<Gebruiker> getGebruikerList(){
        return gebruikerRepo.findAll();
    }

    public void insertGebruiker(Gebruiker gebruiker){

        controleerUniekeEmail(gebruiker);

        gebruikerRepo.startTransaction();
        gebruikerRepo.insert(gebruiker);
        gebruikerRepo.commitTransaction();
    }

    public void updateGebruiker(Gebruiker gebruiker) {

        try {
            gebruikerRepo.startTransaction();

            controleerUniekeEmail(gebruiker);

            gebruikerRepo.update(gebruiker);

            gebruikerRepo.commitTransaction();

        } catch (Exception e) {

            gebruikerRepo.rollbackTransaction();

            throw new IllegalArgumentException("Er bestaat al een gebruiker met dit e-mailadres.");
        }
    }

    private void controleerUniekeEmail(Gebruiker gebruiker) {

        Gebruiker bestaande =
                gebruikerRepo.getGebruikerByEmail(gebruiker.getEmail());

        if (bestaande == null) {
            return;
        }

        if (gebruiker.getId() == null ||
                !bestaande.getId().equals(gebruiker.getId())) {

            throw new IllegalArgumentException(
                    "Er bestaat al een gebruiker met dit e-mailadres.");
        }
    }

    public void verwijderGebruiker(Gebruiker gebruiker){
        gebruiker.setStatus(Status.INACTIEF);
        updateGebruiker(gebruiker);
    }

    public void closePersistency() {
        gebruikerRepo.closePersistency();
    }
}