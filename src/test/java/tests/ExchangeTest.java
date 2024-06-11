package tests;

import domain.*;
import exceptions.ExchangeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ExchangeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeTest {
    private ExchangeService exchangeService;

    @BeforeEach
    void setUp() {
        ExchangeService.resetState();
        exchangeService = new ExchangeService();
        exchangeService.startExchange();
    }

    @AfterEach
    void tearDown() {
        exchangeService.shutdownExchange();
    }

    @Test
    void testOrderMatching() throws ExchangeException {
        User user1 = UserFactory.createUser("Alice");
        User user2 = UserFactory.createUser("Bob");
        exchangeService.registerUser(user1);
        exchangeService.registerUser(user2);

        Order buyOrder = OrderFactory.createOrder("AAPL", Order.OrderType.BUY, 150.0, 10, user1.getId());
        Order sellOrder = OrderFactory.createOrder("AAPL", Order.OrderType.SELL, 149.0, 5, user2.getId());

        exchangeService.submitOrder(buyOrder);
        exchangeService.submitOrder(sellOrder);

        // Call the matchOrders method
        exchangeService.matchOrders();

        assertEquals(5, buyOrder.getQuantity(), "Buy order quantity should be updated after matching");
        assertEquals(5, sellOrder.getQuantity(), "Sell order quantity should be updated after matching");
    }

    @Test
    void testUserRegistration() {
        User user = UserFactory.createUser("Charlie");
        exchangeService.registerUser(user);

        List<User> users = exchangeService.getAllUsers();
        assertTrue(users.contains(user), "User should be registered in the exchange");
    }

    @Test
    void testOrderSubmission() throws ExchangeException {
        User user = UserFactory.createUser("David");
        exchangeService.registerUser(user);

        Order buyOrder = OrderFactory.createOrder("AAPL", Order.OrderType.BUY, 150.0, 10, user.getId());
        exchangeService.submitOrder(buyOrder);

        // Ensure that the order book for "AAPL" is created
        exchangeService.matchOrders(buyOrder, buyOrder);

        List<Order> orders = exchangeService.getOrderBook("AAPL").getAllOrders();
        assertTrue(orders.contains(buyOrder), "Buy order should be submitted to the order book");
    }

    @Test
    void testReportGeneration() {
        exchangeService.generateReport();
    }

    @Test
    void testGetAllUsers() {
        User user1 = UserFactory.createUser("Alice");
        User user2 = UserFactory.createUser("Bob");
        exchangeService.registerUser(user1);
        exchangeService.registerUser(user2);
        List<User> users = exchangeService.getAllUsers();
        assertTrue(users.contains(user1) && users.contains(user2), "All registered users should be retrieved");
    }

    @Test
    void testGetOrderBook() {
        OrderBook orderBook = new OrderBook("AAPL");
        exchangeService.addOrderBook(orderBook);
        OrderBook retrievedOrderBook = exchangeService.getOrderBook("AAPL");
        assertEquals(orderBook, retrievedOrderBook, "The correct order book should be retrieved");
    }

    @Test
    void testRegisterUser() {
        User user = UserFactory.createUser("Charlie");
        exchangeService.registerUser(user);
        List<User> users = exchangeService.getAllUsers();
        assertTrue(users.contains(user), "User should be registered in the exchange");
    }

    @Test
    void testResetState() {
        User user = UserFactory.createUser("Charlie");
        exchangeService.registerUser(user);
        ExchangeService.resetState();
        List<User> users = exchangeService.getAllUsers();
        assertTrue(users.isEmpty(), "All users should be removed after reset");
    }
}
