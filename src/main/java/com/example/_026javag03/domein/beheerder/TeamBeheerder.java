package com.example._026javag03.domein.beheerder;

import com.example._026javag03.domein.Gebruiker;
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

    public void insertTeam(Team team){

        teamRepo.startTransaction();
        teamRepo.insert(team);
        teamRepo.commitTransaction();
    }

    public void updateTeam(Team team) {

    }

    public void deleteTeam(Team team){
        teamRepo.startTransaction();
        teamRepo.delete(team);
        teamRepo.commitTransaction();
    }

    public void closePersistency(){
        teamRepo.closePersistency();
    }
}
