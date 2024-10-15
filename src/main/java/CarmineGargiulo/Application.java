package CarmineGargiulo;

import CarmineGargiulo.dao.*;
import CarmineGargiulo.entities.*;
import CarmineGargiulo.enums.TipoVeicolo;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class Application {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("azienda-trasporti");
    private static final Faker faker = new Faker(Locale.ITALY); // Per generare dati fittiz

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        PuntoVenditaDAO puntoVenditaDAO = new PuntoVenditaDAO(em);
        TratteDao tratteDao = new TratteDao(em);
        UtenteDao utenteDao = new UtenteDao(em);
        TessereDAO tessereDAO = new TessereDAO(em);
        TitoloViaggioDao titoloViaggioDao = new TitoloViaggioDao(em);
        VeicoloDAO veicoloDAO = new VeicoloDAO(em);
        ManutenzioneDao manutenzioneDao = new ManutenzioneDao(em);
        ServizioDao servizioDao = new ServizioDao(em);

        inizializzaDb(puntoVenditaDAO, tratteDao, utenteDao, tessereDAO, titoloViaggioDao, veicoloDAO);

        try {
            salvaVeicoliInManutenzioneEServizio(veicoloDAO, manutenzioneDao, servizioDao);
            listaVeicoliManutenzioneEServizio(veicoloDAO);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        em.close();
        emf.close();
    }

    public static void inizializzaDb(
            PuntoVenditaDAO puntoVenditaDAO, TratteDao tratteDao, UtenteDao utenteDao,
            TessereDAO tessereDAO, TitoloViaggioDao titoloViaggioDao, VeicoloDAO veicoloDAO) {

        if (puntoVenditaDAO.ottieniListaPuntiVendita().isEmpty()) {
            for (int i = 0; i < 10; i++) {
                boolean random = faker.random().nextBoolean();
                if (random) {
                    RivenditoreAutorizzato rivenditoreAutorizzato =
                            new RivenditoreAutorizzato(faker.address().streetName(), faker.company().name());
                    puntoVenditaDAO.salvaPuntoVendita(rivenditoreAutorizzato);
                } else {
                    Distributore distributore =
                            new Distributore(faker.address().streetName(), faker.random().nextBoolean());
                    puntoVenditaDAO.salvaPuntoVendita(distributore);
                }
            }
        }

        if (tratteDao.ottieniListaTratte().isEmpty()) {
            for (int i = 0; i < 5; i++) {
                Tratta tratta = new Tratta(
                        "Linea " + (i + 1),
                        faker.address().cityName(),
                        faker.address().cityName(),
                        faker.number().numberBetween(20, 60)
                );
                tratteDao.saveTratta(tratta);
            }
        }

        if (utenteDao.ottieniListaUtenti().isEmpty()) {
            for (int i = 0; i < 5; i++) {
                Utente utente = new Utente(
                        faker.name().fullName(),
                        faker.random().nextInt(1950, 2006)
                );
                utenteDao.salvaUtenteDao(utente);
            }
        }

        if (tessereDAO.ottieniListaTessere().isEmpty()) {
            List<Utente> utentiList = utenteDao.ottieniListaUtenti();
            for (Utente utente : utentiList) {
                Tessera tessera = new Tessera(LocalDate.now(), utente);
                tessereDAO.salvaTessera(tessera);
            }
        }

        List<VeicoloPubblico> veicoliEsistenti = veicoloDAO.ottieniListaVeicoli();
        int veicoliDaAggiungere = 20 - veicoliEsistenti.size();

        if (veicoliDaAggiungere > 0) {
            for (int i = 0; i < veicoliDaAggiungere; i++) {
                VeicoloPubblico veicolo = new VeicoloPubblico(
                        faker.bothify("??###??").toUpperCase(),
                        i % 2 == 0 ? TipoVeicolo.TRAM : TipoVeicolo.AUTOBUS
                );
                veicoloDAO.salvaVeicolo(veicolo);
            }
        }
    }

    public static void salvaVeicoliInManutenzioneEServizio(
            VeicoloDAO veicoloDAO, ManutenzioneDao manutenzioneDao, ServizioDao servizioDao) {

        List<VeicoloPubblico> veicoli = veicoloDAO.ottieniListaVeicoli();

        if (veicoli.size() < 20) {
            throw new RuntimeException("Non ci sono abbastanza veicoli per mettere 10 in manutenzione e 10 in servizio.");
        }

        for (int i = 0; i < 10; i++) {
            VeicoloPubblico veicolo = veicoli.get(i);
            Manutenzione manutenzione = new Manutenzione(LocalDate.now(), LocalDate.now().plusDays(7), null, veicolo);
            manutenzioneDao.salvaManutenzione(manutenzione);
        }

        for (int i = 10; i < 20; i++) {
            VeicoloPubblico veicolo = veicoli.get(i);
            Servizio servizio = new Servizio(LocalDate.now(), LocalDate.now().plusDays(7), veicolo, null);
            servizioDao.salvaServizio(servizio);
        }
    }

    public static void listaVeicoliManutenzioneEServizio(VeicoloDAO veicoloDAO) {
        List<VeicoloPubblico> veicoliInManutenzione = veicoloDAO.ottieniVeicoliInManutenzione();
        System.out.println("\nVeicoli in manutenzione:\n");
        for (VeicoloPubblico veicolo : veicoliInManutenzione) {
            System.out.println(veicolo.getTarga());
        }

        List<VeicoloPubblico> veicoliInServizio = veicoloDAO.ottieniVeicoliInServizio();
        System.out.println("\nVeicoli in servizio:\n");
        for (VeicoloPubblico veicolo : veicoliInServizio) {
            System.out.println(veicolo.getTarga());
        }
    }
}
