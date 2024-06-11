package exceptions;

public class OrderExistsException extends ExchangeException {
    public OrderExistsException(String message) {
        super(message);
    }
}

