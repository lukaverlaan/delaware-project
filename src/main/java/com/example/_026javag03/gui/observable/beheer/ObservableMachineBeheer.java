package com.example._026javag03.gui.observable.beheer;

import com.example._026javag03.domein.controller.MachineController;
import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.gui.observable.ObservableMachine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

public class ObservableMachineBeheer {

    private final ObservableList<ObservableMachine> machines;

    @Getter
    private final FilteredList<ObservableMachine> filteredMachines;

    private final MachineController controller;
    private final SiteController siteController;

    public ObservableMachineBeheer(MachineController controller, SiteController siteController) {

        this.controller = controller;
        this.siteController = siteController;

        machines = FXCollections.observableArrayList();

        controller.getMachines().forEach(dto ->
                machines.add(new ObservableMachine(dto, siteController))
        );

        filteredMachines = new FilteredList<>(machines, m -> true);
    }

    public void refresh() {
        machines.clear();
        controller.getMachines().forEach(dto ->
                machines.add(new ObservableMachine(dto, siteController))
        );
    }
}