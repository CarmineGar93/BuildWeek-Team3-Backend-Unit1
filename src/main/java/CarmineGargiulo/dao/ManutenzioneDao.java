package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Manutenzione;
import CarmineGargiulo.entities.Servizio;
import CarmineGargiulo.entities.VeicoloPubblico;
import CarmineGargiulo.enums.TipoManutenzione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.util.Optional;

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

    public List<Manutenzione> ottieniListaManutenzioniAttuali(VeicoloPubblico veicoloPubblico) {
        LocalDate oggi = LocalDate.now();
        TypedQuery<Manutenzione> query = entityManager.createQuery(
                        "SELECT m FROM Manutenzione m " +
                                "WHERE m.veicoloPubblico = :veicoloPubblico AND m.dataInizio <= :oggi " +
                                "AND (m.dataFine IS NULL OR m.dataFine >= :oggi)",
                        Manutenzione.class)
                .setParameter("veicoloPubblico", veicoloPubblico)
                .setParameter("oggi", oggi);

        return query.getResultList();
    }




    public void mettiInManutenzione(VeicoloPubblico veicoloPubblico, TipoManutenzione tipoManutenzione){
        if(veicoloPubblico.isInServizio()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO IN SERVIZIO
        if(veicoloPubblico.isInManutenzione()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO GIA IN MANUTENZIONE
        veicoloPubblico.setInManutenzione(true);
        Manutenzione manutenzione = new Manutenzione(tipoManutenzione, veicoloPubblico);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(manutenzione);
        transaction.commit();
        System.out.println("Il veicolo " + manutenzione.getVeicoloPubblico().getTarga() + " è stato messo in manutenzione");
    }

    public void terminaManutenzione(VeicoloPubblico veicoloPubblico){
        if(veicoloPubblico.isInServizio()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO IN SERVIZIO
        if(!veicoloPubblico.isInManutenzione()) throw new RuntimeException(); // TODO CREARE ECCEZIONE VEICOLO GIà NON IN MANUTENZIONE
        veicoloPubblico.setInManutenzione(false);
        Optional<Manutenzione> ricerca = veicoloPubblico.getManutenzionList().stream().filter(manutenzione -> manutenzione.getDataFine() == null).findFirst();
        ricerca.ifPresent(manutenzione -> manutenzione.setDataFine(LocalDate.now()));
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(veicoloPubblico);
        transaction.commit();
        System.out.println("Il veicolo " + veicoloPubblico.getTarga() + " è fuori manutenzione");
    }
}
