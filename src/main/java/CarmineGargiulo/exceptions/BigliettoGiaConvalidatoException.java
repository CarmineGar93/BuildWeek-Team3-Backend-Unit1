package CarmineGargiulo.exceptions;

public class BigliettoGiaConvalidatoException extends RuntimeException{
    public BigliettoGiaConvalidatoException(){
        super("Il biglietto non è utilizzabile dato che è gia stato convalidato");
    }
}
