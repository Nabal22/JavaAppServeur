package exceptions;

public class documentNonEmpruntéException extends Exception {
    public documentNonEmpruntéException() {
        super("Le document n'a pas été emprunté.");
    }
}
