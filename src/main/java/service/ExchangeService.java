package service;

import domain.Exchange;
import domain.Order;
import domain.OrderBook;
import domain.User;
import exceptions.ExchangeException;

import java.util.List;

public class ExchangeService {

    private static final Exchange exchange = Exchange.getInstance();

    public static void resetState() {
        Exchange.resetState();
    }

    public void matchOrders(Order buyOrder, Order sellOrder) {
        if (buyOrder.getPrice() >= sellOrder.getPrice()) {
            int quantityToTrade = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
            buyOrder.setQuantity(buyOrder.getQuantity() - quantityToTrade);
            sellOrder.setQuantity(sellOrder.getQuantity() - quantityToTrade);

            updateTradeVolume(quantityToTrade);
            System.out.println("Total trade volume after matching orders: " + exchange.getTradeVolume());
        }
    }

    private final ExchangeDataLoaderService dataLoaderService = new ExchangeDataLoaderService();
    private final ExchangeReport exchangeReport = new ExchangeReport();

    public void startExchange() {
        dataLoaderService.loadData(exchange);
    }

    public void submitOrder(Order order) throws ExchangeException {
        OrderBook orderBook = getOrderBook(order.getSymbol());
        if (orderBook == null) {
            orderBook = createOrderBook(order.getSymbol());
        }
        orderBook.addOrder(order);
    }

    private OrderBook createOrderBook(String symbol) {
        OrderBook orderBook = new OrderBook(symbol);
        exchange.addOrderBook(orderBook);
        return orderBook;
    }

    public void registerUser(User user) {
        exchange.registerUser(user);
    }

    public void generateReport() {
        exchangeReport.generateReport(exchange);
    }

    public void shutdownExchange() {
        exchange.shutdown();
    }

    public OrderBook getOrderBook(String symbol) {
        return exchange.getOrderBook(symbol);
    }

    public List<User> getAllUsers() {
        return exchange.getAllUsers();
    }

    public void matchOrders() {
        List<OrderBook> orderBooks = exchange.getAllOrderBooks();
        for (OrderBook orderBook : orderBooks) {
            List<Order> buyOrders = orderBook.getAllOrders().stream()
                    .filter(o -> o.getType() == Order.OrderType.BUY)
                    .toList();
            List<Order> sellOrders = orderBook.getAllOrders().stream()
                    .filter(o -> o.getType() == Order.OrderType.SELL)
                    .toList();

            for (Order buyOrder : buyOrders) {
                Order matchingSellOrder = sellOrders.stream()
                        .filter(o -> o.getPrice() <= buyOrder.getPrice())
                        .findFirst()
                        .orElse(null);

                if (matchingSellOrder != null) {
                    int tradeQuantity = Math.min(buyOrder.getQuantity(), matchingSellOrder.getQuantity());
                    double tradePrice = matchingSellOrder.getPrice();

                    buyOrder.setQuantity(buyOrder.getQuantity() - tradeQuantity);
                    matchingSellOrder.setQuantity(matchingSellOrder.getQuantity() - tradeQuantity);

                    updateTradeVolume(tradeQuantity);
                    System.out.println("Trade executed: " + tradeQuantity + " shares of " + buyOrder.getSymbol() + " at $" + tradePrice);
                    System.out.println("Trade volume after execution: " + exchange.getTradeVolume());
                }
            }
        }
    }

    public void addOrderBook(OrderBook orderBook) {
        exchange.addOrderBook(orderBook);
    }

    public synchronized void updateTradeVolume(double quantity) {
        System.out.println("Updating trade volume with quantity: " + quantity);
        exchange.updateTradeVolume(quantity);
        System.out.println("Updated trade volume: " + exchange.getTradeVolume());
    }

    public double getTradeVolume() {
        return exchange.getTradeVolume();
    }
}
