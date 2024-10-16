package CarmineGargiulo;

import CarmineGargiulo.dao.*;
import CarmineGargiulo.entities.*;
import CarmineGargiulo.enums.TipoAbbonamento;
import CarmineGargiulo.enums.TipoManutenzione;
import CarmineGargiulo.enums.TipoVeicolo;
import CarmineGargiulo.exceptions.AbbonamentoDateException;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.*;

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

        inizializzaDb(puntoVenditaDAO, tratteDao, utenteDao, tessereDAO, titoloViaggioDao, veicoloDAO);

        generaStoricoVeicoli(veicoloDAO, manutenzioneDao, servizioDao, tratteDao);

        em.close();
        emf.close();
    }



    public static void inizializzaDb(
            PuntoVenditaDAO puntoVenditaDAO,
            TratteDao tratteDao,
            UtenteDao utenteDao,
            TessereDAO tessereDAO,
            TitoloViaggioDao titoloViaggioDao,
            VeicoloDAO veicoloDAO) {

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

        if (titoloViaggioDao.ottieniListaTitoliViaggio().isEmpty()) {
            List<PuntoVendita> puntoVenditaList = puntoVenditaDAO.ottieniListaPuntiVendita();
            List<Tessera> tesseraList = tessereDAO.ottieniListaTessere();
            List<TipoAbbonamento> tipiList = Arrays.asList(TipoAbbonamento.values());

            for (int i = 0; i < 10; i++) {
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
                            tipiList.get(faker.random().nextInt(0, tipiList.size() - 1)),
                            tesseraList.get(faker.random().nextInt(0, tesseraList.size() - 1))
                    );
                    try {
                        titoloViaggioDao.salvaTitoloViaggio(abbonamento);
                    } catch (AbbonamentoDateException e) {
                        System.out.println(e.getMessage());
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
    }

    public static void generaStoricoVeicoli(
            VeicoloDAO veicoloDAO,
            ManutenzioneDao manutenzioneDao,
            ServizioDao servizioDao,
            TratteDao tratteDao) {

        List<VeicoloPubblico> veicoli = veicoloDAO.ottieniListaVeicoli();
        List<Tratta> tratte = tratteDao.ottieniListaTratte();
        Set<String> nomiGenerati = new HashSet<>();

        if (tratte.isEmpty()) {
            System.out.println("Nessuna tratta disponibile per generare servizi. Aggiungi tratte nel database.");
            return;
        }

        int[] numeroManutenzioni = {4, 6, 9, 3, 9, 4, 2, 6, 7, 3, 3, 7};
        int[] numeroServizi = {5, 8, 5, 4, 7, 6, 5, 7, 8, 6, 3, 3};

        int index = 0;
        for (VeicoloPubblico veicolo : veicoli) {
            String nomeVeicolo;
            do {
                nomeVeicolo = faker.ancient().god();
            } while (!nomiGenerati.add(nomeVeicolo));

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

            if (!manutenzioniAggiornate && !serviziAggiornati) {
                System.out.println("Lo storico per il veicolo con targa " + veicolo.getTarga() + " è già stato salvato.");
            } else {
                System.out.println("Le nuove manutenzioni e servizi per il veicolo con targa " + veicolo.getTarga() + " sono stati salvati.");
            }

            index++;
        }

        System.out.println("\nControllo e generazione dei dati di manutenzione e servizi completato.");

        for (VeicoloPubblico veicolo : veicoli) {

            List<Manutenzione> manutenzioni = manutenzioneDao.ottieniListaManutenzioni(veicolo);

            if (manutenzioni.isEmpty()) {
                System.out.println("   ");
                System.out.println("Non è presente nessuna manutenzione per il veicolo: " + veicolo.getVeicoloId());
            } else {

                System.out.println("   ");
                System.out.println("Manutenzioni per il veicolo: " + veicolo.getVeicoloId());
                for (Manutenzione manutenzione : manutenzioni) {
                    System.out.println(manutenzione.getTipoManutenzione() + " dal "
                            + manutenzione.getDataInizio() + " al " + manutenzione.getDataFine());
                }
            }
        }
    }





}