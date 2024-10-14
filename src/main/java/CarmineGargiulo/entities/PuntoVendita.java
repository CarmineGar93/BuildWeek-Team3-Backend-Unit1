package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "punti_vendita")
public abstract class PuntoVendita {
    @Id
    @GeneratedValue
    @Column(name = "punto_vendita_id")
    protected UUID puntoVenditaId;
    @Column(nullable = false)
    protected String indirizzo;
    @OneToMany(mappedBy = "puntoVendita")
    protected List<TitoloViaggio> titoliViaggioList;

    public PuntoVendita() {
    }

    public PuntoVendita(String indirizzo) {
        this.indirizzo = indirizzo;
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

    @Override
    public String toString() {
        return "puntoVendita_id: " + puntoVenditaId +
                ", indirizzo: " + indirizzo;
    }
}
