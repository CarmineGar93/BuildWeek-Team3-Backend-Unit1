package CarmineGargiulo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "utenti")
public class Utente {
    @Id
    @GeneratedValue
    private UUID utente_id;
    @Column(nullable = false)
    private String nominativo;
    @Column(nullable = false, unique = true)
    private long numero_tessera;
    @Column(nullable = false)
    private int anno_nascita;
    private static int count = 0;
    @OneToMany(mappedBy = "utente")
    private List<Abbonamento> abbonamentiList;

    public Utente(){

    }
    public Utente(String nominativo, int anno_nascita) {
        this.nominativo = nominativo;
        this.anno_nascita = anno_nascita;
        this.numero_tessera = count;
        count ++;
    }

    public UUID getUtente_id() {
        return utente_id;
    }

    public String getNominativo() {
        return nominativo;
    }

    public void setNominativo(String nominativo) {
        this.nominativo = nominativo;
    }

    public long getNumero_tessera() {
        return numero_tessera;
    }

    public int getAnno_nascita() {
        return anno_nascita;
    }

    public void setAnno_nascita(int anno_nascita) {
        this.anno_nascita = anno_nascita;
    }

    public List<Abbonamento> getAbbonamentiList() {
        return abbonamentiList;
    }

    @Override
    public String toString() {
        return "Utente = utente_id: " + utente_id +
                ", nominativo: " + nominativo +
                ", numero_tessera: " + numero_tessera +
                ", anno_nascita: " + anno_nascita;
    }
}
