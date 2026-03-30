package com.example._026javag03.gui.observable.beheer;

import com.example._026javag03.domein.controller.NotificatieController;
import com.example._026javag03.gui.observable.ObservableNotificatie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

public class ObservableNotificatieBeheer {

    private final NotificatieController controller;

    private final ObservableList<ObservableNotificatie> notificaties;

    @Getter
    private final FilteredList<ObservableNotificatie> filteredNotificaties;

    public ObservableNotificatieBeheer(NotificatieController controller) {

        this.controller = controller;

        this.notificaties = FXCollections.observableArrayList();

        controller.getNotificaties()
                .forEach(dto -> notificaties.add(new ObservableNotificatie(dto)));

        this.filteredNotificaties =
                new FilteredList<>(notificaties, n -> true);
    }

    public void refresh() {
        notificaties.clear();

        controller.getNotificaties()
                .forEach(dto -> notificaties.add(new ObservableNotificatie(dto)));
    }

    public void setFilter(String filter) {

        filteredNotificaties.setPredicate(n -> {

            if (filter == null || filter.isBlank())
                return true;

            String lower = filter.toLowerCase();

            return contains(n.getType(), lower)
                    || contains(n.getInhoud(), lower)
                    || contains(n.getStatus(), lower)
                    || String.valueOf(n.getCode()).contains(lower);
        });
    }

    private boolean contains(String value, String filter) {
        return value != null && value.toLowerCase().contains(filter);
    }
}