package cz.vutbr.fit.pdb.nichcz.services;

/**
 * User: Marek Salát
 * Date: 27.11.13
 * Time: 21:38
 */
public class ServiceCloseException extends RuntimeException {
    public ServiceCloseException() {
    }

    public ServiceCloseException(String message) {
        super(message);
    }

    public ServiceCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceCloseException(Throwable cause) {
        super(cause);
    }

    public ServiceCloseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
