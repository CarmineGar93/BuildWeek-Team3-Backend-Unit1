package CarmineGargiulo.exceptions;

public class VeicoloInServizioException extends RuntimeException {
    public VeicoloInServizioException() {
        super("Il veicolo cercato è attualmente in servizio.");
    }
}
