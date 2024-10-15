package CarmineGargiulo;

import CarmineGargiulo.dao.*;
import CarmineGargiulo.entities.*;
import CarmineGargiulo.enums.TipoAbbonamento;
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
        TratteDao st = new TratteDao(em);
        UtenteDao utenteDao = new UtenteDao(em);
        TessereDAO tessereDAO = new TessereDAO(em);
        TitoloViaggioDao titoloViaggioDao = new TitoloViaggioDao(em);
        inizializzaDb(puntoVenditaDAO, st, utenteDao, tessereDAO, titoloViaggioDao);

        em.close();
        emf.close();
    }

    public static void inizializzaDb(PuntoVenditaDAO puntoVenditaDAO, TratteDao tratteDao, UtenteDao utenteDao, TessereDAO tessereDAO, TitoloViaggioDao titoloViaggioDao){
        if(puntoVenditaDAO.ottieniListaPuntiVendita().isEmpty()){
            for (int i = 0; i < 10; i++) {
                boolean random = faker.random().nextBoolean();
                if(random){
                    RivenditoreAutorizzato rivenditoreAutorizzato = new RivenditoreAutorizzato(faker.address().streetName(), faker.company().name());
                    puntoVenditaDAO.salvaPuntoVendita(rivenditoreAutorizzato);
                } else {
                    Distributore distributore = new Distributore(faker.address().streetName(), faker.random().nextBoolean());
                    puntoVenditaDAO.salvaPuntoVendita(distributore);
                }
            }
        }
        if(tratteDao.ottieniListaTratte().isEmpty()){
            for (int i = 0; i < 5; i++) {
                Tratta t1 = new Tratta(
                        "Linea " + (i + 1),
                        faker.address().cityName(),
                        faker.address().cityName(),
                        faker.number().numberBetween(20, 60)
                );
                tratteDao.saveTratta(t1);
            }
        }
        if (utenteDao.ottieniListaUtenti().isEmpty()){
            for (int i = 0; i < 5; i++) {
                Utente utente= new Utente(
                        faker.name().fullName(),
                        faker.random().nextInt(1950,2006)
                );
                utenteDao.salvaUtenteDao(utente);
            }
        }
        if(tessereDAO.ottieniListaTessere().isEmpty()){
            List<Utente> utentiList = utenteDao.ottieniListaUtenti();
            for (int i = 0; i < utentiList.size(); i++) {
                Tessera tessera = new Tessera(LocalDate.now(), utentiList.get(i));
                tessereDAO.salvaTessera(tessera);
            }
        }
       if(titoloViaggioDao.ottieniListaTitoliViaggio().isEmpty()){
           List<PuntoVendita> puntoVenditaList = puntoVenditaDAO.ottieniListaPuntiVendita();
           List<Tessera> tesseraList = tessereDAO.ottieniListaTessere();
           List<TipoAbbonamento> tipiList = Arrays.stream(TipoAbbonamento.values()).toList();
           for (int i = 0; i < 10; i++) {
               boolean random = faker.random().nextBoolean();
               PuntoVendita puntoRandom;
               while (true) {
                   puntoRandom = puntoVenditaList.get(faker.random().nextInt(0, puntoVenditaList.size() - 1));
                   if (puntoRandom instanceof RivenditoreAutorizzato) break;
                   else if (((Distributore) puntoRandom).isAttivo()) break;
               }
               if(random){
                   Biglietto biglietto = new Biglietto(Double.parseDouble(faker.commerce().price()),
                           LocalDate.of(faker.random().nextInt(2020, 2024), faker.random().nextInt(1,12), faker.random().nextInt(1, 27)),
                           puntoRandom);
                   titoloViaggioDao.salvaTitoloViaggio(biglietto);
               } else {
                   Abbonamento abbonamento = new Abbonamento(Double.parseDouble(faker.commerce().price()),
                           LocalDate.of(faker.random().nextInt(2020, 2024), faker.random().nextInt(1,12), faker.random().nextInt(1, 27)),
                           puntoRandom, LocalDate.now(),
                           tipiList.get(faker.random().nextInt(0, tipiList.size()-1)), tesseraList.get(faker.random().nextInt(0, tesseraList.size() -1)));
                   titoloViaggioDao.salvaTitoloViaggio(abbonamento);
               }
           }
       }
    }
}
