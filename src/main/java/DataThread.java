import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;
import javax.security.auth.login.LoginException;

/**
 * This thread takes the json from above and parses it, jsons are like a tree structure or directory, and you go
 * down the right paths to get the info you need, then go to the root and try again for the next branch
 * it's looking for regularMarketValue regularMarketChange and the symbol, it then maps these to matching
 * symbols in the Stocks[] array (first cycle initializes the nulls) this array of stocks is shared between all the
 * threads, but not the GUI
 *
 * from here I initialize the discord bots because the Informer gets sent the change values to spam me with, and the
 * Delagate needs a friend so I put him here too
 */
public class DataThread extends Thread{
    private Stock[] stocks;
    private InformerBot informerBot;
    private DelagateBot delagateBot;
    private Portfolio portfolio;



    public DataThread() {
        this.stocks = new Stock[9];
        this.portfolio = new Portfolio(stocks);
        try {
            this.informerBot = new InformerBot();
            this.delagateBot = new DelagateBot(this.stocks, this.portfolio);
        }catch (LoginException e){
            e.printStackTrace();
        }
    }


    public void loadStockDataFromJson(String json) {
        try {
            JsonObject rootObject = JsonParser.parseString(json).getAsJsonObject();
            JsonArray resultArray = rootObject.getAsJsonObject("quoteResponse").getAsJsonArray("result");

            for (int i = 0; i < resultArray.size() && i < stocks.length; i++) {
                JsonObject stockJson = resultArray.get(i).getAsJsonObject();

                String symbol = stockJson.get("symbol").getAsString();
                double price = stockJson.get("regularMarketPrice").getAsDouble();
                double changePercent = stockJson.get("regularMarketChangePercent").getAsDouble();

                if (stocks[i] == null) {
                    stocks[i] = new Stock(symbol, price, 0, changePercent); // Example usage of changePercent
                } else {
                    stocks[i].setPrice(price);
                    stocks[i].setChange(changePercent); // Example usage of changePercent
                }

            }
            hasSignificantChange();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasSignificantChange() {
        synchronized (stocks) {
            for (Stock stock : stocks) {
                if (stock != null) {
                    double difference = stock.getChange() - stock.getLastChangePing();
                    if (difference > .25) {
                        if (stock.getChange() < 3.5) {
                            informerBot.moneyUpMessage(stock);
                            stock.setLastChangePing(stock.getChange());
                        }
                        else{
                            informerBot.moneyVeryUpMessage(stock);
                        stock.setLastChangePing(stock.getChange());
                    }
                    }
                    if (difference < -.25) {
                        if (stock.getChange() > -1){
                            informerBot.moneyDownMessage(stock);
                        stock.setLastChangePing(stock.getChange());
                    }
                        else{
                            informerBot.moneyVeryDownMessage(stock);
                stock.setLastChangePing(stock.getChange());
            }
                        return true;
                    }

                }
            }
        }
        return false;
    }

    public Stock[] getStocks() {
        return stocks;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    @Override
    public void run() {
        System.out.println("DataThread started");
    }


}
