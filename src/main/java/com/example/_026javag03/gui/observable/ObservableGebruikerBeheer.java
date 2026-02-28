package com.example._026javag03.gui.observable;

import com.example._026javag03.domein.GebruikerController;
import com.example._026javag03.dto.GebruikerDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

public class ObservableGebruikerBeheer {

    private final GebruikerController controller;

    private final ObservableList<ObservableGebruiker> gebruikerObservableList;

    @Getter
    private final FilteredList<ObservableGebruiker> filteredGebruikers;

    public ObservableGebruikerBeheer(GebruikerController controller) {
        this.controller = controller;

        this.gebruikerObservableList = FXCollections.observableArrayList();

        controller.getGebruikers()
                .forEach(dto ->
                        gebruikerObservableList.add(new ObservableGebruiker(dto)));

        this.filteredGebruikers =
                new FilteredList<>(gebruikerObservableList, g -> true);
    }

    public void refresh() {
        gebruikerObservableList.clear();

        controller.getGebruikers()
                .forEach(dto ->
                        gebruikerObservableList.add(new ObservableGebruiker(dto)));
    }

    public void changeFilter(String filterValue) {
        filteredGebruikers.setPredicate(g -> {

            if (filterValue == null || filterValue.isBlank())
                return true;

            String lower = filterValue.toLowerCase();

            return g.getNaam().toLowerCase().contains(lower)
                    || g.getVoornaam().toLowerCase().contains(lower)
                    || g.getEmail().toLowerCase().contains(lower);
        });
    }
}