package com.example._026javag03.gui.observable;

import com.example._026javag03.dto.GebruikerDTO;
import javafx.beans.property.*;
import lombok.Getter;

public class ObservableGebruiker {

    private final LongProperty id;
    private final StringProperty personeelsnummer;
    private final StringProperty naam;
    private final StringProperty voornaam;
    private final StringProperty email;
    private final StringProperty gsm;
    private final StringProperty rol;
    private final StringProperty status;

    @Getter
    private GebruikerDTO dto;

    public ObservableGebruiker(GebruikerDTO dto) {
        this.dto = dto;

        this.id = new SimpleLongProperty(dto.id() != null ? dto.id() : 0);
        this.personeelsnummer = new SimpleStringProperty(dto.personeelsnummer());
        this.naam = new SimpleStringProperty(dto.naam());
        this.voornaam = new SimpleStringProperty(dto.voornaam());
        this.email = new SimpleStringProperty(dto.email());
        this.gsm = new SimpleStringProperty(dto.telefoonnummer());
        this.rol = new SimpleStringProperty(dto.rol().toString());
        this.status = new SimpleStringProperty(dto.status().toString());
    }

    public LongProperty idProperty() { return id; }
    public StringProperty personeelsnummerProperty() { return personeelsnummer; }
    public StringProperty naamProperty() { return naam; }
    public StringProperty voornaamProperty() { return voornaam; }
    public StringProperty emailProperty() { return email; }
    public StringProperty gsmProperty() { return gsm; }
    public StringProperty rolProperty() { return rol; }
    public StringProperty statusProperty() { return status; }

    public Long getId() { return id.get(); }
    public String getNaam() { return naam.get(); }
    public String getVoornaam() { return voornaam.get(); }
    public String getEmail() { return email.get(); }
}