package com.example._026javag03.domein.beheerder;

import com.example._026javag03.domein.Taak;
import com.example._026javag03.repository.GenericDao;
import com.example._026javag03.repository.taak.TaakDao;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TaakBeheerder {

    private final GenericDao<Taak> taakRepo;

    public List<Taak> getTaken(){
        return taakRepo.findAll();
    }

    public void insertTaak(Taak taak){
        taakRepo.startTransaction();
        taakRepo.insert(taak);
        taakRepo.commitTransaction();
    }

    public void updateTaak(Taak taak){
        try {
            taakRepo.startTransaction();
            taakRepo.update(taak);
            taakRepo.commitTransaction();
        } catch (Exception e){
            taakRepo.rollbackTransaction();
        }
    }

    public void deleteTaak(Taak taak){
        taakRepo.delete(taak);
    }

    public void closePersistency(){taakRepo.closePersistency();}
}
