package CarmineGargiulo.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String elementoCercato, String elementoAssociato){
        super("Nessun/a " + elementoCercato + " associato/a al/alla " + elementoAssociato + " selezionato");
    }
}
