package com.example._026javag03.repository.machine;

import com.example._026javag03.domein.Machine;
import com.example._026javag03.domein.Site;
import com.example._026javag03.repository.GenericDaoJpa;

import java.util.List;

public class MachineDaoJpa extends GenericDaoJpa<Machine> implements MachineDao {

    public MachineDaoJpa() {
        super(Machine.class);
    }

}
