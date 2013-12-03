package cz.vutbr.fit.pdb.nichcz.services;

/**
 * User: Marek Salát
 * Date: 6.10.13
 * Time: 22:50
 */
public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException() {
    }

    public ServiceNotFoundException(String message) {
        super(message);
    }

    public ServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceNotFoundException(Throwable cause) {
        super(cause);
    }

    public ServiceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
