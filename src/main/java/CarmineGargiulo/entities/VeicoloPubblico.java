package CarmineGargiulo.entities;

import CarmineGargiulo.enums.TipoVeicolo;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "veicoli_pubblici")
@NamedQuery(name = "getAllVeicoli", query = "SELECT v FROM VeicoloPubblico v")
public class VeicoloPubblico {
    @Id
    @GeneratedValue
    @Column(name = "veicolo_id")
    private UUID veicoloId;
    @Column(unique = true, nullable = false)
    private String targa;
    private int capienza;
    @Column(name = "in_servizio")
    private boolean inServizio = false;
    @Column(name = "in_manutenzione")
    private boolean inManutenzione = false;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_veicolo")
    private TipoVeicolo tipoVeicolo;
    @Column(name = "numero_manutenzioni", nullable = false)
    private int numeroManutenzioni = 0;
    @Column(name = "numero_servizi", nullable = false)
    private int numeroServizi = 0;

    public VeicoloPubblico() {
    }

    public VeicoloPubblico(String targa, TipoVeicolo tipoVeicolo) {
        this.targa = targa;
        this.tipoVeicolo = tipoVeicolo;
        this.capienza = tipoVeicolo == TipoVeicolo.AUTOBUS ? 70 : 150;
    }

    public UUID getVeicoloId() {
        return veicoloId;
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public int getCapienza() {
        return capienza;
    }

    public boolean isInServizio() {
        return inServizio;
    }

    public void setInServizio(boolean inServizio) {
        this.inServizio = inServizio;
    }

    public boolean isInManutenzione() {
        return inManutenzione;
    }

    public void setInManutenzione(boolean inManutenzione) {
        this.inManutenzione = inManutenzione;
    }

    public TipoVeicolo getTipoVeicolo() {
        return tipoVeicolo;
    }

    public void setTipoVeicolo(TipoVeicolo tipoVeicolo) {
        this.tipoVeicolo = tipoVeicolo;
    }

    public int getNumeroManutenzioni() {
        return numeroManutenzioni;
    }

    public void setNumeroManutenzioni(int numeroManutenzioni) {
        this.numeroManutenzioni = numeroManutenzioni;
    }

    public int getNumeroServizi() {
        return numeroServizi;
    }

    public void setNumeroServizi(int numeroServizi) {
        this.numeroServizi = numeroServizi;
    }

    @Override
    public String toString() {
        return "VeicoloPubblico = targa: " + targa +
                ", capienza: " + capienza +
                ", numeroManutenzioni: " + numeroManutenzioni +
                ", numeroServizi: " + numeroServizi +
                ", tipoVeicolo: " + tipoVeicolo;
    }
}
