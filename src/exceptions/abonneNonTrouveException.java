package exceptions;

public class abonneNonTrouveException extends Exception {
    public abonneNonTrouveException() {
        super("L'abonné n'a pas été trouvé.");
    }

}
