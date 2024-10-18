package CarmineGargiulo;

import CarmineGargiulo.dao.*;
import CarmineGargiulo.entities.*;
import CarmineGargiulo.enums.TipoAbbonamento;
import CarmineGargiulo.enums.TipoManutenzione;
import CarmineGargiulo.enums.TipoVeicolo;
import CarmineGargiulo.exceptions.DistributoreFuoriServizioException;
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
    private static boolean bigliettiObliteratiInizializzati = false;

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
        MenuInterattivo menuInterattivo = new MenuInterattivo(em);
        inizializzaDb(puntoVenditaDAO, tratteDao, utenteDao, tessereDAO, titoloViaggioDao, veicoloDAO, manutenzioneDao, servizioDao, em);
        generaStoricoVeicoli(veicoloDAO, manutenzioneDao, servizioDao, tratteDao, puntoVenditaDAO);
        if(!puntoVenditaDAO.ottieniListaPuntiVendita().isEmpty()) menuInterattivo.avviaMenu();





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
                                tipiList.get(faker.random().nextInt(0, tipiList.size()-1)),
                                tesseraList.get(faker.random().nextInt(0, tesseraList.size()-1))
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

        if (servizioDao.controlloServiziAttivi().isEmpty()) {
            List<VeicoloPubblico> veicoli = veicoloDAO.ottieniListaVeicoli();
            List<Tratta> tratte = tratteDao.ottieniListaTratte();
            for (int i = 0; i < 6; i++) {
                servizioDao.mettiInServizio(veicoli.get(i), tratte.get(i));
            }
        }

            if (!bigliettiObliteratiInizializzati) {
                List<VeicoloPubblico> veicoli = veicoloDAO.ottieniListaVeicoli();
                if (!veicoli.isEmpty()) {
                    List<PuntoVendita> puntiVendita = puntoVenditaDAO.ottieniListaPuntiVendita();
                    for (int i = 0; i < 10; i++) {
                        PuntoVendita puntoVendita = puntiVendita.get(faker.random().nextInt(0, puntiVendita.size() - 1));
                        VeicoloPubblico veicolo2 = veicoli.get(faker.random().nextInt(0, veicoli.size() - 1));

                        Biglietto biglietto = new Biglietto(
                                Double.parseDouble(faker.commerce().price().replace(",", ".")),
                                LocalDate.now().minusDays(faker.random().nextInt(0, 150)),
                                puntoVendita
                        );

                        try {
                            titoloViaggioDao.salvaTitoloViaggio(biglietto);
                        } catch (DistributoreFuoriServizioException e) {
                            System.out.println(e.getMessage());
                        }


                        try {
                            titoloViaggioDao.obliteraBiglietto(biglietto.getTitoloViaggio_id().toString(), veicolo2);
                        } catch (Exception e) {
                            System.out.println("Errore durante l'obliterazione del biglietto: " + e.getMessage());
                        }
                    }
                }
                bigliettiObliteratiInizializzati = true;
            }
    }

    public static void generaStoricoVeicoli(
            VeicoloDAO veicoloDAO,
            ManutenzioneDao manutenzioneDao,
            ServizioDao servizioDao,
            TratteDao tratteDao, PuntoVenditaDAO puntoVenditaDAO) {

        List<VeicoloPubblico> veicoli = veicoloDAO.ottieniListaVeicoli();
        List<Tratta> tratte = tratteDao.ottieniListaTratte();

        if (tratte.isEmpty()) {
            System.out.println("Nessuna tratta disponibile per generare servizi. Aggiungi tratte nel database.");
            return;
        }

        int[] numeroManutenzioni = {4, 6, 9, 3, 9, 4, 2, 6, 7, 3, 3, 7};
        int[] numeroServizi = {5, 8, 5, 4, 7, 6, 5, 7, 8, 6, 3, 3};

        int index = 0;
        for (VeicoloPubblico veicolo : veicoli) {

            long manutenzioniPresenti = manutenzioneDao.contaManutenzioniPerVeicolo(veicolo);
            long serviziPresenti = servizioDao.contaServiziPerVeicolo(veicolo);

            boolean manutenzioniAggiornate = false;
            boolean serviziAggiornati = false;

            if (manutenzioniPresenti < numeroManutenzioni[index]) {
                int manutenzioniDaCreare = numeroManutenzioni[index] - (int) manutenzioniPresenti;
                for (int i = 0; i < manutenzioniDaCreare; i++) {
                    Manutenzione manutenzione = new Manutenzione(
                            LocalDate.now().minusMonths(faker.random().nextInt(1, 12)),
                            LocalDate.now().minusMonths(faker.random().nextInt(1, 6)),
                            TipoManutenzione.values()[faker.random().nextInt(TipoManutenzione.values().length)],
                            veicolo
                    );
                    manutenzioneDao.salvaManutenzione(manutenzione);
                }
                manutenzioniAggiornate = true;
            }


            if (serviziPresenti < numeroServizi[index]) {
                int serviziDaCreare = numeroServizi[index] - (int) serviziPresenti;
                Tratta trattaAssociata = tratte.get(index % tratte.size());
                for (int j = 0; j < serviziDaCreare; j++) {
                    Servizio servizio = new Servizio(
                            LocalDate.now().minusMonths(faker.random().nextInt(1, 12)),
                            LocalDate.now().minusMonths(faker.random().nextInt(1, 6)),
                            veicolo,
                            trattaAssociata
                    );
                    servizioDao.salvaServizio(servizio);
                }
                serviziAggiornati = true;
            }

            index++;
        }


    }

    public static boolean DatabasePopolato(TitoloViaggioDao titoloViaggioDao, VeicoloDAO veicoloDAO) {
        return !titoloViaggioDao.ottieniListaTitoliViaggio().isEmpty() && !veicoloDAO.ottieniListaVeicoli().isEmpty();
    }
}