package cz.vutbr.fit.pdb.nichcz.services;

/**
 * User: Marek Salát
 * Date: 27.11.13
 * Time: 21:38
 *
 * Trida pro vyjimku zavreni sluzby.
 */
public class ServiceCloseException extends RuntimeException {
    public ServiceCloseException() {
    }

    /**
     * Nova vyjimka.
     * @param message Zprava.
     */
    public ServiceCloseException(String message) {
        super(message);
    }

    /**
     * Nova vyjimka.
     * @param message Zprava.
     * @param cause Duvod.
     */
    public ServiceCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Nova vyjimka.
     * @param cause Duvod.
     */
    public ServiceCloseException(Throwable cause) {
        super(cause);
    }

    public ServiceCloseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
