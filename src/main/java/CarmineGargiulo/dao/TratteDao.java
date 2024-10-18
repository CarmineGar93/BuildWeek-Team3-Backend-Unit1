package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Tratta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;

public class TratteDao {
    private final EntityManager entityManager;

    public TratteDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveTratta(Tratta tratta) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(tratta);
        transaction.commit();

        System.out.println("La tratta " + tratta.getNomeTratta() + " è stata creata con successo");
    }

    public Tratta findTratta(UUID trattaId) {
        Tratta found = entityManager.find(Tratta.class, trattaId);
        if (found == null) throw new RuntimeException("La tratta " + trattaId + " non è stata trovata");
        return found;
    }

    public List<Tratta> ottieniListaTratte() {
        TypedQuery<Tratta> query = entityManager.createNamedQuery("getAllTratte", Tratta.class);
        return query.getResultList();
    }


}

