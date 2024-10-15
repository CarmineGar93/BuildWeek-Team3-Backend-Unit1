package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Tratta;
import CarmineGargiulo.entities.VeicoloPubblico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class VeicoloDAO {
    private final EntityManager entityManager;

    public VeicoloDAO (EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void salvaVeicolo (VeicoloPubblico veicoloPubblico) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(veicoloPubblico);
        transaction.commit();

        System.out.println("Il veicolo " + veicoloPubblico.getTarga() + " salvato correttamente" );
    }

    public List<VeicoloPubblico> ottieniListaVeicoli(){
        TypedQuery<VeicoloPubblico> query = entityManager.createNamedQuery("getAllVeicoli", VeicoloPubblico.class);
        return query.getResultList();
    }
}
