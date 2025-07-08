/**
 * this class stores most the information that gets manipulated and displayed, the threads all share an array of
 * stocks to play with, and the GUI has its own clone array so that it can social distance the threads
 */

public class Stock {
    private String symbol;
    private double price;
    private double change;
    private double lastChangePing;
    private int quantity;

    public Stock(String symbol, double price, int quantity, double change) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.change = change;
    }

    public synchronized double getChange() {
        return change;
    }
    public synchronized String getChangeString() {
        String changeText = String.format("%.2f", this.getChange());
        return changeText;
    }

    public synchronized void setChange(double change) {
        this.change = change;
    }

    public synchronized double getLastChangePing() {
        return lastChangePing;
    }

    public synchronized void setLastChangePing(double lastChangePing) {
        this.lastChangePing = lastChangePing;
    }

    public synchronized void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public synchronized String getSymbol() {
        return symbol;
    }

    public synchronized double getPrice() {
        return price;
    }

    public synchronized void setPrice(double price) {
        this.price = price;
    }

    public synchronized int getQuantity() {
        return quantity;
    }

    public synchronized void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public synchronized Boolean addQuantity(int quantity) {
        if (this.quantity + quantity < 0) {
            return false;
        }
        this.quantity += quantity;
        return true;
    }

}