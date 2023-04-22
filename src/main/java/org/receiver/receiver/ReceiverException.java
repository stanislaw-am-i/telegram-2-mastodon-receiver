package org.receiver.receiver;

public class ReceiverException extends Exception {

    public ReceiverException(String error) {
        super(error);
    }

    public ReceiverException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReceiverException(Throwable cause) {
        super(cause);
    }

}
