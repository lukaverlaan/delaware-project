package com.example._026javag03.gui.observable;

import com.example._026javag03.dto.SiteDTO;
import com.example._026javag03.dto.TaakDTO;
import javafx.beans.property.*;
import lombok.Getter;

public class ObservableTaak {
    private final LongProperty id;
    private final StringProperty type;
    private final StringProperty omscrijving;
    private final StringProperty duurtijd;


    @Getter
    private TaakDTO dto;

    public ObservableTaak(TaakDTO dto) {

        this.dto = dto;

        this.id = new SimpleLongProperty(dto.id() != null ? dto.id() : 0);
        this.type = new SimpleStringProperty(dto.type());
        this.omscrijving = new SimpleStringProperty(dto.omschrijving());
        this.duurtijd = new SimpleStringProperty(dto.duurtijd().toString());

    }

    public LongProperty idProperty() { return id; }
    public StringProperty typeProperty() { return type; }
    public StringProperty omschrijvingProperty() { return omscrijving; }
    public StringProperty duurtijdProperty() { return duurtijd; }


    public String getType() { return type.get(); }
    public String getOmschrijving() { return omscrijving.get(); }
    public String getDuurtijd() { return duurtijd.get(); }

}
