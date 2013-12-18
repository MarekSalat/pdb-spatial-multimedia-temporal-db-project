package cz.vutbr.fit.pdb.nichcz.services.impl;

/**
 * User: Marek Salát
 * Date: 27.11.13
 * Time: 21:29
 *
 * Trida pro vyjimku vytvoreni spojeni.
 */
public class CreateDBconnectionException extends RuntimeException{
    public CreateDBconnectionException() {
    }

    /**
     * Nova vyjimka.
     * @param message Zprava.
     */
    public CreateDBconnectionException(String message) {
        super(message);
    }

    /**
     * Nova vyjimka.
     * @param message Zprava.
     * @param cause Duvod.
     */
    public CreateDBconnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Nova vyjimka.
     * @param cause Duvod.
     */
    public CreateDBconnectionException(Throwable cause) {
        super(cause);
    }

    public CreateDBconnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
