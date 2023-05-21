package exceptions;

public class documentNonTrouveException extends Exception {
    public documentNonTrouveException() {
        super("Le document n'a pas été trouvé.");
    }
}
