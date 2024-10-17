package CarmineGargiulo.exceptions;

public class VeicoloGiaFuoriManutenzioneException extends RuntimeException {
    public VeicoloGiaFuoriManutenzioneException() {
        super("Il veicolo è già fuori manutenzione.");
    }
}
