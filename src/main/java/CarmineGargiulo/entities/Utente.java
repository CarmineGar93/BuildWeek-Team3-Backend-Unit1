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
    @Column(nullable = false, name = "anno_nascita")
    private int annoNascita;
    @OneToOne(mappedBy = "utente")
    private Tessera tessera;

    public Tessera getTessera() {
        return tessera;
    }

    public Utente(){

    }
    public Utente(String nominativo, int annoNascita) {
        this.nominativo = nominativo;
        this.annoNascita = annoNascita;
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

    public int getAnnoNascita() {
        return annoNascita;
    }

    public void setAnnoNascita(int annoNascita) {
        this.annoNascita = annoNascita;
    }


    @Override
    public String toString() {
        return "Utente = utente_id: " + utenteId +
                ", nominativo: " + nominativo +
                ", anno_nascita: " + annoNascita;
    }
}
