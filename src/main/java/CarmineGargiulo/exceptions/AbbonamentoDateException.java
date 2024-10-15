package CarmineGargiulo.exceptions;

public class AbbonamentoDateException extends RuntimeException{
    public AbbonamentoDateException(){
        super("Non puoi comprare l'abbonamento per le date selezionate dato che hai o avrai già un abbonamento in corso di validità");
    }
}
