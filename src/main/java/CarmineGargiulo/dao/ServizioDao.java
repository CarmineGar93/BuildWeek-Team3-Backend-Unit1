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

    public List<Servizio> controlloServiziAttivi(){
        TypedQuery<Servizio> query = entityManager.createQuery("SELECT s FROM Servizio s WHERE s.dataFine IS NULL", Servizio.class);
        return query.getResultList();
    }

    public void mettiInServizio(Servizio servizio){
        if(servizio.getVeicoloPubblico().isInManutenzione()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO IN MANUTENZIONE
        if(servizio.getVeicoloPubblico().isInServizio()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO GIA IN SERVIZIO
        if(servizio.getTratta().getServiziList().stream().anyMatch(servizio1 -> servizio1.getDataFine() == null)) throw new RuntimeException(); // TODO CREARE ECCEZIONE TRATTA GIA PERCORSA DA UN ALTRO VEICOLO
        servizio.getVeicoloPubblico().setInServizio(true);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(servizio);
        transaction.commit();
        System.out.println("Il servizio " + servizio.getServizio_id() + " è stato salvato correttamente.");
    }

    public void fuoriServizio(VeicoloPubblico veicoloPubblico){
        if(veicoloPubblico.isInManutenzione()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO IN MANUTENZIONE
        if(!veicoloPubblico.isInServizio()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO GIà FUORI SERVIZIO
        veicoloPubblico.setInServizio(false);
        veicoloPubblico.
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(servizio);
        transaction.commit();
        System.out.println("Il servizio " + servizio.getServizio_id() + " è stato salvato correttamente.");
    }
}