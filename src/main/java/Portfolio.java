/**
 * handles a few stock purchasing actions, and is the pipeline that links the bottom of eventlistener to the top at GUI
 */
public class Portfolio {

    private Double Money;
    private Stock[] stocks;

    public Portfolio(Stock[] stocks) {
        System.out.println("Portfolio initialized");
        this.stocks = stocks;
        this.Money = 0.0;

    }



    public Double getMoney() {
        return Money;
    }
    public void setMoney(Double money) {
        Money = money;
    }

    public void buyStock(String symbol, int quantity) {
        for (Stock stock : stocks) {
            if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                double price = stock.getPrice();
                stock.addQuantity(quantity);
                Money -= price * quantity;
                System.out.println("Bought " + quantity + " shares of " + symbol + " at " + price);
                break;
            }
        }
    }

    public void sellStock(String symbol, int quantity) {
        for (Stock stock : stocks) {
            if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                double price = stock.getPrice();
                stock.addQuantity(-quantity);
                Money += price * quantity;
                break;
            }
        }
    }

    public void sellAll (String symbol) {
        for (Stock stock : stocks) {
            if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                double price = stock.getPrice();
                int quantity = stock.getQuantity();
                stock.addQuantity(-quantity);
                Money += price * quantity;
                break;
            }
        }
    }


}
