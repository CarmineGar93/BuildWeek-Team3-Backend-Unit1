package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Servizio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;

public class ServizioDao {
    private final EntityManager entityManager;

    public ServizioDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void salvaServizio(Servizio servizio) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(servizio);
        transaction.commit();
    }

    public List<Servizio> ottieniListaServizi() {
        TypedQuery<Servizio> query = entityManager.createQuery("SELECT s FROM Servizio s", Servizio.class);
        return query.getResultList();
    }

    public Servizio findServizioById(String id) {
        Servizio cercato = entityManager.find(Servizio.class, UUID.fromString(id));
        if (cercato == null) {
            throw new RuntimeException("Alcun servizio trovato.");
        }
        return cercato;
    }
}
