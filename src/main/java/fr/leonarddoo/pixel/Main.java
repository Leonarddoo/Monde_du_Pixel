package fr.leonarddoo.pixel;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main {

    private static final String TOKEN = "OTU2Njk5MjIyMDAwOTUxMzA3.Yj0BlQ.KUBM3ARne1dfMRi8TrvOW11nEXE";
    private static final String GUILDID = "955919690838990940";

    public static void main(String[] args){
        try {
            JDABuilder.createDefault(TOKEN)
                    .addEventListeners(new CommandsBuilder())
                    .addEventListeners(new CommandsUse())
                    .addEventListeners(new SendMessage())
                    .addEventListeners(new Invitation())

                    .enableIntents(GatewayIntent.GUILD_PRESENCES)
                    .enableIntents(GatewayIntent.GUILD_INVITES)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .build();
            Membre.readList();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static String getGuildid(){
        return GUILDID;
    }
}
