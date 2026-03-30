package com.example._026javag03.gui.observable;

import com.example._026javag03.dto.NotificatieDTO;
import javafx.beans.property.*;
import lombok.Getter;

import java.time.LocalDateTime;

public class ObservableNotificatie {

    private final StringProperty code;
    private final ObjectProperty<LocalDateTime> datum;
    private final StringProperty type;
    private final StringProperty inhoud;
    private final StringProperty status;

    @Getter
    private NotificatieDTO dto;

    public ObservableNotificatie(NotificatieDTO dto) {
        this.dto = dto;

        this.code = new SimpleStringProperty(dto.code());
        this.datum = new SimpleObjectProperty<>(dto.datum());
        this.type = new SimpleStringProperty(dto.type());
        this.inhoud = new SimpleStringProperty(dto.inhoud());
        this.status = new SimpleStringProperty(dto.status().toString());
    }

    public StringProperty codeProperty() { return code; }
    public ObjectProperty<LocalDateTime> datumProperty() { return datum; }
    public StringProperty typeProperty() { return type; }
    public StringProperty inhoudProperty() { return inhoud; }
    public StringProperty statusProperty() { return status; }

    public String getCode() { return code.get(); }
    public LocalDateTime getDatum() { return datum.get(); }
    public String getType() { return type.get(); }
    public String getInhoud() { return inhoud.get(); }
    public String getStatus() { return status.get(); }
}