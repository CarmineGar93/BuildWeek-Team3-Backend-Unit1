package CarmineGargiulo.exceptions;

public class TrattaGiaPercorsaException extends RuntimeException {
    public TrattaGiaPercorsaException() {
        super("La tratta è già percorsa da un altro veicolo.");
    }
}
