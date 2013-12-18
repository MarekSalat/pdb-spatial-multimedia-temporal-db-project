package cz.vutbr.fit.pdb.nichcz.services;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 22:50
 *
 * Trida pro vyjimku nenalezeni sluzby.
 */
public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException() {
    }

    /**
     * Nova vyjimka.
     * @param message Zprava.
     */
    public ServiceNotFoundException(String message) {
        super(message);
    }

    /**
     * Nova vyjimka.
     * @param message Zprava.
     * @param cause Duvod.
     */
    public ServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Nova vyjimka.
     * @param cause Duvod.
     */
    public ServiceNotFoundException(Throwable cause) {
        super(cause);
    }

    public ServiceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
