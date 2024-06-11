package domain;

import email.Email;
import email.EmailException;
import email.EmailService;
import email.NotificationQueue;
import exceptions.ExchangeException;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderBook {
    private final String symbol;
    private final PriorityQueue<Order> buyOrders;
    private final PriorityQueue<Order> sellOrders;
    private final ReentrantLock lock = new ReentrantLock();
    private final NotificationQueue notificationQueue = new NotificationQueue();
    private final EmailService emailService = new EmailService();
    private final ExecutorService emailExecutor = Executors.newSingleThreadExecutor();

    public OrderBook(String symbol) {
        this.symbol = symbol;
        this.buyOrders = new PriorityQueue<>((o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice()));
        this.sellOrders = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice));

        // Start the email sender thread
        emailExecutor.submit(() -> {
            while (true) {
                try {
                    Email email = notificationQueue.takeEmail();
                    emailService.sendEmail(email);
                } catch (InterruptedException | EmailException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addOrder(Order order) throws ExchangeException {
        lock.lock();
        try {
            if (order.getType() == Order.OrderType.BUY) {
                buyOrders.add(order);
            } else {
                sellOrders.add(order);
            }
            matchOrders();
        } finally {
            lock.unlock();
        }
    }

    private void matchOrders() throws ExchangeException {
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            Order buyOrder = buyOrders.peek();
            Order sellOrder = sellOrders.peek();

            if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                int quantityToMatch = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());

                if (buyOrder.getQuantity() == quantityToMatch) {
                    buyOrders.poll();
                } else {
                    buyOrder.setQuantity(buyOrder.getQuantity() - quantityToMatch);
                }

                if (sellOrder.getQuantity() == quantityToMatch) {
                    sellOrders.poll();
                } else {
                    sellOrder.setQuantity(sellOrder.getQuantity() - quantityToMatch);
                }

                System.out.println("Matched Orders: " + buyOrder + " with " + sellOrder);

                Email buyOrderEmail = new Email(
                        "buyer@example.com",
                        "Order Matched",
                        "Your buy order has been matched with a sell order."
                );
                Email sellOrderEmail = new Email(
                        "seller@example.com",
                        "Order Matched",
                        "Your sell order has been matched with a buy order."
                );
                notificationQueue.addEmail(buyOrderEmail);
                notificationQueue.addEmail(sellOrderEmail);
            } else {
                break;
            }
        }
    }

    public List<Order> getAllOrders() {
        lock.lock();
        try {
            return Stream.concat(buyOrders.stream(), sellOrders.stream())
                    .collect(Collectors.toList());
        } finally {
            lock.unlock();
        }
    }

    public List<Order> getOrdersByType(Order.OrderType type) {
        lock.lock();
        try {
            return (type == Order.OrderType.BUY ? buyOrders.stream() : sellOrders.stream())
                    .collect(Collectors.toList());
        } finally {
            lock.unlock();
        }
    }

    public List<Order> getOrdersAbovePrice(double price) {
        lock.lock();
        try {
            return Stream.concat(buyOrders.stream(), sellOrders.stream())
                    .filter(order -> order.getPrice() > price)
                    .collect(Collectors.toList());
        } finally {
            lock.unlock();
        }
    }

    public String getSymbol() {
        return this.symbol;
    }
}
