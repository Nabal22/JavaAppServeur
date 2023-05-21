package exceptions;

public class documentNonLibreException extends Exception{
    public documentNonLibreException() {
        super("Le document n'est pas libre.");
    }
}
