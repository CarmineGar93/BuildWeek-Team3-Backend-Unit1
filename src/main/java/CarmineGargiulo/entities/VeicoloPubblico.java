package CarmineGargiulo.entities;

import CarmineGargiulo.enums.TipoVeicolo;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "veicoli_pubblici")
@NamedQuery(name = "getAllVeicoli", query = "SELECT v FROM VeicoloPubblico v"
)
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
    @OneToMany(mappedBy = "veicoloPubblico")
    private List<Manutenzione> manutenzioniList;
    @OneToMany(mappedBy = "veicoloPubblico")
    private List<Servizio> serviziList;
    @OneToMany(mappedBy = "veicoloPubblico")
    private List<Biglietto> bigliettiList;

    public VeicoloPubblico() {
    }

    public VeicoloPubblico(String targa, TipoVeicolo tipoVeicolo) {
        this.targa = targa;
        this.tipoVeicolo = tipoVeicolo;
        if(tipoVeicolo == TipoVeicolo.AUTOBUS) capienza = 70;
        else capienza = 150;
    }

    public List<Manutenzione> getManutenzioniList() {
        return manutenzioniList;
    }

    public List<Servizio> getServiziList() {
        return serviziList;
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

    public List<Biglietto> getBigliettiList() {
        return bigliettiList;
    }

    @Override
    public String toString() {
        return "VeicoloPubblico = targa: " + targa +
                ", capienza: " + capienza +
                ", inServizio: " + inServizio +
                ", inManutenzione: " + inManutenzione +
                ", tipoVeicolo: " + tipoVeicolo;
    }
}
