package CarmineGargiulo.dao;

import CarmineGargiulo.entities.VeicoloPubblico;
import CarmineGargiulo.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

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

    public void getStoricoVeicolo(VeicoloPubblico veicolo) {
        long manutenzioniCount = contaManutenzioniPerVeicolo(veicolo);
        long serviziCount = contaServiziPerVeicolo(veicolo);

        System.out.println("Storico del veicolo con targa: " + veicolo.getTarga());
        System.out.println("Numero di manutenzioni: " + manutenzioniCount);
        System.out.println("Numero di servizi: " + serviziCount);
    }

    public long contaManutenzioniPerVeicolo(VeicoloPubblico veicolo) {
        return entityManager.createQuery(
                        "SELECT COUNT(m) FROM Manutenzione m WHERE m.veicoloPubblico = :veicolo", Long.class)
                .setParameter("veicolo", veicolo)
                .getSingleResult();
    }

    public long contaServiziPerVeicolo(VeicoloPubblico veicolo) {
        return entityManager.createQuery(
                        "SELECT COUNT(s) FROM Servizio s WHERE s.veicoloPubblico = :veicolo", Long.class)
                .setParameter("veicolo", veicolo)
                .getSingleResult();
    }
}
