package com.example._026javag03.domein.controller;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.domein.Team;
import com.example._026javag03.domein.beheerder.TeamBeheerder;
import com.example._026javag03.dto.TeamDTO;
import com.example._026javag03.exceptions.TeamException;

import java.util.List;
import java.util.stream.Collectors;

public class TeamController {
    private final TeamBeheerder teamBeheerder;

    public TeamController(TeamBeheerder teamBeheerder) {
        this.teamBeheerder = teamBeheerder;
    }

    public List<TeamDTO> getTeams(){
        return teamBeheerder.getTeamList()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private TeamDTO mapToDTO(Team t) {
        return new TeamDTO(
                t.getId(),
                t.getSite().getId(),
                t.getCode(),
                t.getId(),
                t.getMedewerkers().stream().map(Gebruiker::getId).collect(Collectors.toList())
                );
    }

    private Team mapToEntity(TeamDTO dto) throws TeamException {
        return null;
    }

    private void updateTeam(TeamDTO dto){

    }
}
