package com.example._026javag03.domein.beheerder;

import com.example._026javag03.domein.Machine;
import com.example._026javag03.repository.GenericDao;

import java.util.List;

public class MachineBeheerder {

    private final GenericDao<Machine> repo;

    public MachineBeheerder(GenericDao<Machine> repo) {
        this.repo = repo;
    }

    public List<Machine> getMachines(){
        return repo.findAll();
    }

    public void insertMachine(Machine machine){
        try {
            machine.setCode(genereerMachineCode());

            repo.startTransaction();
            repo.insert(machine);
            repo.commitTransaction();

        } catch (Exception e){
            repo.rollbackTransaction();
            throw new IllegalArgumentException("Machine kon niet opgeslagen worden.");
        }
    }

    public void updateMachine(Machine machine){
        try {
            repo.startTransaction();
            repo.update(machine);
            repo.commitTransaction();
        } catch (Exception e){
            repo.rollbackTransaction();
            throw new IllegalArgumentException("Machine kon niet geupdate worden.");
        }
    }

    public void deleteMachine(Machine machine){
        try {
            repo.startTransaction();
            repo.delete(machine);
            repo.commitTransaction();
        } catch (Exception e){
            repo.rollbackTransaction();
            throw new IllegalArgumentException("Machine kon niet verwijderd worden.");
        }
    }

    public void closePersistency(){repo.closePersistency();}

    public String genereerMachineCode() {

        long max = repo.findAll()
                .stream()
                .map(Machine::getCode)
                .filter(code -> code != null && code.startsWith("DLW-MACHINE-"))
                .map(code -> code.replace("DLW-MACHINE-", ""))
                .mapToLong(code -> {
                    try {
                        return Long.parseLong(code);
                    } catch (NumberFormatException e) {
                        return 0L;
                    }
                })
                .max()
                .orElse(0L);

        return String.format("DLW-MACHINE-%04d", max + 1);
    }

}
