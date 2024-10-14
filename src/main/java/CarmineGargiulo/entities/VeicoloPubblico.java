package CarmineGargiulo.entities;

import CarmineGargiulo.enums.TipoVeicolo;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "veicoli_pubblici")
public class VeicoloPubblico {
    @Id
    @GeneratedValue
    private UUID veicolo_id;
    @Column(unique = true, nullable = false)
    private String targa;
    private int capienza;
    private boolean inServizio = false;
    private boolean inManutenzione = false;
    @Enumerated(EnumType.STRING)
    private TipoVeicolo tipoVeicolo;
    @OneToMany(mappedBy = "veicoloPubblico")
    private List<Manutenzione> manutenzioniList;
    @OneToMany(mappedBy = "veicoloPubblico")
    private List<Servizio> serviziList;

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

    public UUID getVeicolo_id() {
        return veicolo_id;
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

    @Override
    public String toString() {
        return "VeicoloPubblico = targa: " + targa +
                ", capienza: " + capienza +
                ", inServizio: " + inServizio +
                ", inManutenzione: " + inManutenzione +
                ", tipoVeicolo: " + tipoVeicolo;
    }
}
