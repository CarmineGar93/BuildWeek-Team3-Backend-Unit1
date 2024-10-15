package CarmineGargiulo.dao;

import CarmineGargiulo.entities.PuntoVendita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.TypedQuery;

import java.util.List;


public class PuntoVenditaDAO {
    private final EntityManager entityManager;

    public PuntoVenditaDAO(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void salvaPuntoVendita(PuntoVendita puntoVendita){
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(puntoVendita);
        transaction.commit();
        System.out.println("Punto vendita " + puntoVendita.getPuntoVenditaId() + " salvato correttamente" );
    }

    public List<PuntoVendita> ottieniListaPuntiVendita(){
        TypedQuery<PuntoVendita> query = entityManager.createNamedQuery("getAllPuntiVendita", PuntoVendita.class);
        return query.getResultList();
    }
}
