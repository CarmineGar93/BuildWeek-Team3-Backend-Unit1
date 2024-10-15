package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Tessera;
import CarmineGargiulo.entities.Utente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;

public class TessereDAO {
    private final EntityManager entityManager;

    public TessereDAO(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void salvaTessera(Tessera tessera){
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(tessera);
        transaction.commit();
        System.out.println("Tessera " + tessera.getTesseraId() + " salvato correttamente" );
    }

    public List<Tessera> ottieniListaTessere(){
        TypedQuery<Tessera> query = entityManager.createNamedQuery("getAllTessere", Tessera.class);
        return query.getResultList();
    }

    public Tessera findTesseraById(String id){
        Tessera cercato = entityManager.find(Tessera.class, UUID.fromString(id));
        if(cercato == null) throw new RuntimeException(); //TODO creare eccezione
        return cercato;
    }
}
