package CarmineGargiulo;

import CarmineGargiulo.dao.*;
import CarmineGargiulo.entities.*;
import CarmineGargiulo.enums.TipoAbbonamento;
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

        inizializzaDb(puntoVenditaDAO, tratteDao, utenteDao, tessereDAO, titoloViaggioDao, veicoloDAO);

        generaStoricoVeicoli(veicoloDAO);

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

    public static void generaStoricoVeicoli(VeicoloDAO veicoloDAO) {

        List<VeicoloPubblico> veicoli = veicoloDAO.ottieniListaVeicoli();

        Set<String> nomiGenerati = new HashSet<>();

        List<String> storicoManutenzioni = new ArrayList<>();
        List<String> storicoServizi = new ArrayList<>();

        for (VeicoloPubblico veicolo : veicoli) {

            int numeroManutenzioni = faker.number().numberBetween(50, 500);
            int numeroServizi = faker.number().numberBetween(50, 500);

            String nomeVeicolo;
            do {
                nomeVeicolo = faker.ancient().god();
            } while (!nomiGenerati.add(nomeVeicolo));

            String risultatoManutenzione = "Il veicolo '" + nomeVeicolo + "' con targa: " + veicolo.getTarga() +
                    " è stato in manutenzione " + numeroManutenzioni + " volte.";
            String risultatoServizio = "Il veicolo '" + nomeVeicolo + "' con targa: " + veicolo.getTarga() +
                    " è stato in servizio " + numeroServizi + " volte.";

            storicoManutenzioni.add(risultatoManutenzione);
            storicoServizi.add(risultatoServizio);
        }

        // Stampa dei risultati
        System.out.println("\n Storico Manutenzioni:");
        for (String storico : storicoManutenzioni) {
            System.out.println(storico);
        }

        System.out.println("\nStorico Servizi:");
        for (String storico : storicoServizi) {
            System.out.println(storico);
        }
    }
}
