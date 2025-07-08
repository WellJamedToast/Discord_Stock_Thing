import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.util.Random;

/**
 * Notifies me through discord when the data thread finds a significant stock shift, or in this case an insignificant
 * stock shift for the purpose of the presentation, has a randomized blurb appended to the start of the message to
 * keep it classy
 */
public class InformerBot extends Thread {
    private String token;
    private String channelId;
    private String[] moneyUp;
    private String[] moneyVeryUp;
    private String[] moneyDown;
    private String[] moneyVeryDown;


    private final ShardManager shardManager;

    public InformerBot( ) throws LoginException {
        channelId = "1360403078431641613";
        token = ""; //key here but I killed it
        moneyUp = new String[]{
                "Sir, line go up.  ",
                "Money up, sir  ",
                "Consider selling...  "
        };

        moneyVeryUp = new String[]{
                "money VERY up.  ",
                "WE SELL.  ",
                "SELL NOW  "
        };

        moneyDown = new String[] {
                "Line go down..  ",
                "Keep an eye on this.  ",
                "Consider buying.  "
        };

        moneyVeryDown = new String[]{
                "This one SIR!  ",
                "BIG DIP  ",
                "Buy dip now.  "
        };

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("The Market"));
        shardManager = builder.enableIntents(GatewayIntent.GUILD_MESSAGES).build();
    }

    //I think these could be one much smaller method if i played it smart, but perhaps it's more readable this way
    public void moneyUpMessage(Stock stock) {

        // Select a random message
        Random random = new Random();
        String randomMessage = moneyUp[random.nextInt(moneyUp.length)] + " " + stock.getSymbol() + " " + stock.getChangeString() + "%\n";

        // Send the message
        System.out.println(randomMessage);
        TextChannel channel = shardManager.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage(randomMessage).queue();
        } else {
            System.out.println("Channel not found!");
        }
    }

    public void moneyVeryUpMessage(Stock stock) {
        // Select a random message
        Random random = new Random();
        String randomMessage = moneyVeryUp[random.nextInt(moneyVeryUp.length)] + " " + stock.getSymbol() + " " + stock.getChangeString() + "%\n";
        // Send the message
        System.out.println(randomMessage);
        TextChannel channel = shardManager.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage(randomMessage).queue();
        } else {
            System.out.println("Channel not found!");
        }
    }

    public void moneyDownMessage(Stock stock) {
        // Select a random message
        Random random = new Random();
        String randomMessage = moneyDown[random.nextInt(moneyDown.length)] + " " + stock.getSymbol() + " " + stock.getChangeString() + "%\n";
        // Send the message
        System.out.println(randomMessage);
        TextChannel channel = shardManager.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage(randomMessage).queue();
        } else {
            System.out.println("Channel not found!");
        }
    }

    public void moneyVeryDownMessage(Stock stock) {
        // Select a random message
        Random random = new Random();
        String randomMessage = moneyVeryDown[random.nextInt(moneyVeryDown.length)] + " " + stock.getSymbol() + " " + stock.getChangeString() + "%\n";
        // Send the message
        System.out.println(randomMessage);
        TextChannel channel = shardManager.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage(randomMessage).queue();
        } else {
            System.out.println("Channel not found!");
        }
    }

    @Override
    public void run() {
    }
}
