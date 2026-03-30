package com.example._026javag03.repository.notificatie;

import com.example._026javag03.domein.Notificatie;
import com.example._026javag03.repository.GenericDaoJpa;

public class NotificatieDaoJpa extends GenericDaoJpa<Notificatie> implements  NotificatieDao {

    public NotificatieDaoJpa() {super(Notificatie.class);}
}
