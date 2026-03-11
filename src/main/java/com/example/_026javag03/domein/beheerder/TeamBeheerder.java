package com.example._026javag03.domein.beheerder;

import com.example._026javag03.domein.Team;
import com.example._026javag03.repository.GenericDao;

import java.util.List;

public class TeamBeheerder {

    private final GenericDao<Team> teamRepo;

    public TeamBeheerder(GenericDao<Team> teamRepo) {
        this.teamRepo = teamRepo;
    }

    public List<Team> getTeamList(){
        return teamRepo.findAll();
    }

    public void insertTeam(Team team) {

        try {

            team.setCode(genereerTeamCode());

            teamRepo.startTransaction();

            teamRepo.insert(team);

            teamRepo.commitTransaction();

        } catch (Exception e) {

            teamRepo.rollbackTransaction();
            throw new IllegalArgumentException("Team kon niet opgeslagen worden.");
        }
    }

    public void updateTeam(Team team) {

        try {

            teamRepo.startTransaction();

            teamRepo.update(team);

            teamRepo.commitTransaction();

        } catch (Exception e) {

            teamRepo.rollbackTransaction();
            throw new IllegalArgumentException("Team kon niet geüpdatet worden.");
        }
    }

    public void deleteTeam(Team team){
        teamRepo.startTransaction();
        teamRepo.delete(team);
        teamRepo.commitTransaction();
    }

    public void closePersistency(){
        teamRepo.closePersistency();
    }

    public String genereerTeamCode() {

        long max = teamRepo.findAll()
                .stream()
                .map(Team::getId)
                .max(Long::compareTo)
                .orElse(0L);

        return String.format("DLW-TEAM-%04d", max + 1);
    }
}
