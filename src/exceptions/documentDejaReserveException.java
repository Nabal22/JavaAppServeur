package exceptions;

import java.util.Date;

public class documentDejaReserveException extends Exception {
    public documentDejaReserveException() {
        super("Le document est déjà reserve.");
    }
}
