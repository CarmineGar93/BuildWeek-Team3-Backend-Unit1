package CarmineGargiulo.exceptions;

public class VeicoloGiaInServizioException extends RuntimeException {
    public VeicoloGiaInServizioException() {
        super("Il veicolo cercato è già in servizio.");
    }
}
