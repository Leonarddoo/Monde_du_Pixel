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

import java.io.IOException;
import java.util.*;

public class Bingo extends ListenerAdapter {

    private static List<Bingo> bingoList= new ArrayList<>();
    private String j1, j2, msg, channel;
    private int mise, number, tour;

    public Bingo(){}

    public Bingo(String j1, String j2, int mise){
        this.j1 = j1;
        this.j2 = j2;
        this.mise = mise;
        this.msg = null;
        this.number = 0;
        this.tour = 0;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!event.getName().equals("bingo")) return;

        Bingo b = retrieveBingoByPlayer(event.getUser().getId());
        if(b != null) return;

        String desc = Duel.getErreur(event.getUser().getId(), event.getOption("adversaire").getAsUser().getId(), event.getOption("mise").getAsInt());
        if(!desc.equals("")){
            event.replyEmbeds(new EmbedBuilder()
                            .setColor(Main.getBlue())
                            .setDescription(desc)
                    .build()).queue();
            return;
        }

        b = new Bingo(event.getUser().getId(), event.getOption("adversaire").getAsUser().getId(), event.getOption("mise").getAsInt());
        bingoList.add(b);


        //Envoie la proposition de duel à la personne mentionner
        Bingo finalB = b;
        event.getOption("adversaire").getAsUser().openPrivateChannel().queue(c -> {
            c.sendMessageEmbeds(new EmbedBuilder()
                    .setTitle("Bingo")
                    .setDescription(event.getUser().getAsMention()+" veut vous défier dans un duel dans l'**Arcade du Pixel** \n" +
                            "\n" +
                            "Vous miserez <:jeu:959785480227020800> "+ finalB.getMise()+" **Point(s) de Jeu** chacun. Si l'un d'entre vous gagne, il prend le tout ! \n" +
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
        Bingo finalB1 = b;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //Si aucun message est setup, et que pourtant le joueur 1 n'est pas null. Alors la personne n'a pas donné de réponse
                if(finalB1.getMsg() == null && finalB1.getJ1() != null && finalB1.getJ2() != null){
                    //On préviens l'auteur de la commande que la personne n'a pas répondu
                    event.getUser().openPrivateChannel().queue(dm -> {
                        dm.sendMessageEmbeds(new EmbedBuilder()
                                .setColor(Main.getBlue())
                                .setDescription("<@"+ finalB1.getJ2()+"> n'a pas répondu à votre demande de duel.\n" +
                                        "Nous vous avons rendu vos <:jeu:959785480227020800> **Points de jeu**")
                                .build()).queue();
                        //On renitialise la partie
                        bingoList.remove(finalB1);
                    });
                }
            }
        }, 60000);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        if(!(buttonId.equals("refuser") || buttonId.equals("accepter"))) return;

        Bingo b = retrieveBingoByPlayer(event.getUser().getId());

        if(b == null) return;
        if(!b.getJ2().equals(event.getUser().getId())) return;


        //Si le joueur 2 clique sur refuser
        if(buttonId.equals("refuser")){
            //On prévient le joueur 1 que le duel a était refusé
            event.getJDA().retrieveUserById(b.getJ1()).queue(u ->{
                u.openPrivateChannel().queue(dm -> {
                    dm.sendMessageEmbeds(new EmbedBuilder()
                            .setColor(Main.getBlue())
                            .setDescription("<:non:958523955126358086> "+event.getUser().getAsMention()+" a refusé le duel contre <@"+b.getJ1()+">")
                            .build()).queue();
                });
            });
            //On envoie un message de réponse
            event.replyEmbeds(new EmbedBuilder()
                    .setColor(Main.getBlue())
                    .setDescription("Vous avez refusé le défi.")
                    .build()).queue();


            //Si le joueur 2 clique sur accepter
        }else {

            //On enleve les points aux deux joueurs.
            Membre.getMembre(b.getJ1()).removePoints(b.getMise());
            Membre.getMembre(b.getJ1()).removePoints(b.getMise());
            //On initialise au hasard le numéro du Bingo
            setNumber();

            //On créer le channel où se trouvera le duel
            event.getJDA().getGuildById(Main.getGuildId()).createTextChannel("duel", event.getJDA().getGuildById(Main.getGuildId()).getCategoryById(Main.getCATEGORYGAME())).queue(channel -> {
                b.setChannel(channel.getId());
                //On envoie le message du jeu qu'on modifiera par la suite
                channel.sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Main.getBlue())
                        .setDescription("<:bingo:960318955623432222> Jeu du Bingo \n" +
                                "\n" +
                                "**Joueur 1 :** <@" + b.getJ1() + ">\n" +
                                "**Joueur 2 :** <@" + b.getJ2() + ">\n" +
                                "\n" +
                                "<:avatar:958465382610513931> Au tour de <@" + b.getJ1() + ">\n" +
                                "\n" +
                                "**Jeu en cours :** entre un nombre entre <:bingo:960318955623432222> **1** et <:bingo:960318955623432222> **1000**")
                        .setImage("https://i.imgur.com/CD7SNaX.png")
                        .build()).queue(mess -> {
                            //On initialise le message où se trouve le duel
                            b.setMsg(mess.getId());
                });
                //On lui indique en mp où se situe la partie
                event.replyEmbeds(new EmbedBuilder()
                        .setColor(Main.getBlue())
                        .setDescription("Vous avez accepter le défi.\n" +
                                "Votre partie se trouve ici : <#" + channel.getId() + ">")
                        .build()).queue();
            });
        }
        //Qu'il accepte ou refuse la demande, on enleve le message d'embed
        event.getMessage().delete().queue();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getChannelType().equals(ChannelType.TEXT)) return;

        Bingo b = retrieveBingoByPlayer(event.getAuthor().getId());
        if(b == null) return;
        if(!event.getChannel().getId().equals(b.getChannel())) return;

        int nombre;
        String image;
        String actualPlayer;

        try {
            nombre = Integer.parseInt(event.getMessage().getContentRaw());
        }catch (NumberFormatException e){
            return;
        }

        if (b.getTour() % 2 != 0) {
            actualPlayer = b.getJ1();
        } else {
            actualPlayer = b.getJ2();
        }
        if (nombre != b.getNumber()) {
            if (nombre > b.getNumber()) {
                image = "https://i.imgur.com/29ngKvf.png";
            } else {
                image = "https://i.imgur.com/KJV1eDQ.png";
            }
            Bingo finalB = b;
            event.getChannel().retrieveMessageById(b.getMsg()).queue(message -> {
                message.editMessageEmbeds(new EmbedBuilder()
                        .setColor(Main.getBlue())
                        .setDescription("<:bingo:960318955623432222> Jeu du Bingo \n" +
                                "\n" +
                                "**Joueur 1 :** <@" + finalB.getJ1() + ">\n" +
                                "**Joueur 2 :** <@" + finalB.getJ2() + ">\n" +
                                "\n" +
                                "<:avatar:958465382610513931> Au tour de <@" + actualPlayer + ">\n" +
                                "**Dernier nombre** : " + nombre)
                        .setImage(image)
                        .build()).queue();
            });
            b.addTour();
        } else {
            event.getChannel().retrieveMessageById(b.getMsg()).queue(message -> {
                message.editMessageEmbeds(new EmbedBuilder()
                        .setColor(Main.getBlue())
                        .setDescription("<@" + event.getAuthor().getId() + "> vient de gagné !")
                        .setImage("https://i.imgur.com/a5sEjbb.png")
                        .build()).queue();
            });
            Membre.getMembre(event.getAuthor().getId()).addPoints(b.getMise() * 2);
            bingoList.remove(b);
            event.getChannel().delete().queue();
        }
    }

    public String getJ1() {
        return j1;
    }

    public String getJ2() {
        return j2;
    }

    public String getMsg() {
        return msg;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getMise() {
        return mise;
    }

    public int getNumber() {
        return number;
    }

    public int getTour() {
        return tour;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setNumber() {
        this.number = new Random().nextInt(1000);
    }

    public void addTour(){
        this.tour++;
    }

    public static Bingo retrieveBingoByPlayer(String id){
        for(Bingo b : bingoList){
            if(b.getJ1().equals(id) || b.getJ2().equals(id)){
                return b;
            }
        }
        return null;
    }

}
