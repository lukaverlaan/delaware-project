package com.example._026javag03.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class GenericDaoJpa<T> implements GenericDao<T> {

    private static final String PU_NAME = "sdp2";

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory(PU_NAME);

    protected EntityManager em;

    private final Class<T> type;

    public GenericDaoJpa(Class<T> type) {
        this.type = type;
        this.em = emf.createEntityManager(); // ✔ nieuwe per DAO
    }

    @Override
    public void closePersistency() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    // 🔥 EXTRA: sluit factory (1x bij afsluiten app)
    public static void closeFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Override
    public void startTransaction() {
        em.getTransaction().begin();
    }

    @Override
    public void commitTransaction() {
        em.getTransaction().commit();
    }

    @Override
    public void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    @Override
    public List<T> findAll() {
        return em.createQuery(
                "select entity from " + type.getSimpleName() + " entity",
                type
        ).getResultList();
    }

    @Override
    public <U> T get(U id) {
        return em.find(type, id);
    }

    @Override
    public T update(T object) {
        return em.merge(object);
    }

    @Override
    public void delete(T object) {
        em.remove(em.merge(object));
    }

    @Override
    public void insert(T object) {
        em.persist(object);
    }

    @Override
    public <U> boolean exists(U id) {
        return em.find(type, id) != null;
    }
}