package com.example._026javag03.gui.observable.beheer;

import com.example._026javag03.gui.observable.ObservableGebruiker;
import com.example._026javag03.gui.observable.ObservableMachine;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

public class ObservableMachineBeheer {

    @Getter
    private final FilteredList<ObservableMachine> filteredMachines;

    public ObservableMachineBeheer(ObservableList<ObservableMachine> machineObservableList, FilteredList<ObservableMachine> filteredMachines) {
        this.filteredMachines = filteredMachines;

    }
}
