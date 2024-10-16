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
        entityManager.merge(veicoloPubblico);
        entityManager.getTransaction().commit();
        System.out.println("Il veicolo " + veicoloPubblico.getTarga() + " salvato correttamente");
    }

    public List<VeicoloPubblico> ottieniListaVeicoli() {
        TypedQuery<VeicoloPubblico> query = entityManager.createNamedQuery("getAllVeicoli", VeicoloPubblico.class);
        return query.getResultList();
    }

    public boolean hasManutenzioni(VeicoloPubblico veicolo) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(m) FROM Manutenzione m WHERE m.veicoloPubblico = :veicolo", Long.class);
        query.setParameter("veicolo", veicolo);
        return query.getSingleResult() > 0;
    }

    public boolean hasServizi(VeicoloPubblico veicolo) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(s) FROM Servizio s WHERE s.veicoloPubblico = :veicolo", Long.class);
        query.setParameter("veicolo", veicolo);
        return query.getSingleResult() > 0;
    }



}
