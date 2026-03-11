package com.example._026javag03.repository.team;

import com.example._026javag03.domein.Team;
import com.example._026javag03.repository.GenericDaoJpa;

public class TeamDaoJpa extends GenericDaoJpa<Team> implements TeamDao {

    public TeamDaoJpa() {
        super(Team.class);
    }
}
