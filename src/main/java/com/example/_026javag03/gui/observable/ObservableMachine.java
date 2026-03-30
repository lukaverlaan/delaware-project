package com.example._026javag03.gui.observable;

import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.dto.MachineDTO;
import javafx.beans.property.*;

public class ObservableMachine {

    private final LongProperty id;
    private final StringProperty status;
    private final StringProperty productInfo;
    private final StringProperty site;
    private final StringProperty onderhoud;
    private final StringProperty uptime;

    private final MachineDTO dto;

    public ObservableMachine(MachineDTO dto, SiteController siteController) {

        this.dto = dto;

        this.id = new SimpleLongProperty(dto.id());

        this.status = new SimpleStringProperty(dto.status().name());
        this.productInfo = new SimpleStringProperty(dto.productinfo());

        String siteNaam = siteController.getSites()
                .stream()
                .filter(s -> s.id().equals(dto.siteId()))
                .map(s -> s.naam())
                .findFirst()
                .orElse("Onbekend");

        this.site = new SimpleStringProperty(siteNaam);

        this.onderhoud = new SimpleStringProperty(
                dto.laatsteOnderhoud() != null ? dto.laatsteOnderhoud().toString() : "-"
        );

        this.uptime = new SimpleStringProperty(dto.uptime());
    }

    public LongProperty idProperty() { return id; }
    public StringProperty statusProperty() { return status; }
    public StringProperty productInfoProperty() { return productInfo; }
    public StringProperty siteProperty() { return site; }
    public StringProperty onderhoudProperty() { return onderhoud; }
    public StringProperty uptimeProperty() { return uptime; }

    public MachineDTO getDto() {
        return dto;
    }
}