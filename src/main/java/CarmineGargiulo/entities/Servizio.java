package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "servizi")
public class Servizio {
    @Id
    @GeneratedValue
    @Column(name = "servizio_id")
    private UUID servizioId;
    @Column(name = "data_inizio")
    private LocalDate dataInizio;
    @Column(name = "data_fine")
    private LocalDate dataFine;
    @ManyToOne
    @JoinColumn(name = "veicolo_id")
    private VeicoloPubblico veicoloPubblico;
    @ManyToOne
    @JoinColumn(name = "tratta_id")
    private Tratta tratta;

    public Servizio() {
    }

    public Servizio(LocalDate dataInizio, LocalDate dataFine, VeicoloPubblico veicoloPubblico, Tratta tratta) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.veicoloPubblico = veicoloPubblico;
        this.tratta = tratta;
    }

    public UUID getServizio_id() {
        return servizioId;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public VeicoloPubblico getVeicoloPubblico() {
        return veicoloPubblico;
    }

    public Tratta getTratta() {
        return tratta;
    }

    @Override
    public String toString() {
        return "Servizio = servizio_id: " + servizioId +
                ", dataInizio: " + dataInizio +
                ", dataFine: " + dataFine +
                ", veicolo: " + veicoloPubblico +
                ", tratta: " + tratta;
    }
}