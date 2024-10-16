package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Servizio;
import CarmineGargiulo.entities.VeicoloPubblico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

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

    public long contaServiziPerVeicolo(VeicoloPubblico veicolo) {
        return entityManager.createQuery(
                        "SELECT COUNT(s) FROM Servizio s WHERE s.veicoloPubblico = :veicolo", Long.class)
                .setParameter("veicolo", veicolo)
                .getSingleResult();
    }
}
