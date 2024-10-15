package CarmineGargiulo;

import CarmineGargiulo.dao.PuntoVenditaDAO;
import CarmineGargiulo.entities.Distributore;
import CarmineGargiulo.entities.RivenditoreAutorizzato;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Locale;

public class Application {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("azienda-trasporti");
    private static final Faker faker = new Faker(Locale.ITALY);
    public static void main(String[] args) {
        System.out.println("Hello World!!");
        EntityManager em = emf.createEntityManager();
        PuntoVenditaDAO puntoVenditaDAO = new PuntoVenditaDAO(em);
        inizializzaDb(puntoVenditaDAO);
    }

    public static void inizializzaDb(PuntoVenditaDAO puntoVenditaDAO){
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
    }
}
