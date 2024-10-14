package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "utenti")
public class Utente {
    @Id
    @GeneratedValue
    @Column(name = "utente_id")
    private UUID utenteId;
    @Column(nullable = false)
    private String nominativo;
    @Column(nullable = false, unique = true, name = "numero_tessera")
    private long numeroTessera;
    @Column(nullable = false, name = "anno_nascita")
    private int annoNascita;
    private static int count = 0;
    @OneToMany(mappedBy = "utente")
    private List<Abbonamento> abbonamentiList;

    public Utente(){

    }
    public Utente(String nominativo, int annoNascita) {
        this.nominativo = nominativo;
        this.annoNascita = annoNascita;
        this.numeroTessera = count;
        count ++;
    }

    public UUID getUtenteId() {
        return utenteId;
    }

    public String getNominativo() {
        return nominativo;
    }

    public void setNominativo(String nominativo) {
        this.nominativo = nominativo;
    }

    public long getNumeroTessera() {
        return numeroTessera;
    }

    public int getAnnoNascita() {
        return annoNascita;
    }

    public void setAnnoNascita(int annoNascita) {
        this.annoNascita = annoNascita;
    }

    public List<Abbonamento> getAbbonamentiList() {
        return abbonamentiList;
    }

    @Override
    public String toString() {
        return "Utente = utente_id: " + utenteId +
                ", nominativo: " + nominativo +
                ", numero_tessera: " + numeroTessera +
                ", anno_nascita: " + annoNascita;
    }
}
