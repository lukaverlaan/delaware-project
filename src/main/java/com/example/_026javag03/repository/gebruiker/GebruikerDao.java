package com.example._026javag03.repository.gebruiker;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.repository.GenericDao;
import jakarta.persistence.EntityNotFoundException;

public interface GebruikerDao extends GenericDao<Gebruiker> {

    Gebruiker getGebruikerByEmail(String email) throws EntityNotFoundException;
}