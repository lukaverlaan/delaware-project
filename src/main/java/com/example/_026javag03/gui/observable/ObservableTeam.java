package com.example._026javag03.gui.observable;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.dto.TeamDTO;
import javafx.beans.property.*;
import lombok.Getter;

public class ObservableTeam {

    private final LongProperty id;
    private final StringProperty site;
    private final StringProperty code;
    private final StringProperty verantwoordelijke;
    private final IntegerProperty aantalMedewerkers;

    @Getter
    private TeamDTO dto;

    public ObservableTeam(
            TeamDTO dto,
            SiteController siteController,
            GebruikerController gebruikerController
    ) {

        this.dto = dto;

        this.id = new SimpleLongProperty(dto.id() != null ? dto.id() : 0);

        // Site zoeken op ID
        String siteNaam = siteController.getSites()
                .stream()
                .filter(s -> s.id().equals(dto.site()))
                .map(s -> s.naam())
                .findFirst()
                .orElse("Onbekend");

        this.site = new SimpleStringProperty(siteNaam);

        // Verantwoordelijke zoeken op ID
        String verantwoordelijkeNaam = gebruikerController.getGebruikers()
                .stream()
                .filter(g -> g.id().equals(dto.verantwoordelijke()))
                .map(g -> g.voornaam() + " " + g.naam())
                .findFirst()
                .orElse("Onbekend");

        this.verantwoordelijke = new SimpleStringProperty(verantwoordelijkeNaam);

        this.code = new SimpleStringProperty(dto.teamCode());

        int aantal = dto.werknemers() != null ? dto.werknemers().size() : 0;
        this.aantalMedewerkers = new SimpleIntegerProperty(aantal);
    }

    public LongProperty idProperty() { return id; }
    public StringProperty siteProperty() { return site; }
    public StringProperty codeProperty() { return code; }
    public StringProperty verantwoordelijkeProperty() { return verantwoordelijke; }
    public IntegerProperty aantalMedewerkersProperty() { return aantalMedewerkers; }

    public String getSite() { return site.get(); }
    public String getCode() { return code.get(); }
    public String getVerantwoordelijke() { return verantwoordelijke.get(); }
    public int getAantalMedewerkers() { return aantalMedewerkers.get(); }
}