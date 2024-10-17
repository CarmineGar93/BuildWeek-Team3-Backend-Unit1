package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Manutenzione;
import CarmineGargiulo.entities.Servizio;
import CarmineGargiulo.entities.Tratta;
import CarmineGargiulo.entities.VeicoloPubblico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    public List<Servizio> ottieniListaServizi(VeicoloPubblico veicolo) {
        TypedQuery<Servizio> query = entityManager.createQuery("SELECT s FROM Servizio s WHERE s.veicoloPubblico = :veicolo", Servizio.class);
        query.setParameter("veicolo", veicolo);
        return query.getResultList();
    }

    public List<Servizio> ottieniListaServiziAttuali(VeicoloPubblico veicoloPubblico) {
        LocalDate oggi = LocalDate.now();
        TypedQuery<Servizio> query = entityManager.createQuery(
                        "SELECT s FROM Servizio s " +
                                "WHERE s.veicoloPubblico = :veicoloPubblico AND s.dataInizio <= :oggi " +
                                "AND (s.dataFine IS NULL OR s.dataFine >= :oggi)",
                        Servizio.class)
                .setParameter("veicoloPubblico", veicoloPubblico)
                .setParameter("oggi", oggi);

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

    public void mettiInServizio(VeicoloPubblico veicoloPubblico, Tratta tratta){
        if(veicoloPubblico.isInManutenzione()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO IN MANUTENZIONE
        if(veicoloPubblico.isInServizio()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO GIA IN SERVIZIO
        if(tratta.getServiziList().stream().anyMatch(servizio1 -> servizio1.getDataFine() == null)) throw new RuntimeException(); // TODO CREARE ECCEZIONE TRATTA GIA PERCORSA DA UN ALTRO VEICOLO
        veicoloPubblico.setInServizio(true);
        Servizio servizio = new Servizio(veicoloPubblico, tratta);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(servizio);
        transaction.commit();
        System.out.println("Il servizio " + servizio.getServizio_id() + " è stato salvato correttamente.");
    }

    public void mettiFuoriServizio(VeicoloPubblico veicoloPubblico){
        if(veicoloPubblico.isInManutenzione()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO IN MANUTENZIONE
        if(!veicoloPubblico.isInServizio()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO GIà FUORI SERVIZIO
        veicoloPubblico.setInServizio(false);
        Optional<Servizio> ricerca = veicoloPubblico.getServiziList().stream().filter(servizio -> servizio.getDataFine() == null).findFirst();
        ricerca.ifPresent(servizio -> servizio.setDataFine(LocalDate.now()));
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(veicoloPubblico);
        transaction.commit();
        System.out.println("Il veicolo " + veicoloPubblico.getTarga() + " è stato fermato");
    }
}