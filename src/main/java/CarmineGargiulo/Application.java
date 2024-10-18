package CarmineGargiulo;

import CarmineGargiulo.dao.*;
import CarmineGargiulo.entities.*;
import CarmineGargiulo.enums.TipoAbbonamento;
import CarmineGargiulo.enums.TipoVeicolo;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Application {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("azienda-trasporti");
    private static final Faker faker = new Faker(Locale.ITALY);

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
        
        inizializzaDbSeVuoto(puntoVenditaDAO, tratteDao, utenteDao, tessereDAO, titoloViaggioDao, veicoloDAO);

        MenuInterattivo menuInterattivo = new MenuInterattivo(
                puntoVenditaDAO, tratteDao, utenteDao, tessereDAO, titoloViaggioDao, veicoloDAO, manutenzioneDao, servizioDao
        );
        menuInterattivo.mostraMenu();

        em.close();
        emf.close();
    }

    public static void inizializzaDbSeVuoto(
            PuntoVenditaDAO puntoVenditaDAO,
            TratteDao tratteDao,
            UtenteDao utenteDao,
            TessereDAO tessereDAO,
            TitoloViaggioDao titoloViaggioDao,
            VeicoloDAO veicoloDAO) {

        try {
            if (titoloViaggioDao.ottieniListaTitoliViaggio().isEmpty()) {
                System.out.println("Inizializzazione del database con dati di esempio...");

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

                if (titoloViaggioDao.ottieniListaTitoliViaggio().isEmpty()) {
                    List<PuntoVendita> puntoVenditaList = puntoVenditaDAO.ottieniListaPuntiVendita();
                    List<Tessera> tesseraList = tessereDAO.ottieniListaTessere();
                    List<TipoAbbonamento> tipiList = Arrays.asList(TipoAbbonamento.values());

                    for (int i = 0; i < 30; i++) {
                        boolean random = faker.random().nextBoolean();
                        PuntoVendita puntoRandom;
                        while (true) {
                            puntoRandom = puntoVenditaList.get(faker.random().nextInt(0, puntoVenditaList.size() - 1));
                            if (puntoRandom instanceof RivenditoreAutorizzato) break;
                            else if (((Distributore) puntoRandom).isAttivo()) break;
                        }

                        if (random) {
                            Biglietto biglietto = new Biglietto(
                                    Double.parseDouble(faker.commerce().price().replace(",", ".")),
                                    LocalDate.of(faker.random().nextInt(2020, 2024),
                                            faker.random().nextInt(1, 12),
                                            faker.random().nextInt(1, 27)),
                                    puntoRandom
                            );
                            titoloViaggioDao.salvaTitoloViaggio(biglietto);
                        } else {
                            Abbonamento abbonamento = new Abbonamento(
                                    Double.parseDouble(faker.commerce().price().replace(",", ".")),
                                    LocalDate.of(faker.random().nextInt(2020, 2024),
                                            faker.random().nextInt(1, 12),
                                            faker.random().nextInt(1, 27)),
                                    puntoRandom,
                                    LocalDate.now(),
                                    tipiList.get(faker.random().nextInt(0, tipiList.size())),
                                    tesseraList.get(faker.random().nextInt(0, tesseraList.size()))
                            );
                            try {
                                titoloViaggioDao.salvaTitoloViaggio(abbonamento);
                            } catch (Exception e) {
                                System.out.println("Errore nell'acquisto dell'abbonamento: " + e.getMessage());
                            }
                        }
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
                List<PuntoVendita> puntiVendita = puntoVenditaDAO.ottieniListaPuntiVendita();

                if (!veicoli.isEmpty() && !puntiVendita.isEmpty()) {
                    for (int i = 0; i < 10; i++) {
                        PuntoVendita puntoVendita = puntiVendita.get(faker.random().nextInt(0, puntiVendita.size() - 1));
                        VeicoloPubblico veicolo = veicoli.get(faker.random().nextInt(0, veicoli.size() - 1));

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

            } else {
                // System.out.println("Database già popolato. Inizializzazione saltata.");
            }

        } catch (Exception e) {
            System.out.println("Errore durante l'inizializzazione del database: " + e.getMessage());
        }
    }
}
