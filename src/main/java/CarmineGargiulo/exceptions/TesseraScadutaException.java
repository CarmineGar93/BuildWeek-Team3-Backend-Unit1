package CarmineGargiulo.exceptions;

public class TesseraScadutaException extends RuntimeException {
    public TesseraScadutaException(){
        super("Impossibile comprare l'abbonamento, la tessera fornita è scaduta");
    }
}
