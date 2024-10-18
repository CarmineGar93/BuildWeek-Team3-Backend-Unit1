package CarmineGargiulo;

import CarmineGargiulo.dao.*;
import CarmineGargiulo.entities.Biglietto;
import CarmineGargiulo.entities.PuntoVendita;
import CarmineGargiulo.entities.Tessera;
import CarmineGargiulo.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MenuInterattivo {
    private EntityManager em = null;
    private final Scanner scanner = new Scanner(System.in);
    private PuntoVenditaDAO puntoVenditaDAO = new PuntoVenditaDAO(em);
    private TratteDao tratteDao = new TratteDao(em);
    private UtenteDao utenteDao = new UtenteDao(em);
    private TessereDAO tessereDAO = new TessereDAO(em);
    private TitoloViaggioDao titoloViaggioDao = new TitoloViaggioDao(em);
    private VeicoloDAO veicoloDAO = new VeicoloDAO(em);
    private ManutenzioneDao manutenzioneDao = new ManutenzioneDao(em);
    private ServizioDao servizioDao = new ServizioDao(em);
    public MenuInterattivo(EntityManager entityManager) {
        this.em = entityManager;
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
        Tessera tessera;
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
                }catch (NotFoundException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        outerLoop2: while (true) {
            System.out.println("Cosa vuoi fare?");
            System.out.println("1) Compra biglietto");
            System.out.println("2) Compra abbonamento");
            System.out.println("3) Compra tessera");
            System.out.println("4) Scegli tratta");
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
            }
        }



    }

    private void compraBiglietto(){
        List<PuntoVendita> puntiVendita = puntoVenditaDAO.ottieniListaPuntiVendita();
        if (puntiVendita.isEmpty()) {
            System.out.println("Nessun punto vendita disponibile");
            return;
        }
        System.out.println("Seleziona un punto vendita dall'elenco:");
        for (int i = 0; i < puntiVendita.size(); i++) {
            System.out.println((i + 1) + ". " + puntiVendita.get(i).getIndirizzo());
        }
        System.out.println("Scegli il numero del punto vendita:");
        int scelta;
        while (true){
            scelta = verifyInput();
            if (scelta < 0 || scelta > puntiVendita.size() - 1) System.out.println("Devi inserire un numero tra 1 e " + (puntiVendita.size() - 1));
            else break;
        }
        PuntoVendita puntoVendita = puntiVendita.get(scelta - 1);
        System.out.println("Hai scelto il punto vendita: " + puntoVendita.getIndirizzo());
        Biglietto biglietto = new Biglietto(2.5, LocalDate.now(), puntoVendita);
        titoloViaggioDao.salvaTitoloViaggio(biglietto);

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
