package domain;

public abstract class AbstractOrder {
    private final int id;
    private final String symbol;
    private final double price;
    private int quantity;

    public AbstractOrder(int id, String symbol, double price, int quantity) {
        this.id = id;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", symbol='" + symbol + '\'' + ", price=" + price + ", quantity=" + quantity + '}';
    }

}