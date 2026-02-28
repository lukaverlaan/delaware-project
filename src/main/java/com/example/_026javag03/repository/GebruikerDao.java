package com.example._026javag03.repository;

import com.example._026javag03.domein.Gebruiker;
import jakarta.persistence.EntityNotFoundException;

public interface GebruikerDao extends GenericDao<Gebruiker> {

    Gebruiker getGebruikerByEmail(String email) throws EntityNotFoundException;
}