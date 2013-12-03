package cz.vutbr.fit.pdb.nichcz.services.impl;

/**
 * User: Marek Salát
 * Date: 27.11.13
 * Time: 21:29
 */
public class CreateDBconnectionException extends RuntimeException{
    public CreateDBconnectionException() {
    }

    public CreateDBconnectionException(String message) {
        super(message);
    }

    public CreateDBconnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateDBconnectionException(Throwable cause) {
        super(cause);
    }

    public CreateDBconnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
