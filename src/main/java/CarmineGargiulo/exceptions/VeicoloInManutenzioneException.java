package CarmineGargiulo.exceptions;

public class VeicoloInManutenzioneException extends RuntimeException {
    public VeicoloInManutenzioneException() {
        super("Il veicolo cercato è attualmente in manutenzione.");
    }
}
