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
    private static final Faker faker = new Faker(Locale.US);
    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        TratteDao st = new TratteDao(em);

        Tratta t1 = new Tratta(
                "Linea 1",
                faker.address().cityName(),
                faker.address().cityName(),
                faker.number().numberBetween(20,60)
        );

        Tratta t2 = new Tratta(
                "Linea 2",
                faker.address().cityName(),
                faker.address().cityName(),
                faker.number().numberBetween(20,60)
        );

        Tratta t3 = new Tratta(
                "Linea 3",
                faker.address().cityName(),
                faker.address().cityName(),
                faker.number().numberBetween(20,60)
        );

        Tratta t4 = new Tratta(
                "Linea 4",
                faker.address().cityName(),
                faker.address().cityName(),
                faker.number().numberBetween(20,60)
        );

        st.saveTratta(t1);
        st.saveTratta(t2);
        st.saveTratta(t3);
        st.saveTratta(t4);

        em.close();
        emf.close();
    }
}
