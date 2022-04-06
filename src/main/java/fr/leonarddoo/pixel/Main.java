package fr.leonarddoo.pixel;

import fr.leonarddoo.pixel.games.Bingo;
import fr.leonarddoo.pixel.games.CommandsPoints;
import fr.leonarddoo.pixel.games.Pendu;
import fr.leonarddoo.pixel.reputation.*;
import fr.leonarddoo.pixel.ticket.CreatePanel;
import fr.leonarddoo.pixel.ticket.CreateTicket;
import fr.leonarddoo.pixel.ticket.ManageTicket;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class Main {

    private static final String TOKEN = "";
    private static final String GUILDID = "955919690838990940";
    private static final String PIXELTEAM = "955994372137189436";
    private static final String CATEGORYGAME = "960086113345740830";

    private static final Color BLUE = new Color(90, 207, 245);

    public static void main(String[] args){
        try {
            JDABuilder.createDefault(TOKEN)
                    .addEventListeners(new CommandsBuilder())
                    .addEventListeners(new Profil())

                    .addEventListeners(new CommandsReputation())
                    .addEventListeners(new SendMessage())
                    .addEventListeners(new Invitation())

                    .addEventListeners(new CreatePanel())
                    .addEventListeners(new CreateTicket())
                    .addEventListeners(new ManageTicket())

                    .addEventListeners(new Bingo())
                    .addEventListeners(new Pendu())
                    .addEventListeners(new CommandsPoints())

                    .enableIntents(GatewayIntent.GUILD_PRESENCES)
                    .enableIntents(GatewayIntent.GUILD_INVITES)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .build();
            Membre.readList();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static String getGuildId(){
        return GUILDID;
    }

    public static String getPixelteam(){
        return PIXELTEAM;
    }

    public static Color getBlue(){
        return BLUE;
    }

    public static String getCATEGORYGAME() {
        return CATEGORYGAME;
    }
}
