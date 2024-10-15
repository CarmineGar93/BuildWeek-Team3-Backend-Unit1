package CarmineGargiulo.dao;

import jakarta.persistence.EntityManager;

public class ManutenzioneDAO {
    private final EntityManager entityManager;

    public ManutenzioneDAO(EntityManager entityManager){
        this.entityManager = entityManager;
    }

}
