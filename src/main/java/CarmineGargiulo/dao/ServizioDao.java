package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Servizio;
import CarmineGargiulo.entities.VeicoloPubblico;
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

        VeicoloPubblico veicolo = servizio.getVeicoloPubblico();

        if (veicolo != null) {

            if (veicolo.isInManutenzione()) {
                throw new RuntimeException("Il veicolo " + veicolo.getTarga() + " è in manutenzione.");
            }

            veicolo.setInServizio(true);
            entityManager.merge(veicolo);
        } else {
            throw new RuntimeException("Nessun veicolo associato al servizio.");
        }

        entityManager.persist(servizio);
        transaction.commit();
        System.out.println("Il servizio " + servizio.getServizio_id() + "e' stato salvato correttamente");
    }

    public List<Servizio> ottieniListaServizi() {
        TypedQuery<Servizio> query = entityManager.createQuery("SELECT s FROM Servizio s", Servizio.class);
        return query.getResultList();
    }

    public Servizio findServizioById(String id) {
        Servizio cercato = entityManager.find(Servizio.class, UUID.fromString(id));
        if (cercato == null) {
            throw new RuntimeException("Alcun servizio trovato");
        }
        return cercato;
    }
}
