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
            System.out.println("Che cosa vuoi fare ?");
            System.out.println("1 per controllo biglietti/abbonamenti venduti");
            System.out.println("2 per controllo biglietti obliterati");
            System.out.println("3 per lista mezzi in manutezione e in servizio");
            System.out.println("4 per tratte scoperte");
            System.out.println("5 per mettere un veicolo in servizio o in manutenzione");
            System.out.println("0 per uscire");
            int scelta;
            while (true){
                scelta = verifyInput();
                if (scelta < 0 || scelta > 5) System.out.println("Devi inserire un numero tra zero e 5");
                else break;
            }
            switch (scelta){
                case 0 -> {
                    System.out.println("Logout da amministratore");
                    break;
                }
            }
        }


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
