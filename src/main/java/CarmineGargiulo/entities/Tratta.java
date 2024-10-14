package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tratte")
public class Tratta {
    @Id
    @GeneratedValue
    private UUID tratta_id;
    private String nome_tratta;
    private String punto_partenza;
    private String punto_arrivo;
    private int tempo_medio_percorrenza;
    @OneToMany(mappedBy = "tratta")
    private List<Servizio> serviziList;

    public Tratta() {
    }

    public Tratta(String nome_tratta, String punto_partenza, String punto_arrivo, int tempo_medio_percorrenza) {
        this.punto_partenza = punto_partenza;
        this.punto_arrivo = punto_arrivo;
        this.tempo_medio_percorrenza = tempo_medio_percorrenza;
        this.nome_tratta = nome_tratta;
    }

    public String getNome_tratta() {
        return nome_tratta;
    }

    public void setNome_tratta(String nome_tratta) {
        this.nome_tratta = nome_tratta;
    }

    public UUID getTratta_id() {
        return tratta_id;
    }

    public String getPunto_partenza() {
        return punto_partenza;
    }

    public String getPunto_arrivo() {
        return punto_arrivo;
    }

    public int getTempo_medio_percorrenza() {
        return tempo_medio_percorrenza;
    }

    public void setPunto_partenza(String punto_partenza) {
        this.punto_partenza = punto_partenza;
    }

    public void setPunto_arrivo(String punto_arrivo) {
        this.punto_arrivo = punto_arrivo;
    }

    public void setTempo_medio_percorrenza(int tempo_medio_percorrenza) {
        this.tempo_medio_percorrenza = tempo_medio_percorrenza;
    }

    public List<Servizio> getServiziList() {
        return serviziList;
    }

    @Override
    public String toString() {
        return "Tratta = nome_tratta: " + nome_tratta +
                ", punto_partenza: " + punto_partenza +
                ", punto_arrivo: " + punto_arrivo +
                ", tempo_medio_percorrenza: " + tempo_medio_percorrenza;
    }
}
