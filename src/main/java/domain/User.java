package domain;

import java.util.*;

public class User {
    private final int id;
    private final String name;
    private final List<Order> orders;
    private final Map<String, Integer> portfolio;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.orders = new ArrayList<>();
        this.portfolio = new HashMap<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<Order> getOrders() { return orders; }
    public Map<String, Integer> getPortfolio() { return portfolio; }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void updatePortfolio(String symbol, int quantity) {
        portfolio.put(symbol, portfolio.getOrDefault(symbol, 0) + quantity);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + name + '\'' + ", orders=" + orders + ", portfolio=" + portfolio + '}';
    }
}
