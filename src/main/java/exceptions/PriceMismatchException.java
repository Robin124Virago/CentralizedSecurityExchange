package exceptions;

public class PriceMismatchException extends ExchangeException {
    public PriceMismatchException(String message) {
        super(message);
    }
}

