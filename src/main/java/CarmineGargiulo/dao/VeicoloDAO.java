package CarmineGargiulo.dao;

import CarmineGargiulo.entities.*;
import CarmineGargiulo.exceptions.EmptyListException;
import CarmineGargiulo.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public VeicoloPubblico findVeicoloById(String id){
        VeicoloPubblico cercato = entityManager.find(VeicoloPubblico.class, UUID.fromString(id));
        if(cercato == null) throw new NotFoundException("veicolo", "id");
        return cercato;
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



    //aggiunto

    List<Tratta> trattePercorse = new ArrayList<>();

    public void aggiungiTratta(Tratta tratta) {
        trattePercorse.add(tratta);
    }
    // Conta quante volte una tratta è stata percorsa
    public int contaTrattePercorse(String nomeTratta) {
        return (int) trattePercorse.stream().filter(tratta -> tratta.getNomeTratta().equals(nomeTratta)).count();
    }

    // Calcola il tempo totale trascorso su una tratta
    public int tempoTotalePerTratta(String nomeTratta) {
        return trattePercorse.stream().filter(tratta -> tratta.getNomeTratta().equals(nomeTratta)).mapToInt(Tratta::getTempoMedioPercorrenza).sum();
    }





}
