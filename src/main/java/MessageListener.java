import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Probably the most challenging class to build, this handles the discord chat events, it's hard coded to search for
 * my id, so you'll have to change that. Uses this pattern matching function to grab relevent text, I still don't
 * really understand it
 */
public class MessageListener extends ListenerAdapter {

            //keep it classy
    private String[] Blurbs = {
            "understood  ",
            "on it,  ",
            "of course,  ",
            "yes sir, ",
            "I will, "
    };
    private final Stock[] stocks;
    private Portfolio portfolio;

    public MessageListener(Stock[] stocks, Portfolio portfolio) {
        this.stocks = stocks;
        this.portfolio = portfolio;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String myId = "202305115103690753";   //<----- your id here

        if (event.getAuthor().getId().equals(myId)) {

            String message = event.getMessage().getContentRaw();
            // check for the string "buy" or "sell", check for any other string, then check for an int in one message
            Pattern pattern = Pattern.compile("\\b(buy|sell)\\b\\s+(.+?)\\s+(\\d+)");
            Matcher matcher = pattern.matcher(message);
            //another case where I just type sell stock, dumps all the stock
            Pattern pattern2 = Pattern.compile("\\b(sell)\\b\\s+(.+)");
            Matcher matcher2 = pattern2.matcher(message);

            if (matcher.find()) {
                String action = matcher.group(1); // "buy" or "sell"
                String symbol = matcher.group(2); // Stock symbol
                int quantity = Integer.parseInt(matcher.group(3)); // Quantity
                processStockAction(action, symbol, quantity, event.getChannel().asTextChannel());
            } else {

                if (matcher2.find()) {
                    String symbol = matcher2.group(2);
                    sellAll(symbol, event.getChannel().asTextChannel());
            }



            }
        }
        }
    private void processStockAction(String action, String symbol, int quantity, TextChannel channel) {
        Random random = new Random();
        String randomBlurb = Blurbs[random.nextInt(Blurbs.length)];
        for(Stock stock : stocks) {
            if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                if (action.equalsIgnoreCase("buy")) {
                    portfolio.buyStock(symbol, quantity);
                    channel.sendMessage(randomBlurb + " buying " + symbol +" " + quantity).queue();
                    return;

                } else if (action.equalsIgnoreCase("sell")) {
                    if (quantity > stock.getQuantity()) {
                        sendMoneyWhere(channel);
                        return;
                    }
                    portfolio.sellStock(symbol, quantity);
                    channel.sendMessage(randomBlurb + " selling " + symbol +" " + quantity).queue();
                    return;
                }
                break; // Exit the loop once the stock is found
            }
        }
        sendHuh(channel);
    }

    private void sellAll(String symbol, TextChannel channel) {
        System.out.println("sellAll called with symbol: " + symbol);
        Random random = new Random();
        String randomBlurb = Blurbs[random.nextInt(Blurbs.length)];
        for(Stock stock : stocks) {
            if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                portfolio.sellAll(symbol);
                channel.sendMessage(randomBlurb + " selling all " + symbol).queue();
                return;
            }
        }
            sendHuh(channel);
    }


    public void sendHuh(TextChannel channel) {
        if (channel != null) {
            channel.sendMessage("https://imgur.com/a/1386jhl").queue();
        } else {
            System.out.println("Channel not found!");
        }
    }

    public void sendMoneyWhere(TextChannel channel) {
        if (channel != null) {
            channel.sendMessage("https://imgur.com/a/7a4vHxc").queue();
        } else {
            System.out.println("Channel not found!");
        }
    }





}


