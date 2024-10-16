package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "biglietti")
public class Biglietto extends TitoloViaggio{
    @Column(nullable = false)
    private boolean convalidato = false;
    private LocalDate dataConvalidazione;
    @ManyToOne
    @JoinColumn(name = "veicolo_convalidazione_id")
    private VeicoloPubblico veicoloPubblico;

    public Biglietto(){

    }

    public Biglietto(double prezzoViaggio, LocalDate dataAcquisto, PuntoVendita puntoVendita) {
        super(prezzoViaggio, dataAcquisto, puntoVendita);
    }

    public LocalDate getDataConvalidazione() {
        return dataConvalidazione;
    }

    public void setDataConvalidazione(LocalDate dataConvalidazione) {
        this.dataConvalidazione = dataConvalidazione;
    }

    public boolean isConvalidato() {
        return convalidato;
    }

    public void setConvalidato(boolean convalidato) {
        this.convalidato = convalidato;
    }

    public VeicoloPubblico getVeicoloPubblico() {
        return veicoloPubblico;
    }

    public void setVeicoloPubblico(VeicoloPubblico veicoloPubblico) {
        this.veicoloPubblico = veicoloPubblico;
    }

    @Override
    public String toString() {
        return "Biglietto = " + super.toString() +
                ", convalidato: " + convalidato + ", veicolo di convalidazione: " + veicoloPubblico;
    }
}
