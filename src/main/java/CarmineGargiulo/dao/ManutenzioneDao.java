package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Manutenzione;
import CarmineGargiulo.entities.VeicoloPubblico;
import CarmineGargiulo.enums.TipoManutenzione;
import CarmineGargiulo.exceptions.ManutenzioneOrServizioException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.Optional;

public class ManutenzioneDao {
    private static EntityManager entityManager = null;

    public ManutenzioneDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void mettiInManutenzione(VeicoloPubblico veicoloPubblico, TipoManutenzione tipoManutenzione) {
        if (veicoloPubblico.isInServizio()) throw new ManutenzioneOrServizioException("in servizio", true);
        if (veicoloPubblico.isInManutenzione()) throw new ManutenzioneOrServizioException("già in manutenzione", true);
        veicoloPubblico.setInManutenzione(true);
        Manutenzione manutenzione = new Manutenzione(tipoManutenzione, veicoloPubblico);
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(manutenzione);
        transaction.commit();
        System.out.println("Il veicolo " + manutenzione.getVeicoloPubblico().getTarga() + " è stato messo in manutenzione");
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

    public void terminaManutenzione(VeicoloPubblico veicoloPubblico) {
        if (veicoloPubblico.isInServizio() || !veicoloPubblico.isInManutenzione())
            throw new ManutenzioneOrServizioException("in manutenzione", false);
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
