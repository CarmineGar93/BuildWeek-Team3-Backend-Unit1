package CarmineGargiulo;

import com.github.javafaker.Faker;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Locale;

public class Application {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("azienda-trasporti");
    private static final Faker faker = new Faker(Locale.US);
    public static void main(String[] args) {
        System.out.println("Hello World!!");
    }
}
