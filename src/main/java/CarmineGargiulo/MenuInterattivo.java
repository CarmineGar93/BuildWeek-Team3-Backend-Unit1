package CarmineGargiulo;

import CarmineGargiulo.dao.*;
import CarmineGargiulo.entities.*;
import CarmineGargiulo.enums.TipoAbbonamento;
import CarmineGargiulo.exceptions.*;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuInterattivo {
    private final EntityManager em;
    private final Scanner scanner = new Scanner(System.in);
    private final PuntoVenditaDAO puntoVenditaDAO;
    private final TratteDao tratteDao;
    private final UtenteDao utenteDao;
    private final TessereDAO tessereDAO;
    private final TitoloViaggioDao titoloViaggioDao;
    private final VeicoloDAO veicoloDAO;
    private final ManutenzioneDao manutenzioneDao;
    private final ServizioDao servizioDao;
    public MenuInterattivo(EntityManager entityManager) {
        this.em = entityManager;
        this.puntoVenditaDAO = new PuntoVenditaDAO(entityManager);
        this.titoloViaggioDao = new TitoloViaggioDao(entityManager);
        this.utenteDao = new UtenteDao(entityManager);
        this.tessereDAO = new TessereDAO(entityManager);
        this.veicoloDAO = new VeicoloDAO(entityManager);
        this.tratteDao = new TratteDao(entityManager);
        this.manutenzioneDao = new ManutenzioneDao(entityManager);
        this.servizioDao = new ServizioDao(entityManager);
    }
    private void menuAmministratore(){
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

    private PuntoVendita selezionaPuntoVendita(){
        List<PuntoVendita> puntiVendita = puntoVenditaDAO.ottieniListaPuntiVendita();
        if (puntiVendita.isEmpty()) {
            System.out.println("Nessun punto vendita disponibile");
        }
        System.out.println("Seleziona un punto vendita dall'elenco:");
        for (int i = 0; i < puntiVendita.size(); i++) {
            System.out.println((i + 1) + ") " + puntiVendita.get(i).getIndirizzo());
        }
        System.out.println("Scegli il numero del punto vendita:");
        int scelta;
        while (true){
            scelta = verifyInput();
            if (scelta <= 0 || scelta > puntiVendita.size()) System.out.println("Devi inserire un numero tra 1 e " + (puntiVendita.size()));
            else break;
        }
        PuntoVendita puntoVendita = puntiVendita.get(scelta - 1);
        System.out.println("Hai scelto il punto vendita: " + puntoVendita.getIndirizzo());
        return puntoVendita;
    }

    public void avviaMenu(){
        System.out.println("Benvenuto nell'azienda di trasporti numero uno al mondo");
        outerLoop: while(true) {
            System.out.println("Vuoi accedere al menu utente o amministratore?");
            System.out.println("1) Utente ");
            System.out.println("2) Amministratore");
            System.out.println("0) Esci");
            int scelta;
            while (true){
                scelta = verifyInput();
                if (scelta < 0 || scelta > 2) System.out.println("Devi inserire un numero tra zero e 2");
                else break;
            }
            switch (scelta){
                case 0 -> {
                    System.out.println("A presto");
                    break outerLoop;
                }
                case 1 -> menuUtente();
                case 2 -> menuAmministratore();
            }
        }

    }

    private void menuUtente(){
        Tessera tessera = null;
        System.out.println("Hai una tessera della nostra azienda? ");
        System.out.println("1) SI");
        System.out.println("2) NO");
        int scelta;
        while (true){
            scelta = verifyInput();
            if (scelta <= 0 || scelta > 2) System.out.println("Devi inserire 1 o 2");
            else break;
        }
        if(scelta == 1) {
            while (true) {
                System.out.println("Inserisci l'id dell tessera");
                String tesseraId = scanner.nextLine();
                try {
                    tessera = tessereDAO.findTesseraById(tesseraId);
                    break;
                } catch (NotFoundException e){
                    System.out.println(e.getMessage());
                } catch (IllegalArgumentException e){
                    System.out.println("L'id fornito non è valido");
                }
            }
        }
        outerLoop2: while (true) {
            System.out.println("Cosa vuoi fare?");
            System.out.println("1) Compra biglietto");
            System.out.println("2) Compra abbonamento");
            System.out.println("3) Compra tessera");
            System.out.println("4) Prendi veicolo pubblico");
            System.out.println("0) Esci");
            int scelta2;
            while (true){
                scelta2 = verifyInput();
                if (scelta2 < 0 || scelta2 > 4) System.out.println("Devi inserire un numero tra 0 e 4");
                else break;
            }
            switch (scelta2){
                case 0 -> {
                    System.out.println("Logout da utente");
                    break outerLoop2;
                }
                case 1 -> compraBiglietto();
                case 2 -> {
                    if (tessera == null) System.out.println("Non hai fornito una tessera");
                    else compraAbbonamento(tessera);
                }
                case 3 -> {
                    if(tessera != null) System.out.println("Hai già fornito una tessera");
                    else tessera = compraTessera();
                }
                case 4 -> prendiVeicoloPubblico(tessera);
            }
        }
    }

    private void prendiVeicoloPubblico(Tessera tessera){
        System.out.println("Seleziona la tratta: ");
        List<Tratta> tratte = tratteDao.ottieniListaTratte();
        if (tratte.isEmpty()) {
            System.out.println("Fattela a piedi che non ci sono tratte disponibili.");
            return;
        }
        for (int i = 0; i < tratte.size(); i++) {
            System.out.println((i + 1) + ") " + tratte.get(i).getNomeTratta());
        }
        System.out.println("Scegli il numero della tratta: ");
        int scelta;
        while (true){
            scelta = verifyInput();
            if (scelta < 0 || scelta > tratte.size()) System.out.println("Devi inserire un numero tra 1 e " + (tratte.size()));
            else break;
        }
        Tratta trattaSelezionata = tratte.get(scelta - 1);
        VeicoloPubblico veicoloPubblico = null;
        if(trattaSelezionata.getServiziList().stream().noneMatch(servizio -> servizio.getDataFine() == null))
            System.out.println("Mi dispiace al momento la tratta è scoperta");
        else {
            Optional<Servizio> servizioCercato = trattaSelezionata.getServiziList().stream().filter(servizio -> servizio.getDataFine() == null).findFirst();
            if(servizioCercato.isPresent()) {
                veicoloPubblico = servizioCercato.get().getVeicoloPubblico();
            }
            if (tessera == null) {
                while (true){
                    System.out.println("Inserisci l'id del tuo biglietto");
                    String bigliettoId = scanner.nextLine();
                    try {
                        titoloViaggioDao.obliteraBiglietto(bigliettoId, veicoloPubblico);
                        System.out.println("Buon viaggio");
                        break;
                    } catch (IllegalArgumentException e){
                        System.out.println("L'id fornito non è valido");
                    } catch (NotFoundException e) {
                        System.out.println(e.getMessage());
                    } catch (BigliettoGiaConvalidatoException e){
                        System.out.println(e.getMessage());
                        break;
                    }
                }
            } else {
                if (tessereDAO.verificaValiditaAbbonamento(tessera)) System.out.println("Buon viaggio");
                else System.out.println("Impossibile salire a bordo, non hai un abbonamento valido");
            }
        }

    }

    private Tessera compraTessera(){
        System.out.println("Inserisci nome e cognome");
        String nominativo = scanner.nextLine();
        System.out.println("Inserisci anno di nascita");
        int anno = verifyInput();
        Utente utente = new Utente(nominativo, anno);
        utenteDao.salvaUtenteDao(utente);
        Tessera tessera = new Tessera(LocalDate.now(), utente);
        tessereDAO.salvaTessera(tessera);
        return tessera;
    }

    private void compraBiglietto(){
        PuntoVendita puntoVendita = selezionaPuntoVendita();
        Biglietto biglietto = new Biglietto(2.5, LocalDate.now(), puntoVendita);
        try {
            titoloViaggioDao.salvaTitoloViaggio(biglietto);
        } catch (DistributoreFuoriServizioException e){
            System.out.println(e.getMessage());
        }

    }

    private void compraAbbonamento(Tessera tessera){
        PuntoVendita puntoVendita = selezionaPuntoVendita();
        System.out.println("Seleziona il tipo di abbonamento:");
        System.out.println("1) Settimanale ");
        System.out.println("2) Mensile ");
        System.out.println("3) Semestrale ");
        System.out.println("4) Annuale ");
        int scelta;
        while (true){
            scelta = verifyInput();
            if (scelta < 0 || scelta > 4) System.out.println("Devi inserire un numero tra 1 e 4");
            else break;
        }
        TipoAbbonamento tipoAbbonamento;
        double prezzoAbbonamento;
        switch (scelta) {
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
        System.out.println("L'abbonamento " + tipoAbbonamento + " viene: " + prezzoAbbonamento + " EUR. Confermare l'acquisto?");
        System.out.println("1) SI");
        System.out.println("2) NO");
        int scelta2;
        while (true){
            scelta2 = verifyInput();
            if (scelta2 <= 0 || scelta2 > 2) System.out.println("Devi inserire 1 o 2");
            else break;
        }
        if (scelta2 == 1){
            Abbonamento abbonamento = new Abbonamento(prezzoAbbonamento, LocalDate.now(), puntoVendita, LocalDate.now(), tipoAbbonamento, tessera);
            try {
                titoloViaggioDao.salvaTitoloViaggio(abbonamento);
                System.out.println("Abbonamento '" + tipoAbbonamento + "' acquistato con successo presso " + puntoVendita.getIndirizzo() + "!");
            } catch (AbbonamentoDateException | TesseraScadutaException | DistributoreFuoriServizioException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Non hai acquistato questo abbonamento.");
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

    private int verifyInput(){
        int number;
        while(true){
            String input = scanner.nextLine();
            try {
                number = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e){
                System.out.println("Devi inserire un numero");
            }
        }
        return number;
    }
}
