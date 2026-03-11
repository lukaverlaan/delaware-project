package com.example._026javag03.domein.controller;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.domein.Site;
import com.example._026javag03.domein.Team;
import com.example._026javag03.domein.beheerder.GebruikerBeheerder;
import com.example._026javag03.domein.beheerder.SiteBeheerder;
import com.example._026javag03.domein.beheerder.TeamBeheerder;
import com.example._026javag03.dto.TeamDTO;
import com.example._026javag03.exceptions.TeamException;
import com.example._026javag03.repository.gebruiker.GebruikerDaoJpa;
import com.example._026javag03.repository.site.SiteDaoJpa;
import com.example._026javag03.repository.team.TeamDaoJpa;

import java.util.List;
import java.util.stream.Collectors;

public class TeamController {

    private final TeamBeheerder beheerder;
    private final SiteBeheerder siteBeheerder;
    private final GebruikerBeheerder gebruikerBeheerder;

    public TeamController() {
        beheerder = new TeamBeheerder(new TeamDaoJpa());
        siteBeheerder = new SiteBeheerder(new SiteDaoJpa());
        gebruikerBeheerder = new GebruikerBeheerder(new GebruikerDaoJpa());
    }

    public List<TeamDTO> getTeams() {
        return beheerder.getTeamList()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void voegTeamToe(Long siteId, Long verantwoordelijkeId) throws TeamException {

        Site site = siteBeheerder.getSites()
                .stream()
                .filter(s -> s.getId().equals(siteId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Site niet gevonden"));

        Gebruiker verantwoordelijke = gebruikerBeheerder.getGebruikerList()
                .stream()
                .filter(g -> g.getId().equals(verantwoordelijkeId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

        Team team = Team.teamBuilder()
                .buildSite(site)
                .buildVerantwoordelijke(verantwoordelijke)
                .build();

        beheerder.insertTeam(team);
    }

    public void updateTeam(TeamDTO dto) {

        Team team = beheerder.getTeamList()
                .stream()
                .filter(t -> t.getId().equals(dto.id()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Team niet gevonden"));

        // site zoeken
        Site site = siteBeheerder.getSites()
                .stream()
                .filter(s -> s.getId().equals(dto.site()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Site niet gevonden"));

        // verantwoordelijke zoeken
        Gebruiker verantwoordelijke = gebruikerBeheerder.getGebruikerList()
                .stream()
                .filter(g -> g.getId().equals(dto.verantwoordelijke()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

        // medewerkers zoeken
        List<Gebruiker> medewerkers = dto.werknemers()
                .stream()
                .map(id ->
                        gebruikerBeheerder.getGebruikerList()
                                .stream()
                                .filter(g -> g.getId().equals(id))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"))
                )
                .toList();

        team.setSite(site);
        team.setVerantwoordelijke(verantwoordelijke);
        team.setMedewerkers(medewerkers);

        beheerder.updateTeam(team);
    }

    public void verwijderTeam(Long id) {

        Team team = beheerder.getTeamList()
                .stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Team niet gevonden"));

        beheerder.deleteTeam(team);
    }

    private TeamDTO mapToDTO(Team t) {

        return new TeamDTO(
                t.getId(),
                t.getSite().getId(),
                t.getCode(),
                t.getVerantwoordelijke().getId(),
                t.getMedewerkers()
                        .stream()
                        .map(Gebruiker::getId)
                        .collect(Collectors.toList())
        );
    }

    public void close() {
        beheerder.closePersistency();
    }
}