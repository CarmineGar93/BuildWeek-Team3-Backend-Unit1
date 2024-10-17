package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "punti_vendita")
@NamedQuery(name = "getAllPuntiVendita", query = "SELECT p FROM PuntoVendita p")
public abstract class PuntoVendita {
    @Id
    @GeneratedValue
    @Column(name = "punto_vendita_id")
    protected UUID puntoVenditaId;

    @Column(nullable = false)
    protected String indirizzo;

    @OneToMany(mappedBy = "puntoVendita")
    protected List<TitoloViaggio> titoliViaggioList;

    // Modifica: da int a Integer
    @Column(name = "biglietti_venduti")
    protected Integer bigliettiVenduti;

    @Column(name = "abbonamenti_venduti")
    protected Integer abbonamentiVenduti;

    public PuntoVendita() {
        // Inizializziamo i valori a 0 per evitare NullPointerException
        this.bigliettiVenduti = 0;
        this.abbonamentiVenduti = 0;
    }

    public PuntoVendita(String indirizzo) {
        this.indirizzo = indirizzo;
        this.bigliettiVenduti = 0;  // Inizializzazione dei valori
        this.abbonamentiVenduti = 0;  // Inizializzazione dei valori
    }

    public UUID getPuntoVenditaId() {
        return puntoVenditaId;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public List<TitoloViaggio> getTitoliViaggioList() {
        return titoliViaggioList;
    }

    public Integer getBigliettiVenduti() {
        return bigliettiVenduti;
    }

    public void setBigliettiVenduti(Integer bigliettiVenduti) {
        this.bigliettiVenduti = bigliettiVenduti;
    }

    public Integer getAbbonamentiVenduti() {
        return abbonamentiVenduti;
    }

    public void setAbbonamentiVenduti(Integer abbonamentiVenduti) {
        this.abbonamentiVenduti = abbonamentiVenduti;
    }

    @Override
    public String toString() {
        return "puntoVendita_id: " + puntoVenditaId +
                ", indirizzo: " + indirizzo +
                ", bigliettiVenduti: " + bigliettiVenduti +
                ", abbonamentiVenduti: " + abbonamentiVenduti;
    }
}
