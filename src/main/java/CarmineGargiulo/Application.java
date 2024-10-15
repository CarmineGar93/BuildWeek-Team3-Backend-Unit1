package CarmineGargiulo;

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
        TratteDao st = new TratteDao(em);
        for (int i = 0; i < 5; i++) {
            Tratta t1 = new Tratta(
                    "Linea " + (i+1),
                    faker.address().cityName(),
                    faker.address().cityName(),
                    faker.number().numberBetween(20,60)
            );
            st.saveTratta(t1);
        }







        em.close();
        emf.close();
    }
}
