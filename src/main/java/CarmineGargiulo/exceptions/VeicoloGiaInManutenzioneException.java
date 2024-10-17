package CarmineGargiulo.exceptions;

public class VeicoloGiaInManutenzioneException extends RuntimeException {
    public VeicoloGiaInManutenzioneException() {
        super("Il veicolo cercato è già in manutenzione.");
    }
}
