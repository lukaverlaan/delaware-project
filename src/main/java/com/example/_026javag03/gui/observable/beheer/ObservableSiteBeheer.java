package com.example._026javag03.gui.observable.beheer;

import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.gui.observable.ObservableSite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

public class ObservableSiteBeheer {

    private final SiteController controller;

    private final ObservableList<ObservableSite> siteObservableList;

    @Getter
    private final FilteredList<ObservableSite> filteredSites;

    public ObservableSiteBeheer(SiteController controller) {

        this.controller = controller;

        this.siteObservableList = FXCollections.observableArrayList();

        controller.getSites()
                .forEach(dto ->
                        siteObservableList.add(new ObservableSite(dto)));

        this.filteredSites =
                new FilteredList<>(siteObservableList, s -> true);
    }

    public void refresh() {

        siteObservableList.clear();

        controller.getSites()
                .forEach(dto ->
                        siteObservableList.add(new ObservableSite(dto)));
    }

    public void changeFilter(String filterValue) {

        filteredSites.setPredicate(site -> {

            if (filterValue == null || filterValue.isBlank())
                return true;

            String lower = filterValue.toLowerCase();

            return contains(site.getNaam(), lower)
                    || contains(site.getLocatie(), lower)
                    || contains(String.valueOf(site.getCapaciteit()), lower)
                    || contains(site.getOperationeleStatus(), lower)
                    || contains(site.getProductieStatus(), lower);
        });
    }

    private boolean contains(String value, String filter) {
        return value != null && value.toLowerCase().contains(filter);
    }
}