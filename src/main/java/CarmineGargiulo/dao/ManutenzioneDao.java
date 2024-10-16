package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Manutenzione;
import CarmineGargiulo.entities.VeicoloPubblico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class ManutenzioneDao {
    private final EntityManager entityManager;

    public ManutenzioneDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void salvaManutenzione(Manutenzione manutenzione) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        VeicoloPubblico veicolo = manutenzione.getVeicoloPubblico();

        veicolo.setNumeroManutenzioni(veicolo.getNumeroManutenzioni() + 1);

        entityManager.merge(veicolo);
        entityManager.persist(manutenzione);
        transaction.commit();
        System.out.println("La manutenzione " + manutenzione.getManutenzioneId() + " è stata salvata correttamente.");
    }
}
