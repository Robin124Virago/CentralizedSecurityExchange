package service;

import domain.Exchange;
import domain.Order;
import domain.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExchangeReport {

    public void generateReport(Exchange exchange) {
        System.out.println("Generating exchange report...");
        List<Order> allOrders = exchange.getAllOrders();
        System.out.println("Total Orders: " + allOrders.size());

        List<User> allUsers = exchange.getAllUsers();
        System.out.println("Total Users: " + allUsers.size());

        double totalTradeVolume = allOrders.stream()
                .mapToDouble(order -> order.getPrice() * order.getQuantity())
                .sum();
        System.out.println("Total Trade Volume: " + totalTradeVolume);

        String mostTradedStock = allOrders.stream()
                .collect(Collectors.groupingBy(Order::getSymbol, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No trades");
        System.out.println("Most Traded Stock: " + mostTradedStock);

        int mostActiveUserId = allOrders.stream()
                .collect(Collectors.groupingBy(Order::getUserId, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(-1);
        String mostActiveUser = (mostActiveUserId == -1) ? "No active users" : String.valueOf(mostActiveUserId);
        System.out.println("Most Active User: " + mostActiveUser);
    }
}
