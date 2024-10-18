package CarmineGargiulo;

import CarmineGargiulo.dao.*;
import CarmineGargiulo.entities.*;
import CarmineGargiulo.enums.TipoAbbonamento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuInterattivo {
    private final Scanner scanner = new Scanner(System.in);
    private final PuntoVenditaDAO puntoVenditaDAO;
    private final TratteDao tratteDao;
    private final UtenteDao utenteDao;
    private final TessereDAO tessereDAO;
    private final TitoloViaggioDao titoloViaggioDao;
    private final VeicoloDAO veicoloDAO;
    private final ManutenzioneDao manutenzioneDao;
    private final ServizioDao servizioDao;

    public MenuInterattivo(PuntoVenditaDAO puntoVenditaDAO, TratteDao tratteDao, UtenteDao utenteDao, TessereDAO tessereDAO,
                           TitoloViaggioDao titoloViaggioDao, VeicoloDAO veicoloDAO, ManutenzioneDao manutenzioneDao, ServizioDao servizioDao) {
        this.puntoVenditaDAO = puntoVenditaDAO;
        this.tratteDao = tratteDao;
        this.utenteDao = utenteDao;
        this.tessereDAO = tessereDAO;
        this.titoloViaggioDao = titoloViaggioDao;
        this.veicoloDAO = veicoloDAO;
        this.manutenzioneDao = manutenzioneDao;
        this.servizioDao = servizioDao;
    }

    public void mostraMenu() {
        System.out.println("Ciao, sei un utente registrato? (S/N)");
        String rispostaUtente = scanner.nextLine().trim().toUpperCase();

        if (rispostaUtente.equals("S")) {
            gestisciUtente();
        } else if (rispostaUtente.equals("N")) {
            System.out.println("Sei un amministratore? (S/N)");
            String rispostaAdmin = scanner.nextLine().trim().toUpperCase();
            if (rispostaAdmin.equals("S")) {
                menuAmministratore();
            } else {
                System.out.println("\nNon abbiamo altre alternative. Una buona giornata da parte dello Staff.");
            }
        } else {
            System.out.println("\nScelta non valida. Riavvia l'applicazione.");
        }
    }

    public void menuAmministratore() {
        while (true) {
            System.out.println("Che cosa vuoi fare?");
            System.out.println("1) Controllo biglietti/abbonamenti venduti");
            System.out.println("2) Controllo biglietti obliterati ");
            System.out.println("3) Controllare lista mezzi in manutenzione e in servizio");
            System.out.println("4) Controllare la lista delle linee scoperte da veicoli (S/N)");
            System.out.println("5) Assegnare un veicolo non ancora in servizio a una linea (S/N)");
            System.out.println("6) Scegliere se un veicolo deve essere in servizio o in manutenzione");
            System.out.println("0) Esci");

            int scelta = scanner.nextInt();
            scanner.nextLine(); // Consuma il newline

            switch (scelta) {
                case 1:
                    visualizzaVenditePuntiVendita();
                    break;
                case 2:
                    controlloBigliettiObliterati();
                    break;
                case 3:
                    sottoscelteControlloMezzi();
                    break;
                case 4:
                    // Codice per controllare la lista delle linee scoperte
                    break;
                case 5:
                    // Codice per assegnare un veicolo a una linea
                    break;
                case 6:
                    // Codice per gestione veicoli in servizio o manutenzione
                    break;
                case 0:
                    System.out.println("\nUscita dal sistema amministratore.");
                    return;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }

    private void sottoscelteControlloMezzi() {
        while (true) {
            System.out.println("\nSeleziona l'operazione che desideri effettuare:");
            System.out.println("1) Visionare quanti mezzi sono in servizio e in che linea");
            System.out.println("2) Controllare quanti mezzi sono in manutenzione");
            System.out.println("3) Controllare quanti mezzi sono fermi in azienda");
            System.out.println("0) Torna al menu principale");

            int scelta = scanner.nextInt();
            scanner.nextLine(); // Consuma il newline

            switch (scelta) {
                case 1:
                    mostraMezziInServizio();
                    break;
                case 2:
                    mostraMezziInManutenzione();
                    break;
                case 3:
                    mostraMezziFermiInAzienda();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }

    private void mostraMezziInServizio() {
        List<VeicoloPubblico> veicoli = veicoloDAO.ottieniListaVeicoli();

        System.out.println("\nVeicoli attualmente in servizio:");
        for (VeicoloPubblico veicolo : veicoli) {
            if (veicolo.isInServizio()) {
                Optional<Servizio> servizioAttivo = veicolo.getServiziList().stream()
                        .filter(servizio -> servizio.getDataFine() == null)
                        .findFirst();

                if (servizioAttivo.isPresent()) {
                    System.out.println("Veicolo: " + veicolo.getTarga() + " - Tratta: " + servizioAttivo.get().getTratta().getNomeTratta());
                } else {
                    System.out.println("Veicolo: " + veicolo.getTarga() + " - Servizio attivo non trovato.");
                }
            }
        }
    }

    private void mostraMezziInManutenzione() {
        List<VeicoloPubblico> veicoli = veicoloDAO.ottieniListaVeicoli();

        System.out.println("\nVeicoli attualmente in manutenzione:");
        for (VeicoloPubblico veicolo : veicoli) {
            if (veicolo.isInManutenzione()) {
                Optional<Manutenzione> manutenzioneAttiva = veicolo.getManutenzionList().stream()
                        .filter(manutenzione -> manutenzione.getDataFine() == null)
                        .findFirst();

                if (manutenzioneAttiva.isPresent()) {
                    System.out.println("\nVeicolo: " + veicolo.getTarga() + " - Tipo Manutenzione: " + manutenzioneAttiva.get().getTipoManutenzione());
                } else {
                    System.out.println("\nVeicolo: " + veicolo.getTarga() + " - Manutenzione attiva non trovata.");
                }
            }
        }
    }

    private void mostraMezziFermiInAzienda() {
        List<VeicoloPubblico> veicoli = veicoloDAO.ottieniListaVeicoli();

        System.out.println("\nVeicoli fermi in azienda (non in servizio o manutenzione):");
        for (VeicoloPubblico veicolo : veicoli) {
            if (!veicolo.isInServizio() && !veicolo.isInManutenzione()) {
                System.out.println("\nVeicolo: " + veicolo.getTarga());
            }
        }
    }

    private void visualizzaVenditePuntiVendita() {
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
            System.out.println("Biglietti venduti: " +
                    titoloViaggioDao.getAllTitoliViaggioPerPuntoVendita(puntoVendita).stream().filter(t -> t instanceof Biglietto).count());
            System.out.println("Abbonamenti venduti: " +
                    titoloViaggioDao.getAllTitoliViaggioPerPuntoVendita(puntoVendita).stream().filter(t -> t instanceof Abbonamento).count());
        } else {
            System.out.println("\nScelta non valida.");
        }
    }

    private void controlloBigliettiObliterati() {
        System.out.println("\nVuoi controllare i biglietti obliterati? (S/N)");
        String risposta = scanner.nextLine().trim().toUpperCase();

        if (risposta.equals("S")) {
            while (true) {
                System.out.println("1) Controllo biglietti obliterati per Periodo");
                System.out.println("2) Controllo biglietti obliterati per Veicolo");
                System.out.println("0) Torna al menu principale");

                int scelta = scanner.nextInt();
                scanner.nextLine();

                switch (scelta) {
                    case 1:
                        controlloBigliettiObliteratiPerPeriodo();
                        break;
                    case 2:
                        controlloBigliettiObliteratiPerVeicolo();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Scelta non valida.");
                }
            }
        } else if (risposta.equals("N")) {
            System.out.println("Tornando al menu principale...");
        } else {
            System.out.println("Scelta non valida. Inserisci 'S' o 'N'.");
            controlloBigliettiObliterati();
        }
    }

    private void controlloBigliettiObliteratiPerPeriodo() {
        System.out.println("\nInserisci il numero di giorni per cui vuoi controllare i biglietti obliterati:");
        int giorni = scanner.nextInt();

        try {
            List<Biglietto> bigliettiObliterati = titoloViaggioDao.ottientiBigliettiObliteratiPerPeriodo(giorni);
            if (bigliettiObliterati.isEmpty()) {
                System.out.println("\nNessun biglietto obliterato negli ultimi " + giorni + " giorni.");
            } else {
                System.out.println("\nSono stati obliterati " + bigliettiObliterati.size() + " biglietti negli ultimi " + giorni + " giorni.");
            }
        } catch (Exception e) {
            System.out.println("\nNessun biglietto obliterato negli ultimi " + giorni + " giorni.");
        }
    }

    private void controlloBigliettiObliteratiPerVeicolo() {
        List<VeicoloPubblico> veicoli = veicoloDAO.ottieniListaVeicoli();
        if (veicoli.isEmpty()) {
            System.out.println("\nNon ci sono veicoli disponibili.");
            return;
        }

        System.out.println("\nSeleziona un veicolo per visualizzare i biglietti obliterati:");
        for (int i = 0; i < veicoli.size(); i++) {
            System.out.println((i + 1) + ". " + veicoli.get(i).getTarga());
        }

        int scelta = scanner.nextInt();

        if (scelta > 0 && scelta <= veicoli.size()) {
            VeicoloPubblico veicoloScelto = veicoli.get(scelta - 1);
            List<Biglietto> bigliettiObliterati = titoloViaggioDao.ottieniBigliettiObliteratiPerVeicolo(veicoloScelto);

            if (bigliettiObliterati.isEmpty()) {
                System.out.println("\nNessun biglietto obliterato per il veicolo con targa " + veicoloScelto.getTarga());
            } else {
                System.out.println("\nSono stati obliterati " + bigliettiObliterati.size() + " biglietti per il veicolo con targa " + veicoloScelto.getTarga());
            }
        } else {
            System.out.println("Scelta non valida.");
        }
    }

    private void gestisciUtente() {
        boolean sceltaValida = false;

        while (!sceltaValida) {
            System.out.println("Cosa vuoi fare?");
            System.out.println("1. Compra biglietto");
            System.out.println("2. Compra abbonamento");
            System.out.println("3. Scegli tratta");

            try {
                int scelta = scanner.nextInt();
                scanner.nextLine();

                switch (scelta) {
                    case 1:
                        compraBiglietto();
                        sceltaValida = true;
                        break;
                    case 2:
                        compraAbbonamento();
                        sceltaValida = true;
                        break;
                    case 3:
                        scegliTratta();
                        sceltaValida = true;
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            } catch (Exception e) {
                System.out.println("Inserisci un numero valido.");
                scanner.nextLine();
            }
        }
    }

    private void compraBiglietto() {
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
            System.out.println("\nScelta non valida.");
        }
    }

    private void compraAbbonamento() {
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
            System.out.println("\nScelta non valida.");
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

        System.out.println("L'abbonamento " + tipoAbbonamento + " costa: " + prezzoAbbonamento + " EUR.");
        System.out.println("Confermi l'acquisto? (S/N)");
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

    private void scegliTratta() {
        System.out.println("\nSelezione della tratta:");
        List<Tratta> tratte = tratteDao.ottieniListaTratte();

        if (tratte.isEmpty()) {
            System.out.println("\nNessuna tratta disponibile.");
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
            System.out.println("\nBuon viaggio nella tratta " + trattaSelezionata.getNomeTratta());
        } else {
            System.out.println("\nScelta non valida.");
        }
    }
}
