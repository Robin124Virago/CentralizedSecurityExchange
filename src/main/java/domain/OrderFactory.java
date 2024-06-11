package domain;

public class OrderFactory {
    private static int idCounter = 0;

    public static Order createOrder(String symbol, Order.OrderType type, double price, int quantity, int userId) {
        return new Order(++idCounter, symbol, type, price, quantity, userId);
    }
}

