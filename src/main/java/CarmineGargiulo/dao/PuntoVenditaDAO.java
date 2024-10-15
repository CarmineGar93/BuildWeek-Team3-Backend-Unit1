package CarmineGargiulo.dao;

import CarmineGargiulo.entities.PuntoVendita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;


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

    public PuntoVendita findPuntoVenditaById(String id){
        PuntoVendita cercato = entityManager.find(PuntoVendita.class, UUID.fromString(id));
        if(cercato == null) throw new RuntimeException(); //TODO aggiungere eccezione personale
        return cercato;
    }
}
