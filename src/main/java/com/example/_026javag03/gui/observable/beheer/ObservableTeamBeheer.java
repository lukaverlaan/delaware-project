package com.example._026javag03.gui.observable.beheer;

import com.example._026javag03.domein.controller.GebruikerController;
import com.example._026javag03.domein.controller.SiteController;
import com.example._026javag03.domein.controller.TeamController;
import com.example._026javag03.gui.observable.ObservableTeam;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

public class ObservableTeamBeheer {

    private final TeamController controller;
    private final SiteController siteController;
    private final GebruikerController gebruikerController;

    private final ObservableList<ObservableTeam> teamObservableList;

    @Getter
    private final FilteredList<ObservableTeam> filteredTeams;

    public ObservableTeamBeheer(
            TeamController controller,
            SiteController siteController,
            GebruikerController gebruikerController
    ) {

        this.controller = controller;
        this.siteController = siteController;
        this.gebruikerController = gebruikerController;

        this.teamObservableList = FXCollections.observableArrayList();

        controller.getTeams()
                .forEach(dto ->
                        teamObservableList.add(
                                new ObservableTeam(dto, siteController, gebruikerController)
                        ));

        this.filteredTeams =
                new FilteredList<>(teamObservableList, t -> true);
    }

    public void refresh() {

        teamObservableList.clear();

        controller.getTeams()
                .forEach(dto ->
                        teamObservableList.add(
                                new ObservableTeam(dto, siteController, gebruikerController)
                        ));
    }

    public void changeFilter(String filterValue) {

        filteredTeams.setPredicate(team -> {

            if (filterValue == null || filterValue.isBlank())
                return true;

            String lower = filterValue.toLowerCase();

            return contains(team.getCode(), lower)
                    || contains(String.valueOf(team.getSite()), lower)
                    || contains(String.valueOf(team.getVerantwoordelijke()), lower)
                    || contains(String.valueOf(team.getAantalMedewerkers()), lower);
        });
    }

    private boolean contains(String value, String filter) {
        return value != null && value.toLowerCase().contains(filter);
    }
}