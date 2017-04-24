package authenticate.exception;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException(String errorMessage) {
        super(errorMessage);
    }
}