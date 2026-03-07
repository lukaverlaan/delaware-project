package com.example._026javag03.gui.observable;

import com.example._026javag03.dto.SiteDTO;
import javafx.beans.property.*;
import lombok.Getter;

public class ObservableSite {

    private final LongProperty id;
    private final StringProperty naam;
    private final StringProperty locatie;
    private final IntegerProperty capaciteit;
    private final StringProperty operationeleStatus;
    private final StringProperty productieStatus;

    @Getter
    private SiteDTO dto;

    public ObservableSite(SiteDTO dto) {

        this.dto = dto;

        this.id = new SimpleLongProperty(dto.id() != null ? dto.id() : 0);
        this.naam = new SimpleStringProperty(dto.naam());
        this.locatie = new SimpleStringProperty(dto.locatie());
        this.capaciteit = new SimpleIntegerProperty(dto.capaciteit());
        this.operationeleStatus = new SimpleStringProperty(dto.operationeleStatus().toString());
        this.productieStatus = new SimpleStringProperty(dto.productieStatus().toString());
    }

    public LongProperty idProperty() { return id; }
    public StringProperty naamProperty() { return naam; }
    public StringProperty locatieProperty() { return locatie; }
    public IntegerProperty capaciteitProperty() { return capaciteit; }
    public StringProperty operationeleStatusProperty() { return operationeleStatus; }
    public StringProperty productieStatusProperty() { return productieStatus; }

    public String getNaam() { return naam.get(); }
    public String getLocatie() { return locatie.get(); }
    public int getCapaciteit() { return capaciteit.get(); }
    public String getOperationeleStatus() { return operationeleStatus.get(); }
    public String getProductieStatus() { return productieStatus.get(); }
}