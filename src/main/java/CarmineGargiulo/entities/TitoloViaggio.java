package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "titoli_viaggio")
public class TitoloViaggio {
    @Id
    @GeneratedValue
    protected UUID titoloViaggio_id;
    @Column(nullable = false)
    protected double prezzoViaggio;
    @Column(nullable = false)
    protected LocalDate dataAcquisto;
    @ManyToOne
    @JoinColumn(name = "puntoVendita_id")
    protected PuntoVendita puntoVendita;

    public TitoloViaggio(){

    }
    public TitoloViaggio(double prezzoViaggio, LocalDate dataAcquisto, PuntoVendita puntoVendita) {
        this.prezzoViaggio = prezzoViaggio;
        this.dataAcquisto = dataAcquisto;
        this.puntoVendita = puntoVendita;
    }

    public UUID getTitoloViaggio_id() {
        return titoloViaggio_id;
    }

    public double getPrezzoViaggio() {
        return prezzoViaggio;
    }

    public void setPrezzoViaggio(double prezzoViaggio) {
        this.prezzoViaggio = prezzoViaggio;
    }

    public PuntoVendita getPuntoVendita() {
        return puntoVendita;
    }

    public LocalDate getDataAcquisto() {
        return dataAcquisto;
    }

    public void setDataAcquisto(LocalDate dataAcquisto) {
        this.dataAcquisto = dataAcquisto;
    }

    @Override
    public String toString() {
        return "Titolo di Viaggio = titoloViaggio_id:" + titoloViaggio_id +
                ", dataAcquisto: " + dataAcquisto +
                ", puntoVendita: " + puntoVendita +
                ", prezzoViaggio: " + prezzoViaggio;
    }
}
