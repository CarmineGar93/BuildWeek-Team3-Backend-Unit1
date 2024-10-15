package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tessere")
public class Tessera {
    @Id
    @GeneratedValue
    @Column(name = "tessera_id")
    private UUID tesseraId;
    @Column(name = "data_emissione")
    private LocalDate dataEmissione;
    @Column(name = "data_scadenza")
    private LocalDate dataScadenza;
    @OneToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;
    @OneToMany(mappedBy = "tessera")
    private List<Abbonamento> abbonamentiList;

    public Tessera() {
    }

    public Tessera(LocalDate dataEmissione, Utente utente) {
        this.dataEmissione = dataEmissione;
        this.utente = utente;
        this.dataScadenza = dataEmissione.plusYears(1);
    }

    public UUID getTesseraId() {
        return tesseraId;
    }

    public LocalDate getDataEmissione() {
        return dataEmissione;
    }

    public void setDataEmissione(LocalDate dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public Utente getUtente() {
        return utente;
    }

    public List<Abbonamento> getAbbonamentiList() {
        return abbonamentiList;
    }

    @Override
    public String toString() {
        return "Tessera =  tesseraId: " + tesseraId +
                ", dataEmissione: " + dataEmissione +
                ", dataScadenza: " + dataScadenza +
                ", utente: " + utente;
    }
}
