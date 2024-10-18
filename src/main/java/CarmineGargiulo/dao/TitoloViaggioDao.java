package CarmineGargiulo.dao;

import CarmineGargiulo.entities.*;
import CarmineGargiulo.exceptions.*;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TitoloViaggioDao {
    private final EntityManager entityManager;
    private final Faker faker = new Faker();

    public TitoloViaggioDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void salvaTitoloViaggio(TitoloViaggio titoloViaggio) {
        String descrizione;
        if (titoloViaggio instanceof Biglietto) {
            descrizione = faker.commerce().productName() + " - " + faker.number().digits(5);
        } else if (titoloViaggio instanceof Abbonamento) {
            descrizione = faker.book().title() + " - " + faker.number().digits(5);
        } else {
            descrizione = "Titolo di viaggio generico - " + faker.number().digits(5);
        }

        if (titoloViaggio instanceof Abbonamento) {
            Abbonamento abbonamento = (Abbonamento) titoloViaggio;
            if (abbonamento.getTessera().getDataScadenza().isBefore(LocalDate.now())) {
                throw new TesseraScadutaException();
            }

            List<Abbonamento> abbonamentiList = getAllAbbonamentiByTessera(abbonamento.getTessera());
            if (!abbonamentiList.isEmpty()) {
                if (abbonamentiList.stream().anyMatch(a -> abbonamento.getDataInizio().isBefore(a.getDataFine()))) {
                    throw new AbbonamentoDateException();
                }
            }
        }

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(titoloViaggio);
        transaction.commit();

        System.out.println("\nTitolo di viaggio '" + descrizione + "' salvato correttamente.");
    }

    public List<TitoloViaggio> ottieniListaTitoliViaggio() {
        TypedQuery<TitoloViaggio> query = entityManager.createNamedQuery("getAllTitoliViaggio", TitoloViaggio.class);
        return query.getResultList();
    }

    public TitoloViaggio findTitoloViaggioById(String id) {
        TitoloViaggio cercato = entityManager.find(TitoloViaggio.class, UUID.fromString(id));
        if (cercato == null) throw new NotFoundException("titolo viaggio", "id");
        return cercato;
    }

    public List<Abbonamento> getAllAbbonamentiByTessera(Tessera tessera) {
        TypedQuery<Abbonamento> query = entityManager.createQuery("SELECT a FROM Abbonamento a WHERE a.tessera = :tessera", Abbonamento.class);
        query.setParameter("tessera", tessera);
        return query.getResultList();
    }

    public List<TitoloViaggio> getAllTitoliViaggioPerPuntoVendita(PuntoVendita puntoVendita) {
        TypedQuery<TitoloViaggio> query = entityManager.createQuery("SELECT t FROM TitoloViaggio t WHERE t.puntoVendita = :puntoVendita", TitoloViaggio.class);
        query.setParameter("puntoVendita", puntoVendita);
        List<TitoloViaggio> result = query.getResultList();
        if (result.isEmpty()) throw new EmptyListException();
        return result;
    }

    public List<TitoloViaggio> getAllTitoliViaggioPerPeriodo(int giorni) {
        TypedQuery<TitoloViaggio> query = entityManager.createQuery("SELECT t FROM TitoloViaggio t WHERE t.dataAcquisto > :date", TitoloViaggio.class);
        query.setParameter("date", LocalDate.now().minusDays(giorni));
        List<TitoloViaggio> result = query.getResultList();
        if (result.isEmpty()) throw new EmptyListException();
        return result;
    }

    public void obliteraBiglietto(String bigliettoId, VeicoloPubblico veicoloPubblico) {
        Biglietto biglietto1 = entityManager.find(Biglietto.class, UUID.fromString(bigliettoId));
        if (biglietto1 == null) throw new NotFoundException("biglietto", "id");
        if (biglietto1.isConvalidato()) throw new BigliettoGiaConvalidatoException();
        biglietto1.setConvalidato(true);
        biglietto1.setVeicoloPubblico(veicoloPubblico);
        biglietto1.setDataConvalidazione(LocalDate.now());
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(biglietto1);
        transaction.commit();
        System.out.println("Biglietto con descrizione '" + faker.commerce().productName() + " - " + faker.number().digits(5) + "' convalidato correttamente.");
    }

    public List<Biglietto> ottieniBigliettiObliteratiPerVeicolo(VeicoloPubblico veicoloPubblico) {
        TypedQuery<Biglietto> query = entityManager.createQuery("SELECT b FROM Biglietto b WHERE b.veicoloPubblico = :veicolo", Biglietto.class);
        query.setParameter("veicolo", veicoloPubblico);
        List<Biglietto> result = query.getResultList();
        if (result.isEmpty()) throw new EmptyListException();
        return result;
    }

    public List<Biglietto> ottientiBigliettiObliteratiPerPeriodo(int giorni) {
        TypedQuery<Biglietto> query = entityManager.createQuery("SELECT b FROM Biglietto b WHERE b.convalidato AND b.dataConvalidazione > :data", Biglietto.class);
        query.setParameter("data", LocalDate.now().minusDays(giorni));
        List<Biglietto> result = query.getResultList();
        if (result.isEmpty()) throw new EmptyListException();
        return result;
    }
}
