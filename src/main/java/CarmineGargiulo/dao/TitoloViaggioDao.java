package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Abbonamento;
import CarmineGargiulo.entities.Tessera;
import CarmineGargiulo.entities.TitoloViaggio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;

public class TitoloViaggioDao {
    private final EntityManager entityManager;

    public TitoloViaggioDao(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void salvaTitoloViaggio(TitoloViaggio titoloViaggio){
        if(titoloViaggio instanceof Abbonamento){

        }
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(titoloViaggio);
        transaction.commit();
        System.out.println("Titolo viaggio " + titoloViaggio.getTitoloViaggio_id() + " salvato correttamente" );
    }

    public List<TitoloViaggio> ottieniListaTitoliViaggio(){
        TypedQuery<TitoloViaggio> query = entityManager.createNamedQuery("getAllTitoliViaggio", TitoloViaggio.class);
        return query.getResultList();
    }

    public TitoloViaggio findTitoloViaggioById(String id){
        TitoloViaggio cercato = entityManager.find(TitoloViaggio.class, UUID.fromString(id));
        if(cercato == null) throw new RuntimeException(); //TODO creare eccezione
        return cercato;
    }
}
