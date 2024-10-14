package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "punti_vendita")
public abstract class PuntoVendita {
    @Id
    @GeneratedValue
    protected UUID puntoVendita_id;
    @Column(nullable = false)
    private String indirizzo;

    public PuntoVendita() {
    }

    public PuntoVendita(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public UUID getPuntoVendita_id() {
        return puntoVendita_id;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    @Override
    public String toString() {
        return "puntoVendita_id: " + puntoVendita_id +
                ", indirizzo: " + indirizzo;
    }
}
