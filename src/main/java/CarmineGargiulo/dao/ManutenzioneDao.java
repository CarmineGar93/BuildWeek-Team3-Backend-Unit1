package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Manutenzione;
import CarmineGargiulo.entities.VeicoloPubblico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;

public class ManutenzioneDao {
    private final EntityManager entityManager;

    public ManutenzioneDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void salvaManutenzione(Manutenzione manutenzione) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();


        VeicoloPubblico veicolo = manutenzione.getVeicoloPubblico();

        if (veicolo.isInServizio()) {
            throw new RuntimeException("Il veicolo " + veicolo.getTarga() + " è attualmente in servizio.");
        }

        veicolo.setInManutenzione(true);
        veicolo.setInServizio(false);

        entityManager.merge(veicolo);

        entityManager.persist(manutenzione);
        transaction.commit();
        System.out.println("La manutenzione " + manutenzione.getManutenzioneId() + " è stata salvata correttamente.");
    }

    public List<Manutenzione> ottieniListaManutenzioni() {
        TypedQuery<Manutenzione> query = entityManager.createQuery("SELECT m FROM Manutenzione m", Manutenzione.class);
        return query.getResultList();
    }

    public Manutenzione findManutenzioneById(String id) {
        Manutenzione cercato = entityManager.find(Manutenzione.class, UUID.fromString(id));
        if (cercato == null) {
            throw new RuntimeException("Non è stata trovata alcuna manutenzione.");
        }
        return cercato;
    }
}
