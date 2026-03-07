package com.example._026javag03.repository.gebruiker;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.repository.GenericDaoJpa;
import jakarta.persistence.NoResultException;

public class GebruikerDaoJpa extends GenericDaoJpa<Gebruiker> implements GebruikerDao {

    public GebruikerDaoJpa() {
        super(Gebruiker.class);
    }

    @Override
    public Gebruiker getGebruikerByEmail(String email) {

        try {
            return em.createQuery(
                            "SELECT g FROM Gebruiker g WHERE LOWER(g.email) = LOWER(:email)",
                            Gebruiker.class)
                    .setParameter("email", email)
                    .getSingleResult();

        } catch (NoResultException ex) {
            return null;
        }
    }
}