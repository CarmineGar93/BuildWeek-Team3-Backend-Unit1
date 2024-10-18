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

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ciao, sei un utente registrato? (S/N)");
        String rispostaUtente = scanner.nextLine().trim().toUpperCase();

        if (rispostaUtente.equals("S")) {
            gestisciUtente(scanner, titoloViaggioDao, puntoVenditaDAO, tratteDao, tessereDAO);
        } else if (rispostaUtente.equals("N")) {
            System.out.println("Sei un amministratore? (S/N)");
            String rispostaAdmin = scanner.nextLine().trim().toUpperCase();
            if (rispostaAdmin.equals("S")) {
                gestisciAdmin(scanner, puntoVenditaDAO, titoloViaggioDao);
            } else {
                System.out.println("\nNon abbiamo altre alternative. Una buona giornata da parte dello Staff.");
            }
        } else {
            System.out.println("\nScelta non valida. Riavvia l'applicazione.");
        }

        em.close();
        emf.close();
    }

    private static void gestisciUtente(Scanner scanner, TitoloViaggioDao titoloViaggioDao, PuntoVenditaDAO puntoVenditaDAO, TratteDao tratteDao, TessereDAO tessereDAO) {
        boolean sceltaValida = false;

        while (!sceltaValida) {
            System.out.println("Cosa vuoi fare?");
            System.out.println("1. Compra biglietto");
            System.out.println("2. Compra abbonamento");
            System.out.println("3. Scegli tratta");

            try {
                int scelta = scanner.nextInt();

                switch (scelta) {
                    case 1:
                        compraBiglietto(scanner, titoloViaggioDao, puntoVenditaDAO);
                        sceltaValida = true;
                        break;
                    case 2:
                        compraAbbonamento(scanner, titoloViaggioDao, puntoVenditaDAO, tessereDAO);
                        sceltaValida = true;
                        break;
                    case 3:
                        scegliTratta(scanner, tratteDao);
                        sceltaValida = true;
                        break;
                    default:
                        System.out.println("Scelta non valida. Scegli un'opzione tra quelle disponibili.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Inserisci un numero valido.");
                scanner.nextLine();
            }
        }
    }

    private static void gestisciAdmin(Scanner scanner, PuntoVenditaDAO puntoVenditaDAO, TitoloViaggioDao titoloViaggioDao) {
        boolean sceltaValida = false;

        while (!sceltaValida) {
            System.out.println("Cosa vuoi fare?");
            System.out.println("1. Controllare vendite biglietti e abbonamenti per punti vendita");

            System.out.println("0. Esci");

            try {
                int scelta = scanner.nextInt();
                scanner.nextLine();

                switch (scelta) {
                    case 1:
                        visualizzaVenditePuntiVendita(puntoVenditaDAO, scanner, titoloViaggioDao);
                        sceltaValida = true;
                        break;
                    case 2:
                        System.out.println("Uscita dal sistema amministratore.");
                        sceltaValida = true;
                        break;
                    default:
                        System.out.println("Scelta non valida. Scegli un'opzione tra quelle disponibili.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Inserisci un numero valido.");
                scanner.nextLine();
            }
        }
    }

    private static void visualizzaVenditePuntiVendita(PuntoVenditaDAO puntoVenditaDAO, Scanner scanner, TitoloViaggioDao titoloViaggioDao) {
        List<PuntoVendita> puntiVendita = puntoVenditaDAO.ottieniListaPuntiVendita();

        if (puntiVendita.isEmpty()) {
            System.out.println("\nNessun punto vendita disponibile.");
            return;
        }

        System.out.println("\nSeleziona un punto vendita dall'elenco:");
        for (int i = 0; i < puntiVendita.size(); i++) {
            System.out.println((i + 1) + ". " + puntiVendita.get(i).getIndirizzo());
        }

        System.out.println("\nScegli il numero del punto vendita:");
        int scelta = scanner.nextInt();
        scanner.nextLine();


        if (scelta > 0 && scelta <= puntiVendita.size()) {
            PuntoVendita puntoVendita = puntiVendita.get(scelta - 1);
            System.out.println("\nPunto Vendita: " + puntoVendita.getIndirizzo());
            System.out.println("Biglietti venduti: " + titoloViaggioDao.getAllTitoliViaggioPerPuntoVendita(puntoVendita).stream().filter(titoloViaggio -> titoloViaggio instanceof Biglietto).toList().size());
            System.out.println("Abbonamenti venduti: " + titoloViaggioDao.getAllTitoliViaggioPerPuntoVendita(puntoVendita).stream().filter(titoloViaggio -> titoloViaggio instanceof Abbonamento).toList().size());
        } else {
            System.out.println("\nScelta non valida.");
        }
    }

    private static void compraBiglietto(Scanner scanner, TitoloViaggioDao titoloViaggioDao, PuntoVenditaDAO puntoVenditaDAO) {
        List<PuntoVendita> puntiVendita = puntoVenditaDAO.ottieniListaPuntiVendita();
        if (puntiVendita.isEmpty()) {
            System.out.println("\nNessun punto vendita disponibile.");
            return;
        }

        System.out.println("\nSeleziona un punto vendita dall'elenco:");
        for (int i = 0; i < puntiVendita.size(); i++) {
            System.out.println((i + 1) + ". " + puntiVendita.get(i).getIndirizzo());
        }

        System.out.println("\nScegli il numero del punto vendita:");
        int scelta = scanner.nextInt();
        scanner.nextLine();

        if (scelta > 0 && scelta <= puntiVendita.size()) {
            PuntoVendita puntoVendita = puntiVendita.get(scelta - 1);
            System.out.println("Hai scelto il punto vendita: " + puntoVendita.getIndirizzo());

            Biglietto biglietto = new Biglietto(2.5, LocalDate.now(), puntoVendita);
            titoloViaggioDao.salvaTitoloViaggio(biglietto);

            System.out.println("\nBiglietto acquistato con successo presso " + puntoVendita.getIndirizzo() + "!");
        } else {
            System.out.println("\nScelta non valida. Per favore, seleziona un numero tra quelli disponibili.");
        }
    }

    private static void compraAbbonamento(Scanner scanner, TitoloViaggioDao titoloViaggioDao, PuntoVenditaDAO puntoVenditaDAO, TessereDAO tessereDAO) {
        System.out.println("Acquisto abbonamento:");

        List<PuntoVendita> puntiVendita = puntoVenditaDAO.ottieniListaPuntiVendita();
        if (puntiVendita.isEmpty()) {
            System.out.println("\nNessun punto vendita disponibile.");
            return;
        }

        System.out.println("\nSeleziona un punto vendita dall'elenco:");
        for (int i = 0; i < puntiVendita.size(); i++) {
            System.out.println((i + 1) + ". " + puntiVendita.get(i).getIndirizzo());
        }

        System.out.println("\nScegli il numero del punto vendita:");
        int sceltaPuntoVendita = scanner.nextInt();
        scanner.nextLine();

        if (sceltaPuntoVendita <= 0 || sceltaPuntoVendita > puntiVendita.size()) {
            System.out.println("\nScelta non valida. Per favore, seleziona un numero tra quelli disponibili.");
            return;
        }

        PuntoVendita puntoVendita = puntiVendita.get(sceltaPuntoVendita - 1);
        System.out.println("\nHai scelto il punto vendita: " + puntoVendita.getIndirizzo());

        System.out.println("Seleziona il tipo di abbonamento:");
        System.out.println("1. SETTIMANALE ");
        System.out.println("2. MENSILE ");
        System.out.println("3. SEMESTRALE ");
        System.out.println("4. ANNUALE ");

        int sceltaTipoAbbonamento = scanner.nextInt();
        scanner.nextLine();

        TipoAbbonamento tipoAbbonamento;
        double prezzoAbbonamento;

        switch (sceltaTipoAbbonamento) {
            case 1:
                tipoAbbonamento = TipoAbbonamento.SETTIMANALE;
                prezzoAbbonamento = 20.0;
                break;
            case 2:
                tipoAbbonamento = TipoAbbonamento.MENSILE;
                prezzoAbbonamento = 50.0;
                break;
            case 3:
                tipoAbbonamento = TipoAbbonamento.SEMESTRALE;
                prezzoAbbonamento = 300.0;
                break;
            case 4:
                tipoAbbonamento = TipoAbbonamento.ANNUALE;
                prezzoAbbonamento = 600.0;
                break;
            default:
                System.out.println("Scelta non valida.");
                return;
        }

        System.out.println("L'abbonamento " + tipoAbbonamento + " viene: " + prezzoAbbonamento + " EUR.");
        System.out.println("Conferma l'acquisto? (S/N)");
        String conferma = scanner.nextLine().trim().toUpperCase();

        if (conferma.equals("S")) {
            Abbonamento abbonamento = new Abbonamento(prezzoAbbonamento, LocalDate.now(), puntoVendita, LocalDate.now(), tipoAbbonamento, null);

            try {
                titoloViaggioDao.salvaTitoloViaggio(abbonamento);
                System.out.println("Abbonamento '" + tipoAbbonamento + "' acquistato con successo presso " + puntoVendita.getIndirizzo() + "!");
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        } else {
            System.out.println("Non hai acquistato questo abbonamento.");
        }
    }

    private static void scegliTratta(Scanner scanner, TratteDao tratteDao) {
        System.out.println("\nSelezione della tratta:");
        List<Tratta> tratte = tratteDao.ottieniListaTratte();

        if (tratte.isEmpty()) {
            System.out.println("\nFattela a piedi che non ci sono tratte disponibili.");
            return;
        }

        for (int i = 0; i < tratte.size(); i++) {
            System.out.println((i + 1) + ". " + tratte.get(i).getNomeTratta());
        }

        System.out.println("\nScegli il numero della tratta:");
        int scelta = scanner.nextInt();
        scanner.nextLine();

        if (scelta > 0 && scelta <= tratte.size()) {
            Tratta trattaSelezionata = tratte.get(scelta - 1);
            System.out.println("\nLe auguriamo un buon viaggio nella " + trattaSelezionata.getNomeTratta());
        } else {
            System.out.println("\nScelta non valida.");
        }
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
                //System.out.println("Lo storico per il veicolo con targa " + veicolo.getTarga() + " è già stato salvato.");
            } else {
                System.out.println("Le nuove manutenzioni e servizi per il veicolo con targa " + veicolo.getTarga() + " sono stati salvati.");
            }

            index++;
        }

        //System.out.println("\nControllo e generazione dei dati di manutenzione e servizi completato.");

        if (servizioDao.controlloServiziAttivi().isEmpty()) {
            for (int i = 0; i < 6; i++) {
                servizioDao.mettiInServizio(veicoli.get(i), tratte.get(i));
            }
        }
    }
}