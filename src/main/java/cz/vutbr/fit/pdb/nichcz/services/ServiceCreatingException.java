package cz.vutbr.fit.pdb.nichcz.services;

/**
 * User: Marek Salát
 * Date: 27.11.13
 * Time: 21:23
 */
public class ServiceCreatingException extends RuntimeException {
    public ServiceCreatingException() {
    }

    public ServiceCreatingException(String message) {
        super(message);
    }

    public ServiceCreatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceCreatingException(Throwable cause) {
        super(cause);
    }

    public ServiceCreatingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
