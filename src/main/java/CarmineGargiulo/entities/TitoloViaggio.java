package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "titoli_viaggio")
@NamedQuery(name = "getAllTitoliViaggio", query = "SELECT t FROM TitoloViaggio t")
public abstract class TitoloViaggio {
    @Id
    @GeneratedValue
    @Column(name = "titolo_viaggio_id")
    protected UUID titoloViaggioId;
    @Column(nullable = false, name = "prezzo_titolo_viaggio")
    protected double prezzoViaggio;
    @Column(nullable = false, name = "data_acquisto")
    protected LocalDate dataAcquisto;
    @ManyToOne
    @JoinColumn(name = "punto_vendita_id")
    protected PuntoVendita puntoVendita;

    public TitoloViaggio(){

    }
    public TitoloViaggio(double prezzoViaggio, LocalDate dataAcquisto, PuntoVendita puntoVendita) {
        this.prezzoViaggio = prezzoViaggio;
        this.dataAcquisto = dataAcquisto;
        this.puntoVendita = puntoVendita;
    }

    public UUID getTitoloViaggio_id() {
        return titoloViaggioId;
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
        return "titoloViaggio_id:" + titoloViaggioId +
                ", dataAcquisto: " + dataAcquisto +
                ", puntoVendita: " + puntoVendita +
                ", prezzoViaggio: " + prezzoViaggio;
    }
}
