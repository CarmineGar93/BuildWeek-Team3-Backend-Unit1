package CarmineGargiulo.exceptions;

public class TrattaGiaPercorsaException  extends RuntimeException{
    public TrattaGiaPercorsaException(){
        super("Impossibile assegnare la tratta fornita dato che è attualmente percorsa da un altro veicolo");
    }
}
