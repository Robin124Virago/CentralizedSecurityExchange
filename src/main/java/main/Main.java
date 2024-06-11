package main;

import domain.Order;
import domain.OrderFactory;
import domain.User;
import domain.UserFactory;
import exceptions.ExchangeException;
import service.ExchangeService;

public class Main {
    public static void main(String[] args) throws ExchangeException {
        ExchangeService exchangeService = new ExchangeService();
        exchangeService.startExchange();

        User user1 = UserFactory.createUser("Alice");
        User user2 = UserFactory.createUser("Bob");
        User user3 = UserFactory.createUser("Charlie");
        User user4 = UserFactory.createUser("David");
        User user5 = UserFactory.createUser("Eve");

        exchangeService.registerUser(user1);
        exchangeService.registerUser(user2);
        exchangeService.registerUser(user3);
        exchangeService.registerUser(user4);
        exchangeService.registerUser(user5);

        Order buyOrder1 = OrderFactory.createOrder("AAPL", Order.OrderType.BUY, 150.0, 10, user1.getId());
        Order sellOrder1 = OrderFactory.createOrder("AAPL", Order.OrderType.SELL, 149.0, 5, user2.getId());

        Order buyOrder2 = OrderFactory.createOrder("AAPL", Order.OrderType.BUY, 155.0, 15, user3.getId());
        Order sellOrder2 = OrderFactory.createOrder("AAPL", Order.OrderType.SELL, 160.0, 20, user4.getId());

        // This order won't be matched with any other order
        Order buyOrder3 = OrderFactory.createOrder("AAPL", Order.OrderType.BUY, 200.0, 25, user5.getId());

        exchangeService.submitOrder(buyOrder1);
        exchangeService.submitOrder(sellOrder1);
        exchangeService.submitOrder(buyOrder2);
        exchangeService.submitOrder(sellOrder2);
        exchangeService.submitOrder(buyOrder3);

        // Match the orders
        exchangeService.matchOrders();

        // Allow some time for processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        exchangeService.generateReport();
        exchangeService.shutdownExchange();
    }
}