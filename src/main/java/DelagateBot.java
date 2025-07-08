import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

/**
 * this guy sits in the Discord and waits for my command, most of the event functionality is in Message listener though
 * I think he wraps it, I think
 */
public class DelagateBot extends Thread {
    private final String channelId = "1360403078431641613";
    private String token;
    private String message;
    private Portfolio portfolio;
    private Stock[] stocks;
    //I think shards allow you to run multiple instances of the bot on different servers, I never did this but the
    //youtube guy did and it works anyways so I won't change it
    private final ShardManager shardManager;

    public DelagateBot(Stock[] stocks, Portfolio portfolio) throws LoginException {
        this.portfolio = portfolio;

        String token = ""; // key goes here but I killed it
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Fall Guys"));
        shardManager = builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT).build();
        JDA jda = shardManager.getShardById(0);
        this.stocks = stocks;
        try {
            if (jda != null) {
                jda.awaitReady();
                System.out.println("Bot is ready!");
            } else {
                System.out.println("Shard not found!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        registerListener();

    }
    public void registerListener() {
        shardManager.addEventListener(new MessageListener(stocks, portfolio));
    }

    @Override
    public void run() {
        System.out.println("hello");
    }

        }

