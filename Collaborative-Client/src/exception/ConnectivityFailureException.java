package exception;

public class ConnectivityFailureException extends Exception{
    public ConnectivityFailureException(String error) {
        super(error);
    }
}
