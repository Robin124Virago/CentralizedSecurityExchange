package employees;

import domain.Exchange;
import domain.Order;
import exceptions.ExchangeException;

public class OrderSpecialist extends ExchangeEmployee {
    public OrderSpecialist(int id, String name) {
        super(id, name);
    }

    public void processOrder(Order order, Exchange exchange) throws ExchangeException {
        exchange.processOrder(order);
    }
}

