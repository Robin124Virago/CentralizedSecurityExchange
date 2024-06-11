# CentralizedSecurityExchange

CentralizedSecurityExchange is a Java application that simulates a centralized security exchange. It allows users to submit buy and sell orders for different stocks, matches these orders based on certain criteria, and updates the total trade volume.

## Features

- **User Registration**: Users can be registered to the exchange and can submit orders.
- **Order Processing**: Orders submitted by users are processed and added to the order book corresponding to the stock symbol.
- **Order Matching**: Buy and sell orders are matched based on their quantities. When a match is found, the quantities of the orders are updated.
- **Trade Volume Update**: The total trade volume is updated whenever orders are matched.
- **Exchange Report**: A report can be generated to provide information about the total number of orders, total number of users, total trade volume, most traded stock, and most active user.

## Classes

- **Exchange**: This is the main class that handles user registration, order processing, and trade volume updates. It maintains a list of all order books and users.
- **OrderBook**: This class represents an order book for a specific stock symbol. It maintains a list of all buy and sell orders for that stock.
- **Order**: This class represents an order submitted by a user. It contains information such as the user ID, stock symbol, order type (buy or sell), price, and quantity.
- **User**: This class represents a user of the exchange. It maintains a list of all orders submitted by the user.
- **ExchangeReport**: This class generates a report of the exchange, providing information about the total number of orders, total number of users, total trade volume, most traded stock, and most active user.

## How to Run

This is a Maven project and can be built and run using the Maven command-line tool. Use the following commands to build and run the project:

```bash
mvn clean install
mvn exec:java
