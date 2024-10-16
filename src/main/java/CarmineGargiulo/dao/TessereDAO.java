package CarmineGargiulo.dao;

import CarmineGargiulo.entities.Abbonamento;
import CarmineGargiulo.entities.Tessera;
import CarmineGargiulo.entities.Utente;
import CarmineGargiulo.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
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
        if(cercato == null) throw new NotFoundException("tessera", "id");
        return cercato;
    }

    public void rinnovaTessera(Utente utente){
        if (utente.getTessera() == null) throw new NotFoundException("tessera", "utente");
        Tessera cercata = entityManager.find(Tessera.class, utente.getTessera().getTesseraId());
        if(cercata.getDataScadenza().isAfter(LocalDate.now())) System.out.println("La tessera è ancora in corso di validità");
        else {
            cercata.setDataScadenza(LocalDate.now().plusYears(1));
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(cercata);
            transaction.commit();
        }
    }

    public boolean verificaValiditaAbbonamento(Utente utente){
        if (utente.getTessera() == null) throw new NotFoundException("tessera", "utente");
        TypedQuery<Abbonamento> query = entityManager.createQuery("SELECT a FROM Abbonamento a WHERE a.tessera = :tessera AND a.dataFine > :date ", Abbonamento.class);
        query.setParameter("tessera", utente.getTessera());
        query.setParameter("date", LocalDate.now());
        return !query.getResultList().isEmpty();
    }
}
