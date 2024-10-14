package CarmineGargiulo.entities;

import CarmineGargiulo.enums.TipoAbbonamento;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "abbonamenti")
public class Abbonamento extends TitoloViaggio{
    @Column(name = "data_inizio", nullable = false)
    private LocalDate dataInizio;
    @Column(name = "data_fine")
    private LocalDate dataFine;
    @Column(name = "tipo_abbonamento", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoAbbonamento tipoAbbonamento;
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;
    public Abbonamento(){

    }
    public Abbonamento(double prezzoViaggio, LocalDate dataAcquisto, PuntoVendita puntoVendita, LocalDate dataInizio, TipoAbbonamento tipoAbbonamento, Utente utente) {
        super(prezzoViaggio, dataAcquisto, puntoVendita);
        this.dataInizio = dataInizio;
        if(tipoAbbonamento == TipoAbbonamento.SETTIMANALE) this.dataFine = dataInizio.plusDays(7);
        else if (tipoAbbonamento == TipoAbbonamento.MENSILE) this.dataFine = dataInizio.plusMonths(1);
        else if (tipoAbbonamento == TipoAbbonamento.SEMESTRALE) this.dataFine = dataInizio.plusMonths(6);
        else this.dataFine = dataInizio.plusYears(1);
        this.tipoAbbonamento = tipoAbbonamento;
        this.utente = utente;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public TipoAbbonamento getTipoAbbonamento() {
        return tipoAbbonamento;
    }

    public void setTipoAbbonamento(TipoAbbonamento tipoAbbonamento) {
        this.tipoAbbonamento = tipoAbbonamento;
    }

    public Utente getUtente() {
        return utente;
    }

    @Override
    public String toString() {
        return "Abbonamento = " + super.toString() +
                ", dataInizio: " + dataInizio +
                ", dataFine: " + dataFine +
                ", tipoAbbonamento: " + tipoAbbonamento +
                ", numero_tessera: " + utente.getNumeroTessera();
    }
}
