package CarmineGargiulo.dao;

import CarmineGargiulo.entities.VeicoloPubblico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class VeicoloDAO {
    private final EntityManager entityManager;

    public VeicoloDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<VeicoloPubblico> ottieniListaVeicoli() {
        TypedQuery<VeicoloPubblico> query = entityManager.createNamedQuery("getAllVeicoli", VeicoloPubblico.class);
        return query.getResultList();
    }

    public List<VeicoloPubblico> ottieniVeicoliInManutenzione() {
        TypedQuery<VeicoloPubblico> query = entityManager.createQuery(
                "SELECT v FROM VeicoloPubblico v WHERE v.inManutenzione = true", VeicoloPubblico.class);
        return query.getResultList();
    }

    public List<VeicoloPubblico> ottieniVeicoliInServizio() {
        TypedQuery<VeicoloPubblico> query = entityManager.createQuery(
                "SELECT v FROM VeicoloPubblico v WHERE v.inServizio = true", VeicoloPubblico.class);
        return query.getResultList();
    }

    public void salvaVeicolo(VeicoloPubblico veicoloPubblico) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(veicoloPubblico);
            transaction.commit();
            System.out.println("Veicolo " + veicoloPubblico.getTarga() + " salvato correttamente.");
        } catch (Exception e) {
            System.out.println("Errore durante il salvataggio del veicolo: " + veicoloPubblico.getTarga() + ". Potrebbe già esistere un veicolo con questa targa.");
            e.printStackTrace();
        }
    }
}
