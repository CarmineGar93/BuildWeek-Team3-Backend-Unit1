package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tratte")
@NamedQuery(name = "getAllTratte", query = "SELECT t FROM Tratta t")
public class Tratta {
    @Id
    @GeneratedValue
    @Column(name = "tratta_id")
    private UUID trattaId;
    @Column(name = "nome_tratta")
    private String nomeTratta;
    @Column(name = "punto_partenza")
    private String puntoPartenza;
    @Column(name = "punto_arrivo")
    private String puntoArrivo;
    @Column(name = "tempo_medio_percorrenza")
    private int tempoMedioPercorrenza;
    @OneToMany(mappedBy = "tratta")
    private List<Servizio> serviziList;

    @ManyToOne
    @JoinColumn(name = "veicolo_id")
    private VeicoloPubblico veicoloPubblico;
    public Tratta() {
    }

    public Tratta(String nomeTratta, String puntoPartenza, String puntoArrivo, int tempoMedioPercorrenza) {
        this.puntoPartenza = puntoPartenza;
        this.puntoArrivo = puntoArrivo;
        this.tempoMedioPercorrenza = tempoMedioPercorrenza;
        this.nomeTratta = nomeTratta;
    }

    public String getNomeTratta() {
        return nomeTratta;
    }

    public void setNomeTratta(String nomeTratta) {
        this.nomeTratta = nomeTratta;
    }

    public UUID getTrattId() {
        return trattaId;
    }

    public String getPuntoPartenza() {
        return puntoPartenza;
    }

    public String getPuntoArrivo() {
        return puntoArrivo;
    }

    public int getTempoMedioPercorrenza() {
        return tempoMedioPercorrenza;
    }

    public void setPuntoPartenza(String puntoPartenza) {
        this.puntoPartenza = puntoPartenza;
    }

    public void setPuntoArrivo(String puntoArrivo) {
        this.puntoArrivo = puntoArrivo;
    }

    public void setTempoMedioPercorrenza(int tempoMedioPercorrenza) {
        this.tempoMedioPercorrenza = tempoMedioPercorrenza;
    }

    public List<Servizio> getServiziList() {
        return serviziList;
    }

    @Override
    public String toString() {
        return "Tratta = nome_tratta: " + nomeTratta +
                ", punto_partenza: " + puntoPartenza +
                ", punto_arrivo: " + puntoArrivo +
                ", tempo_medio_percorrenza: " + tempoMedioPercorrenza;
    }
}
