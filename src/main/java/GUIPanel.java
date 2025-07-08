import javax.swing.*;
import java.awt.*;
import javax.security.auth.login.LoginException;

/**
 * this is the top level class besides main, it floats on top of all the threads that write to objects
 * in the main loop, it first updates all its variables from the objects below, NOT the threads
 * then it repaints and gets slotted into the GUI frame
 * this is also the class that Initializes HookThread
 */
public class GUIPanel extends JPanel {
    private Stock[] stocks;
    private HookThread hookThread;

    private double netWorth;
    private double money;
    private double stockValue;

    public GUIPanel() {

        this.stocks = new Stock[9];
        setPreferredSize(new Dimension(400, 200));
        this.hookThread = new HookThread();



        initStocks(); // Initialize the stocks array
        hookThread.start();


    }

    public void initStocks() {
        // Initialize the stocks array with some dummy values
        for (int i = 0; i < stocks.length; i++) {
            stocks[i] = new Stock("Stock " + (i + 1), 0.0, 0, 0.0);
        }
    }

    public void updateStocksFromThreads() {
            // Retrieve the updated stocks from the DataThread in HookThread
            Stock[] updatedStocks = hookThread.getStocks();

            // Update the stocks array in GUIPanel with thread stuff
            for (int i = 0; i < stocks.length && i < updatedStocks.length; i++) {
                if (updatedStocks[i] != null) {
                    stocks[i].setSymbol(updatedStocks[i].getSymbol());
                    stocks[i].setPrice(updatedStocks[i].getPrice());
                    stocks[i].setChange(updatedStocks[i].getChange());
                    stocks[i].setQuantity(updatedStocks[i].getQuantity());

                }
            }
    }

    //this is the only math this class does, calculates the totals for the bottom frame of the GUI
    public void netWorthEct() {
        money = hookThread.getDataThread().getPortfolio().getMoney();
        double totalValue = 0.0;
        for (Stock stock : stocks) {
            if (stock != null) {
                totalValue += stock.getPrice() * stock.getQuantity();
            }
        }
        this.stockValue = totalValue;
        this.netWorth = this.money + this.stockValue;
    }

    //this paints the whole GUI, stocks are lain out in a grid and the total box placed at the bottom
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color myGreen = new Color(3, 123, 102);
        Color myRed = new Color(214, 10, 34);

        g2d.setFont(new Font("Arial", Font.BOLD, 20));

        // Draw the array of symbols, prices, change percent, quantity, and total value in a grid layout
        int bottomBoxHeight = 120; // Height of the bottom box with padding
        int availableHeight = getHeight() - bottomBoxHeight;

        int gridSize = (int) Math.ceil(Math.sqrt(stocks.length));
        int cellWidth = getWidth() / gridSize;
        int cellHeight = availableHeight / gridSize;

        for (int i = 0; i < stocks.length; i++) {
            String symbolText = stocks[i].getSymbol();
            String priceText = String.format("%.2f", stocks[i].getPrice());
            String changeText = String.format("%.2f", stocks[i].getChange());
            String combinedText = priceText + " (" + changeText + "%)";
            String quantityAndTotal = stocks[i].getQuantity() + " | " + String.format("%.2f", stocks[i].getPrice() * stocks[i].getQuantity());
            int row = i / gridSize;
            int col = i % gridSize;

            int symbolX = col * cellWidth + (cellWidth - g2d.getFontMetrics().stringWidth(symbolText)) / 2;
            int symbolY = row * cellHeight + (cellHeight / 2) - g2d.getFontMetrics().getAscent();


            int combinedX = col * cellWidth + (cellWidth - g2d.getFontMetrics().stringWidth(combinedText)) / 2;
            int combinedY = row * cellHeight + (cellHeight / 2) + g2d.getFontMetrics().getAscent();


            int quantityAndTotalX = col * cellWidth + (cellWidth - g2d.getFontMetrics().stringWidth(quantityAndTotal)) / 2;
            int quantityAndTotalY = combinedY + g2d.getFontMetrics().getHeight();


            g2d.setColor(Color.BLACK);
            g2d.drawString(symbolText, symbolX, symbolY);


            if (stocks[i].getChange() >= 0) {
                g2d.setColor(myGreen);
            } else {
                g2d.setColor(myRed);
            }


            g2d.drawString(combinedText, combinedX, combinedY);


            g2d.setColor(Color.BLACK);
            g2d.drawString(quantityAndTotal, quantityAndTotalX, quantityAndTotalY);
        }

        // Draw the bottom box
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setColor(Color.BLACK);

        String moneyText = "Money: $" + String.format("%.2f", money);
        String netWorthText = "Net Worth: $" + String.format("%.2f", netWorth);
        String stockValueText = "Stock Value: $" + String.format("%.2f", stockValue);

        int boxWidth = 400;
        int boxHeight = 100;
        int boxX = (getWidth() - boxWidth) / 2;
        int boxY = getHeight() - boxHeight - 20;

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(boxX, boxY, boxWidth, boxHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(boxX, boxY, boxWidth, boxHeight);

        int textX = boxX + 20;
        int moneyY = boxY + 30;
        int stockValueY = boxY + 60;
        int netWorthY = boxY + 90;

        g2d.drawString(moneyText, textX, moneyY);
        g2d.drawString(stockValueText, textX, stockValueY);
        g2d.drawString(netWorthText, textX, netWorthY);
    }
}