package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Biglietto;
import CarmineGargiulo.entities.TitoloViaggio;
import CarmineGargiulo.entities.VeicoloPubblico;
import CarmineGargiulo.exceptions.EmptyListException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class VeicoloDAO {
    private final EntityManager entityManager;

    public VeicoloDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void salvaVeicolo(VeicoloPubblico veicoloPubblico) {
        entityManager.getTransaction().begin();
        entityManager.persist(veicoloPubblico);
        entityManager.getTransaction().commit();
        System.out.println("Il veicolo " + veicoloPubblico.getTarga() + " salvato correttamente");
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



}
