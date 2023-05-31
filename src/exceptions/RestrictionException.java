package exceptions;

public class RestrictionException extends RuntimeException {

    /**
     * Construit une nouvelle RestrictionException avec le message détaillé spécifié.
     * @param s le message détaillé
     */
    public RestrictionException(String s) {
        super(s);
    }
}
