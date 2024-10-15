package CarmineGargiulo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "biglietti")
public class Biglietto extends TitoloViaggio{
    @Column(nullable = false)
    private boolean convalidato = false;

    public Biglietto(double prezzoViaggio, LocalDate dataAcquisto, PuntoVendita puntoVendita) {
        super(prezzoViaggio, dataAcquisto, puntoVendita);
    }

    public boolean isConvalidato() {
        return convalidato;
    }

    public void setConvalidato(boolean convalidato) {
        this.convalidato = convalidato;
    }

    @Override
    public String toString() {
        return "Biglietto = " + super.toString() +
                ", convalidato: " + convalidato;
    }
}
