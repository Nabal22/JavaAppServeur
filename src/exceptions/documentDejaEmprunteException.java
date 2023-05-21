package exceptions;

public class documentDejaEmprunteException extends Exception {
    public documentDejaEmprunteException() {
        super("Le document est déjà emprunté.");
    }
}
