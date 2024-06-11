package domain;
public class Order extends AbstractOrder {
    private double volume = getPrice() * getQuantity();

    private double tradeVolume;

    public double getTradeVolume() {
        return this.tradeVolume;
    }
    public void setTradeVolume(double tradeVolume) {
        this.tradeVolume = tradeVolume;
    }
    public enum OrderType { BUY, SELL }

    private final OrderType type;
    private final int userId;

    public Order(int id, String symbol, OrderType type, double price, int quantity, int userId) {
        super(id, symbol, price, quantity);
        this.type = type;
        this.userId = userId;
    }

    public OrderType getType() { return type; }
    public int getUserId() { return userId; }

    public double getVolume() {
        return this.volume;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + getId() + ", symbol='" + getSymbol() + '\'' + ", type=" + type + ", price=" + getPrice() + ", quantity=" + getQuantity() + ", userId=" + userId + '}';
    }
}

