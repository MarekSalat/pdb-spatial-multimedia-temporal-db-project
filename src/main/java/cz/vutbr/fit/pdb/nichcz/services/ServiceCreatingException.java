package cz.vutbr.fit.pdb.nichcz.services;

/**
 * User: Marek Salát
 * Date: 27.11.13
 * Time: 21:23
 *
 * Trida pro vyjimku vytvoreni sluzby.
 */
public class ServiceCreatingException extends RuntimeException {
    public ServiceCreatingException() {
    }

    /**
     * Nova vyjimka.
     * @param message Zprava.
     */
    public ServiceCreatingException(String message) {
        super(message);
    }

    /**
     * Nova vyjimka.
     * @param message Zprava.
     * @param cause Duvod.
     */
    public ServiceCreatingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Nova vyjimka.
     * @param cause Duvod.
     */
    public ServiceCreatingException(Throwable cause) {
        super(cause);
    }

    public ServiceCreatingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
