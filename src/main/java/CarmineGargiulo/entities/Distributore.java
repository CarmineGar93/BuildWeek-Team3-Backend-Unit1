package CarmineGargiulo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "distributori")
public class Distributore  extends PuntoVendita{
    private boolean attivo;
    public Distributore(){

    }
    public Distributore(String indirizzo, boolean attivo) {
        super(indirizzo);
        this.attivo = attivo;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    @Override
    public String toString() {
        return "Distributore = " + super.toString() + ", attivo: " + attivo;
    }
}
