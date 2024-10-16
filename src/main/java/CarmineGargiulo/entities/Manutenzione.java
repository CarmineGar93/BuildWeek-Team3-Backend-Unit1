package CarmineGargiulo.entities;

import CarmineGargiulo.enums.TipoManutenzione;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "manutenzioni")
public class Manutenzione {
    @Id
    @GeneratedValue
    @Column(name = "manutenzione_id")
    private UUID manutenzioneId;
    @Column(name = "data_inizio", nullable = false)
    private LocalDate dataInizio;

    private LocalDate dataFine;
    @Column(name = "tipo_manutenzione")
    @Enumerated(EnumType.STRING)
    private TipoManutenzione tipoManutenzione;
    @ManyToOne
    @JoinColumn(name = "veicolo_id")
    public VeicoloPubblico veicoloPubblico;

    public Manutenzione(){

    }
    public Manutenzione(LocalDate dataInizio, LocalDate dataFine, TipoManutenzione tipoManutenzione, VeicoloPubblico veicoloPubblico) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.tipoManutenzione = tipoManutenzione;
        this.veicoloPubblico = veicoloPubblico;
    }

    public Manutenzione(TipoManutenzione tipoManutenzione, VeicoloPubblico veicoloPubblico){
        this.dataInizio = LocalDate.now();
        this.tipoManutenzione = tipoManutenzione;
        this.veicoloPubblico = veicoloPubblico;
    }

    public UUID getManutenzioneId() {
        return manutenzioneId;
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

    public TipoManutenzione getTipoManutenzione() {
        return tipoManutenzione;
    }

    public void setTipoManutenzione(TipoManutenzione tipoManutenzione) {
        this.tipoManutenzione = tipoManutenzione;
    }

    public VeicoloPubblico getVeicoloPubblico() {
        return veicoloPubblico;
    }

    @Override
    public String toString() {
        return "Manutenzione = manutenzione_id: " + manutenzioneId +
                ", dataInizio: " + dataInizio +
                ", dataFine: " + dataFine +
                ", tipoManutenzione: " + tipoManutenzione +
                ", veicolo: " + veicoloPubblico +
                '}';
    }
}
