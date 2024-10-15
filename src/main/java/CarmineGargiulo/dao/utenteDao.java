package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Utente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.UUID;


public class utenteDao {
    private final EntityManager entityManager;

    public utenteDao(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void salvaUtenteDao(Utente utente){
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(utente);
        transaction.commit();
        System.out.println("Utente" + utente.getUtenteId() + " salvato correttamente" );
    }

    public List<Utente> ottieniListaUtenti(){
        TypedQuery<Utente> query = entityManager.createNamedQuery("getAllUsers", Utente.class);
        return query.getResultList();
    }

    public Utente findUtenteById(String id){
        Utente cercato = entityManager.find(Utente.class, UUID.fromString(id));
        if(cercato == null) throw new RuntimeException(); //
        return cercato;
    }
}

