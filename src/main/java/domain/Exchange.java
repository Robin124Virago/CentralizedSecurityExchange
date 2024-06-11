package domain;

import exceptions.ExchangeException;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Exchange {
    private static Exchange instance;

    public Exchange() {
    }

    public static synchronized Exchange getInstance() {
        if (instance == null) {
            instance = new Exchange();
        }
        return instance;
    }

    private static final ConcurrentHashMap<String, OrderBook> orderBooks = new ConcurrentHashMap<>();
    private static UserCache userCache = new UserCache();
    private static ExecutorService executor = Executors.newFixedThreadPool(4);
    private volatile double tradeVolume = 0.0;

    public static void resetState() {
        orderBooks.clear();
        userCache = new UserCache();
        executor.shutdown();
        executor = Executors.newFixedThreadPool(4);
    }

    public void registerUser(User user) {
        userCache.addUser(user);
    }

    public void processOrder(Order order) throws ExchangeException {
        orderBooks.computeIfAbsent(order.getSymbol(), OrderBook::new).addOrder(order);
        User user = userCache.getUser(order.getUserId());
        if (user != null) {
            user.addOrder(order);
        }
    }

    public void submitOrder(Order order) {
        executor.submit(() -> {
            try {
                processOrder(order);
            } catch (ExchangeException e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
        try {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getAllOrders() {
        return orderBooks.values().stream()
                .flatMap(orderBook -> orderBook.getAllOrders().stream())
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userCache.getAllUsers();
    }

    public void removeUser(int id) {
        userCache.removeUser(id);
    }

    public OrderBook getOrderBook(String symbol) {
        return orderBooks.get(symbol);
    }

    public void addOrderBook(OrderBook orderBook) {
        orderBooks.put(orderBook.getSymbol(), orderBook);
    }

    public List<OrderBook> getAllOrderBooks() {
        return orderBooks.values().stream().collect(Collectors.toList());
    }

    public synchronized void updateTradeVolume(double quantity) {
        System.out.println("Before update: " + tradeVolume);
        tradeVolume += quantity;
        System.out.println("After update: " + tradeVolume);
    }

    public double getTradeVolume() {
        System.out.println("Getting trade volume: " + tradeVolume);
        return tradeVolume;
    }
}
