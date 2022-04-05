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
import java.util.Timer;
import java.util.TimerTask;

public class Bingo extends ListenerAdapter {

    private static String j1 = null;
    private static String j2 = null;
    private static int mise = 0;
    private static String msg = null;
    private static int number = 0;
    private static int tour = 0;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!event.getName().equals("bingo")) return;

        String desc = Duel.getErreur(j1, j2, event.getOption("mise").getAsInt(), event.getUser().getId(), event.getOption("adversaire").getAsUser().getId());
        if(!desc.equals("")){
            event.replyEmbeds(new EmbedBuilder()
                            .setColor(Main.getBlue())
                            .setDescription(desc)
                    .build()).queue();
            return;
        }

        j1 = event.getUser().getId();
        j2 = event.getOption("adversaire").getAsUser().getId();
        mise = event.getOption("mise").getAsInt();

        //Envoie la proposition de duel à la personne mentionner
        event.getOption("adversaire").getAsUser().openPrivateChannel().queue(c -> {
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
        //Préviens l'auteur de la commande que la demande a été envoyée
        event.replyEmbeds(new EmbedBuilder()
                .setColor(Main.getBlue())
                .setDescription("<:oui:958523955285721188> Votre demande de duel à bien été envoyé à "+event.getOption("adversaire").getAsMember().getAsMention())
                .build()).queue();

        //Timer qui regarde si au bout de 1m la personne mentionné a donné une réponse.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //Si aucun message est setup, et que pourtant le joueur 1 n'est pas null. Alors la personne n'a pas donné de réponse
                if(msg == null && j1 != null && j2 != null){
                    //On préviens l'auteur de la commande que la personne n'a pas répondu
                    event.getUser().openPrivateChannel().queue(dm -> {
                        dm.sendMessageEmbeds(new EmbedBuilder()
                                .setColor(Main.getBlue())
                                .setDescription("<@"+j2+"> n'a pas répondu à votre demande de duel.\n" +
                                        "Nous vous avons rendu vos <:jeu:959785480227020800> **Points de jeu**")
                                .build()).queue();
                        //On renitialise la partie
                        reset();
                    });
                }
            }
        }, 60000);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        if(!(buttonId.equals("refuser") || buttonId.equals("accepter"))) return;
        if(!event.getUser().getId().equals(j2)) return;

        //Si le joueur 2 clique sur refuser
        if(buttonId.equals("refuser")){
            event.getJDA().getGuildById(Main.getGuildId()).getTextChannelById(Main.getDUELCHANNELID()).sendMessageEmbeds(new EmbedBuilder()
                    .setColor(Main.getBlue())
                    .setDescription("<:non:958523955126358086> "+event.getUser().getAsMention()+" a refusé le duel contre <@"+j1+">")
                    .build()).queue();
            event.replyEmbeds(new EmbedBuilder()
                    .setColor(Main.getBlue())
                    .setDescription("Vous avez refusé le défi.")
                    .build()).queue();
            Membre.getMembre(j1).addPoints(mise);
            reset();

            //Si le joueur 2 clique sur accepter
        }else {

            //On enleve les points aux deux joueurs.
            Membre.getMembre(j1).removePoints(mise);
            Membre.getMembre(j2).removePoints(mise);
            //On initialise au hasard le numéro du Bingo
            setNumber();

            //On lui indique en mp où se situe la partie
            event.replyEmbeds(new EmbedBuilder()
                    .setColor(Main.getBlue())
                    .setDescription("Vous avez accepter le défi.\n" +
                            "Votre partie se trouve ici : <#" + Main.getDUELCHANNELID() + ">")
                    .build()).queue();

            //On envoie le message de jeu, qu'on modifiera avec le temps
            event.getJDA().getGuildById(Main.getGuildId()).getTextChannelById(Main.getDUELCHANNELID()).sendMessageEmbeds(new EmbedBuilder()
                    .setColor(Main.getBlue())
                    .setDescription("<:bingo:960318955623432222> Jeu du Bingo \n" +
                            "\n" +
                            "**Joueur 1 :** <@" + j1 + ">\n" +
                            "**Joueur 2 :** <@" + j2 + ">\n" +
                            "\n" +
                            "<:avatar:958465382610513931> Au tour de <@" + j1 + ">\n" +
                            "\n" +
                            "**Jeu en cours :** entre un nombre entre <:bingo:960318955623432222> **1** et <:bingo:960318955623432222> **1000**")
                    .setImage("https://i.imgur.com/CD7SNaX.png")
                    .build()).queue(mess -> {
                //On initialise le message contenant le jeu
               setMsg(mess.getId());
            });
        }
        //Qu'il accepte ou refuse la demande, on enleve le message d'embed
        event.getMessage().delete().queue();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getChannelType().equals(ChannelType.TEXT)) return;
        if (!event.getChannel().getId().equals(Main.getDUELCHANNELID())) return;
        if (!(event.getAuthor().getId().equals(j1) || event.getAuthor().getId().equals(j2))) return;

        int nombre;
        String image;
        String actualPlayer;
        if (tour % 2 != 0) {
            actualPlayer = j1;
        } else {
            actualPlayer = j2;
        }
        try {
            nombre = Integer.parseInt(event.getMessage().getContentRaw());
            if (nombre != number) {
                if (nombre > number) {
                    image = "https://i.imgur.com/29ngKvf.png";
                } else {
                    image = "https://i.imgur.com/KJV1eDQ.png";
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
                                    "**Dernier nombre** : " + nombre)
                            .setImage(image)
                            .build()).queue();
                });
                Bingo.addTour();
            } else {
                event.getChannel().retrieveMessageById(msg).queue(message -> {
                    message.editMessageEmbeds(new EmbedBuilder()
                            .setColor(Main.getBlue())
                            .setDescription("<@" + event.getAuthor().getId() + "> vient de gagné !")
                            .setImage("https://i.imgur.com/a5sEjbb.png")
                            .build()).queue();
                });
                Membre.getMembre(event.getAuthor().getId()).addPoints(mise * 2);
                reset();
            }
        } catch (NumberFormatException e) {
            return;
        }
        event.getMessage().delete().queue();
    }

    public static void reset(){
        j1 = null;
        j2 = null;
        mise = 0;
        msg = null;
        number = 0;
        tour = 0;
    }

    public static void setMsg(String msg) {
        Bingo.msg = msg;
    }

    public static void setNumber() {
        Bingo.number = new Random().nextInt(1000);
    }

    public static void addTour(){
        tour++;
    }

}
