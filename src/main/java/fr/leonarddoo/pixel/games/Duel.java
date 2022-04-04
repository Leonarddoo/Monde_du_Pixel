package fr.leonarddoo.pixel.games;

import fr.leonarddoo.pixel.Main;
import fr.leonarddoo.pixel.Membre;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Duel extends ListenerAdapter {

    private static int mise;
    private static String j1;
    private static String j2;
    private static String msg;
    private static int tour;
    private static int number;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!event.getName().equals("duel")) return;

        int montant = event.getOption("montant").getAsInt();

        if(montant < 1 || montant > 3){
            event.reply("La mise doit être comprise entre 1 et 3 (inclus)").queue();
            return;
        }

        Membre m = Membre.getMembre(event.getUser().getId());

        if(!(m.getPoints() >= montant)){
            event.replyEmbeds(new EmbedBuilder()
                            .setDescription("Vous n'avez pas assez de jetons pour faire cela.")
                            .setColor(Main.getBlue())
                    .build()).queue();
            return;
        }

        m.removePoints(montant);
        mise = montant;
        j1 = event.getUser().getId();
        j2 = event.getOption("membre").getAsUser().getId();

        event.getOption("membre").getAsUser().openPrivateChannel().queue(c -> {
            c.sendMessageEmbeds(new EmbedBuilder()
                    .setTitle("Bingo")
                    .setDescription(event.getUser().getAsMention()+" veut vous défier dans un duel dans l'**Arcade du Pixel** \n" +
                            "\n" +
                            "Vous miserez <:jeu:959785480227020800> "+mise+" **Point(s) de Jeu** chacun. Si l'un d'entre vous gagne, il prend le tout ! \n" +
                            "\n" +
                            "Acceptez-vous ce défi ?")
                    .setColor(Main.getBlue())
                    .build()).setActionRow(Button.success("accepter", "Accepter"), Button.danger("refuser", "Refuser")).queue();
        });
        event.replyEmbeds(new EmbedBuilder()
                        .setColor(Main.getBlue())
                        .setDescription("<:oui:958523955285721188> Votre demande de duel à bien été envoyé à "+event.getOption("membre").getAsMember().getAsMention())
                .build()).queue();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        if(!(buttonId.equals("refuser") || buttonId.equals("accepter"))) return;
        if(!(event.getUser().getId().equals(j2)) || msg != null) return;

        if(buttonId.equals("refuser")){
            event.getJDA().getGuildById(Main.getGuildId()).getTextChannelById(Main.getDUELCHANNELID()).sendMessageEmbeds(new EmbedBuilder()
                            .setColor(Main.getBlue())
                            .setDescription("<:non:958523955126358086> "+event.getUser().getAsMention()+" a refusé le duel contre <@"+j1+">")
                    .build()).queue();
            Membre.getMembre(j1).addPoints(mise);
            reset();
            return;
        }

        event.getJDA().getGuildById(Main.getGuildId()).getTextChannelById(Main.getDUELCHANNELID()).sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Main.getBlue())
                        .setDescription("<:bingo:960318955623432222> Jeu du Bingo \n" +
                                "\n" +
                                "**Joueur 1 :** <@"+j1+">\n" +
                                "**Joueur 2 :** <@"+j2+">\n" +
                                "\n" +
                                "<:avatar:958465382610513931> Au tour de <@"+j1+">\n" +
                                "\n" +
                                "**Jeu en cours :** entre un nombre entre <:bingo:960318955623432222> **1** et <:bingo:960318955623432222> **100**")
                        .setImage("https://cdn.discordapp.com/attachments/958700629201666069/960520343926214696/JEU_DU_BINGO.png")
                .build()).queue(mess -> {
                    Membre.getMembre(j2).removePoints(mise);
                    setNumber();
                    msg = mess.getId();
        });
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.getChannelType().equals(ChannelType.TEXT)) return;
        if(!event.getChannel().getId().equals(Main.getDUELCHANNELID())) return;
        if( !(event.getAuthor().getId().equals(j1) || event.getAuthor().getId().equals(j2)) ) return;
        if(tour%2 == 0 && event.getAuthor().getId().equals(j2)) return;
        if(tour%2 == 1 && event.getAuthor().getId().equals(j1)) return;

        int nombre;
        String image;
        String actualPlayer;
        if(tour%2 != 0){
            actualPlayer = j1;
        }else{
            actualPlayer = j2;
        }
        try{
            nombre = Integer.parseInt(event.getMessage().getContentRaw());
            if(nombre != number) {
                if(nombre > number){
                    image = "https://cdn.discordapp.com/attachments/958700629201666069/960521658085543997/nombe__petit.png";
                }else{
                    image = "https://cdn.discordapp.com/attachments/958700629201666069/960521658614050886/nombre__grand.png";
                }
                event.getChannel().retrieveMessageById(msg).queue(message -> {
                    message.editMessageEmbeds(new EmbedBuilder()
                            .setColor(Main.getBlue())
                            .setDescription("<:bingo:960318955623432222> Jeu du Bingo \n" +
                                    "\n" +
                                    "**Joueur 1 :** <@" + j1 + ">\n" +
                                    "**Joueur 2 :** <@" + j2 + ">\n" +
                                    "\n" +
                                    "<:avatar:958465382610513931> Au tour de <@" + actualPlayer + ">\n" +
                                    "**Dernier nombre** : "+nombre)
                            .setImage(image)
                            .build()).queue();
                });
                event.getMessage().delete().queue();
                tour++;
            }else{
                event.getChannel().retrieveMessageById(msg).queue(message -> {
                    message.editMessageEmbeds(new EmbedBuilder()
                                    .setColor(Main.getBlue())
                                    .setDescription("<@"+actualPlayer+"> vient de gagné !")
                                    .setImage("https://cdn.discordapp.com/attachments/958700629201666069/960528390912376902/bingo.png")
                            .build()).queue();
                });
                Membre.getMembre(actualPlayer).addPoints(mise);
                reset();
            }
        }catch (NumberFormatException e){
            event.getMessage().delete().queue();
        }
    }

    public static void reset() {
        mise = 0;
        j1 = null;
        j2 = null;
        msg = null;
        tour = 0;
        number = 0;
    }

    public static void setNumber(){
        number = new Random().nextInt(999)+1;
    }
}
