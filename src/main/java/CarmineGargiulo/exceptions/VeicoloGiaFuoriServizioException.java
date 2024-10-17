package CarmineGargiulo.exceptions;

public class VeicoloGiaFuoriServizioException extends RuntimeException {
    public VeicoloGiaFuoriServizioException() {
        super("Il veicolo è già fuori servizio.");
    }
}
