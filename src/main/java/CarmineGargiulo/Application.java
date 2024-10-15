package CarmineGargiulo;

import CarmineGargiulo.dao.PuntoVenditaDAO;
import CarmineGargiulo.entities.Distributore;
import CarmineGargiulo.entities.RivenditoreAutorizzato;
import CarmineGargiulo.dao.TratteDao;
import CarmineGargiulo.entities.Tratta;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Locale;

public class Application {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("azienda-trasporti");
    private static final Faker faker = new Faker(Locale.ITALY);
    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        PuntoVenditaDAO puntoVenditaDAO = new PuntoVenditaDAO(em);
        TratteDao st = new TratteDao(em);
        inizializzaDb(puntoVenditaDAO, st);

        em.close();
        emf.close();
    }

    public static void inizializzaDb(PuntoVenditaDAO puntoVenditaDAO, TratteDao tratteDao){
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

    }
}
