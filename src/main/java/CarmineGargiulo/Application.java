package CarmineGargiulo;

import CarmineGargiulo.dao.*;
import CarmineGargiulo.entities.*;
import CarmineGargiulo.enums.TipoManutenzione;
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
    private static final Faker faker = new Faker(Locale.ITALY);
    private static boolean bigliettiObliteratiInizializzati = false; // Flag per gestire l'inserimento di biglietti obliterati

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

        if (!DatabasePopolato(titoloViaggioDao, veicoloDAO)) {
            System.out.println("Inizializzazione del database con dati di esempio...");
            inizializzaDb(puntoVenditaDAO, tratteDao, utenteDao, tessereDAO, titoloViaggioDao, veicoloDAO, manutenzioneDao, servizioDao, em);
        } else {
            // System.out.println("Database già popolato. Inizializzazione saltata.");
        }

        MenuInterattivo menuInterattivo = new MenuInterattivo(
                puntoVenditaDAO, tratteDao, utenteDao, tessereDAO, titoloViaggioDao, veicoloDAO, manutenzioneDao, servizioDao
        );
        menuInterattivo.mostraMenu();

        em.close();
        emf.close();
    }

    public static void inizializzaDb(
            PuntoVenditaDAO puntoVenditaDAO,
            TratteDao tratteDao,
            UtenteDao utenteDao,
            TessereDAO tessereDAO,
            TitoloViaggioDao titoloViaggioDao,
            VeicoloDAO veicoloDAO,
            ManutenzioneDao manutenzioneDao,
            ServizioDao servizioDao,
            EntityManager em) {

        try {
            if (puntoVenditaDAO.ottieniListaPuntiVendita().isEmpty()) {
                for (int i = 0; i < 10; i++) {
                    boolean random = faker.random().nextBoolean();
                    if (random) {
                        RivenditoreAutorizzato rivenditoreAutorizzato = new RivenditoreAutorizzato(
                                faker.address().streetName(), faker.company().name());
                        puntoVenditaDAO.salvaPuntoVendita(rivenditoreAutorizzato);
                    } else {
                        Distributore distributore = new Distributore(
                                faker.address().streetName(), faker.random().nextBoolean());
                        puntoVenditaDAO.salvaPuntoVendita(distributore);
                    }
                }
            }

            List<Tratta> tratteEsistenti = tratteDao.ottieniListaTratte();
            if (tratteEsistenti.size() < 6) {
                int tratteDaAggiungere = 6 - tratteEsistenti.size();
                for (int i = 0; i < tratteDaAggiungere; i++) {
                    Tratta tratta = new Tratta(
                            "Linea " + (tratteEsistenti.size() + i + 1),
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
                            faker.number().numberBetween(1950, 2006)
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

            if (veicoloDAO.ottieniListaVeicoli().isEmpty()) {
                for (int i = 0; i < 6; i++) {
                    VeicoloPubblico veicoloPubblico = new VeicoloPubblico(
                            faker.bothify("??###??").toUpperCase(),
                            TipoVeicolo.TRAM
                    );
                    veicoloDAO.salvaVeicolo(veicoloPubblico);
                }
                for (int i = 0; i < 6; i++) {
                    VeicoloPubblico veicoloPubblico = new VeicoloPubblico(
                            faker.bothify("??###??").toUpperCase(),
                            TipoVeicolo.AUTOBUS
                    );
                    veicoloDAO.salvaVeicolo(veicoloPubblico);
                }
            }

            List<VeicoloPubblico> veicoli = veicoloDAO.ottieniListaVeicoli();
            List<Tratta> tratte = tratteDao.ottieniListaTratte();

            if (!veicoli.isEmpty()) {
                em.getTransaction().begin();

                for (VeicoloPubblico veicolo : veicoli) {
                    veicolo.setInServizio(false);
                    veicolo.setInManutenzione(false);
                }

                for (int i = 0; i < 5; i++) {
                    VeicoloPubblico veicolo = veicoli.get(i);
                    Tratta tratta = tratte.get(faker.random().nextInt(0, tratte.size()));
                    Servizio servizio = new Servizio(veicolo, tratta);
                    veicolo.setInServizio(true);
                    em.persist(servizio);
                }

                for (int i = 5; i < 9; i++) {
                    VeicoloPubblico veicolo = veicoli.get(i);
                    Manutenzione manutenzione = new Manutenzione(
                            TipoManutenzione.values()[faker.random().nextInt(0, TipoManutenzione.values().length)], veicolo);
                    veicolo.setInManutenzione(true);
                    em.persist(manutenzione);
                }

                em.getTransaction().commit();
            }

            if (!bigliettiObliteratiInizializzati) {
                if (!veicoli.isEmpty()) {
                    List<PuntoVendita> puntiVendita = puntoVenditaDAO.ottieniListaPuntiVendita();
                    for (int i = 0; i < 10; i++) {
                        PuntoVendita puntoVendita = puntiVendita.get(faker.random().nextInt(0, puntiVendita.size()));
                        VeicoloPubblico veicolo = veicoli.get(faker.random().nextInt(0, veicoli.size()));

                        Biglietto biglietto = new Biglietto(
                                Double.parseDouble(faker.commerce().price().replace(",", ".")),
                                LocalDate.now().minusDays(faker.random().nextInt(0, 150)),
                                puntoVendita
                        );
                        titoloViaggioDao.salvaTitoloViaggio(biglietto);

                        try {
                            titoloViaggioDao.obliteraBiglietto(biglietto.getTitoloViaggio_id().toString(), veicolo);
                        } catch (Exception e) {
                            System.out.println("Errore durante l'obliterazione del biglietto: " + e.getMessage());
                        }
                    }
                }
                bigliettiObliteratiInizializzati = true;
            }

            System.out.println("Inizializzazione completata con successo.");
        } catch (Exception e) {
            System.out.println("Errore durante l'inizializzazione del database: " + e.getMessage());
        }
    }

    public static boolean DatabasePopolato(TitoloViaggioDao titoloViaggioDao, VeicoloDAO veicoloDAO) {
        return !titoloViaggioDao.ottieniListaTitoliViaggio().isEmpty() && !veicoloDAO.ottieniListaVeicoli().isEmpty();
    }
}
