package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Manutenzione;
import CarmineGargiulo.entities.VeicoloPubblico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManutenzioneDao {
    private final EntityManager entityManager;

    public ManutenzioneDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void salvaManutenzione(Manutenzione manutenzione) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(manutenzione);
        transaction.commit();
    }

    public long contaManutenzioniPerVeicolo(VeicoloPubblico veicolo) {
        return entityManager.createQuery(
                        "SELECT COUNT(m) FROM Manutenzione m WHERE m.veicoloPubblico = :veicolo", Long.class)
                .setParameter("veicolo", veicolo)
                .getSingleResult();
    }

    public List<Manutenzione> ottieniListaManutenzioni(VeicoloPubblico veicolo) {
        TypedQuery<Manutenzione> query = entityManager.createQuery(
                "SELECT m FROM Manutenzione m WHERE m.veicoloPubblico = :veicolo", Manutenzione.class);
        query.setParameter("veicolo", veicolo);

        return query.getResultList();
    }


}
