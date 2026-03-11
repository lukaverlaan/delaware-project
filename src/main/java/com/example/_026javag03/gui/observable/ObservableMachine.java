package com.example._026javag03.gui.observable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;



public class ObservableMachine {

    public ObservableValue<String> statusProperty() {
        return new SimpleStringProperty("Draait");
    }

    public ObservableValue<String> productInfoProperty() {
        return new SimpleStringProperty("Games");
    }

    public ObservableValue<String> siteProperty() {
        return new SimpleStringProperty("Delaware Gent");
    }

    public ObservableValue<String> onderhoudProperty() {
        return new SimpleStringProperty("8/2/2026");
    }

    public ObservableValue<String> uptimeProperty() {
        return new SimpleStringProperty("5 uur");
    }
}
