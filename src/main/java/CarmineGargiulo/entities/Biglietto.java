package CarmineGargiulo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "biglietti")
public class Biglietto extends TitoloViaggio{
    private boolean convalidato = false;
    private LocalDateTime dataConvalidazione;
    private int durata;

    public Biglietto(double prezzoViaggio, LocalDate dataAcquisto, PuntoVendita puntoVendita, int durata) {
        super(prezzoViaggio, dataAcquisto, puntoVendita);
        this.durata = durata;
    }

    public boolean isConvalidato() {
        return convalidato;
    }

    public void setConvalidato(boolean convalidato) {
        this.convalidato = convalidato;
    }

    public LocalDateTime getDataConvalidazione() {
        return dataConvalidazione;
    }

    public void setDataConvalidazione(LocalDateTime dataConvalidazione) {
        this.dataConvalidazione = dataConvalidazione;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    @Override
    public String toString() {
        return "Biglietto = " + super.toString() +
                ", convalidato: " + convalidato +
                ", dataConvalidazione: " + dataConvalidazione +
                ", durata: " + durata;
    }
}
