package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Servizio;
import CarmineGargiulo.entities.Tratta;
import CarmineGargiulo.entities.VeicoloPubblico;
import CarmineGargiulo.exceptions.ManutenzioneOrServizioException;
import CarmineGargiulo.exceptions.TrattaGiaPercorsaException;
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

    public void mettiInServizio(VeicoloPubblico veicoloPubblico, Tratta tratta){
        if(veicoloPubblico.isInManutenzione()) throw new ManutenzioneOrServizioException("in manutenzione", true);
        if(veicoloPubblico.isInServizio()) throw new ManutenzioneOrServizioException ("già in servizio", true);
        if(tratta.getServiziList().stream().anyMatch(servizio1 -> servizio1.getDataFine() == null)) throw new TrattaGiaPercorsaException();
        veicoloPubblico.setInServizio(true);
        Servizio servizio = new Servizio(veicoloPubblico, tratta);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(servizio);
        transaction.commit();
        System.out.println("Il servizio " + servizio.getServizio_id() + " è stato salvato correttamente.");
    }

    public void mettiFuoriServizio(VeicoloPubblico veicoloPubblico){
        if(veicoloPubblico.isInManutenzione() || !veicoloPubblico.isInServizio()) throw new ManutenzioneOrServizioException("in servizio", false);
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