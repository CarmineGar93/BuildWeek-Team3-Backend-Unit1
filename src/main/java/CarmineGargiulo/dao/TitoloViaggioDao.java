package CarmineGargiulo.dao;

import CarmineGargiulo.entities.*;
import CarmineGargiulo.exceptions.AbbonamentoDateException;
import CarmineGargiulo.exceptions.EmptyListException;
import CarmineGargiulo.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TitoloViaggioDao {
    private final EntityManager entityManager;

    public TitoloViaggioDao(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void salvaTitoloViaggio(TitoloViaggio titoloViaggio){
        if(titoloViaggio instanceof Abbonamento){
            List<Abbonamento> abbonamentiList = getAllAbbonamentiByTessera(((Abbonamento) titoloViaggio).getTessera());
            if(!abbonamentiList.isEmpty()){
                if(abbonamentiList.stream().anyMatch(abbonamento -> ((Abbonamento) titoloViaggio).getDataInizio().isBefore(abbonamento.getDataFine()))) throw new AbbonamentoDateException();
            }
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
        if(cercato == null) throw new NotFoundException("titolo viaggio", "id");
        return cercato;
    }

    public List<Abbonamento> getAllAbbonamentiByTessera(Tessera tessera){
        TypedQuery<Abbonamento> query = entityManager.createQuery("SELECT a FROM Abbonamento a WHERE a.tessera = :tessera", Abbonamento.class);
        query.setParameter("tessera", tessera);
        return query.getResultList();
    }

    public List<TitoloViaggio> getAllTitoliViaggioPerPuntoVendita(PuntoVendita puntoVendita){
        TypedQuery<TitoloViaggio> query = entityManager.createQuery("SELECT t FROM TitoloViaggio t WHERE t.puntoVendita = :puntoVendita", TitoloViaggio.class);
        query.setParameter("puntoVendita", puntoVendita);
        List<TitoloViaggio> result = query.getResultList();
        if(result.isEmpty()) throw new EmptyListException();
        return result;
    }


}
