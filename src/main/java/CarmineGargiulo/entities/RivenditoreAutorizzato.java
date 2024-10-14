package CarmineGargiulo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "rivenditori_autorizzati")
public class RivenditoreAutorizzato extends PuntoVendita{
    private String nome;

    public RivenditoreAutorizzato(){

    }
    public RivenditoreAutorizzato(String indirizzo, String nome) {
        super(indirizzo);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Rivenditore autorizzato = " + super.toString() + ", nome: " + nome;
    }
}
