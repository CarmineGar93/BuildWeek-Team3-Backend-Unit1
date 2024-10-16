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


        if (veicolo.isInManutenzione()) {
            throw new RuntimeException("Il veicolo " + veicolo.getTarga() + " è attualmente in manutenzione.");
        }

        veicolo.setInServizio(true);

        entityManager.merge(veicolo);

        entityManager.persist(servizio);
        transaction.commit();
        System.out.println("Il servizio " + servizio.getServizio_id() + " è stato salvato correttamente.");
    }

    public List<Servizio> ottieniListaServizi() {
        TypedQuery<Servizio> query = entityManager.createQuery("SELECT s FROM Servizio s", Servizio.class);
        return query.getResultList();
    }

    public Servizio findServizioById(String id) {
        Servizio cercato = entityManager.find(Servizio.class, UUID.fromString(id));
        if (cercato == null) {
            throw new RuntimeException("Nessun servizio trovato.");
        }
        return cercato;
    }

    public long contaServiziPerVeicolo(VeicoloPubblico veicolo) {
        return entityManager.createQuery(
                        "SELECT COUNT(s) FROM Servizio s WHERE s.veicoloPubblico = :veicolo", Long.class)
                .setParameter("veicolo", veicolo)
                .getSingleResult();
    }
}