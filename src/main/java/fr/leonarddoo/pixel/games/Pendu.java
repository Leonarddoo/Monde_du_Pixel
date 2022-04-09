package fr.leonarddoo.pixel.games;

import fr.leonarddoo.pixel.Main;
import fr.leonarddoo.pixel.Membre;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Pendu extends ListenerAdapter {

    private Player p1;
    private Player p2;
    private int mise;
    private String channel;

    private static List<Pendu> penduList = new ArrayList<>();

    private static final String[] affichage = {
            "https://i.imgur.com/q762jPd.jpg",
            "https://i.imgur.com/uijC6Nd.jpg",
            "https://i.imgur.com/sRN1uVy.jpg",
            "https://i.imgur.com/yoCbfow.jpg",
            "https://i.imgur.com/WBZbTTC.jpg",
            "https://i.imgur.com/Ifiq9ei.jpg",
            "https://i.imgur.com/EWOGyxM.jpg",
            "https://i.imgur.com/5hPC4Q7.jpg",
            "https://i.imgur.com/2VjsgdZ.jpg"
    };

    public Pendu(){}

    public Pendu(Player p1, Player p2, int mise){
        this.p1 = p1;
        this.p2 = p2;
        this.mise = mise;
        this.channel = null;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!event.getName().equals("pendu")) return;

        Pendu p = getPenduByPlayer(event.getUser().getId());
        if(p != null) return;

        String desc = Duel.getErreur(event.getUser().getId(), event.getOption("adversaire").getAsUser().getId(), event.getOption("mise").getAsInt());
        if(!desc.equals("")){
            event.replyEmbeds(new EmbedBuilder()
                    .setColor(Main.getBlue())
                    .setDescription(desc)
                    .build()).queue();
            return;
        }

        p = new Pendu(new Player(event.getUser().getId()), new Player(event.getOption("adversaire").getAsUser().getId()), event.getOption("mise").getAsInt());
        penduList.add(p);

        //Envoie la proposition de duel à la personne mentionner
        Pendu finalP = p;
        event.getOption("adversaire").getAsUser().openPrivateChannel().queue(c -> {
            c.sendMessageEmbeds(new EmbedBuilder()
                    .setTitle("Pendu")
                    .setDescription(event.getUser().getAsMention()+" veut vous défier dans un duel dans l'**Arcade du Pixel** \n" +
                            "\n" +
                            "Vous miserez <:jeu:959785480227020800> "+finalP.mise+" **Point(s) de Jeu** chacun. Si l'un d'entre vous gagne, il prend le tout ! \n" +
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
                if(finalP.channel == null){
                    //On préviens l'auteur de la commande que la personne n'a pas répondu
                    event.getUser().openPrivateChannel().queue(dm -> {
                        dm.sendMessageEmbeds(new EmbedBuilder()
                                .setColor(Main.getBlue())
                                .setDescription("<@"+finalP.p2.getId()+"> n'a pas répondu à votre demande.\n" +
                                        "Votre duel a donc été annulé.")
                                .build()).queue();
                        //On renitialise la partie
                        penduList.remove(finalP);
                    });
                }
            }
        }, 60000);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        if(!(buttonId.equals("refuser") || buttonId.equals("accepter"))) return;

        Pendu p = getPenduByPlayer(event.getUser().getId());

        if(p == null) return;
        if(!p.p2.getId().equals(event.getUser().getId())) return;


        //Si le joueur 2 clique sur refuser
        if(buttonId.equals("refuser")){
            //On prévient le joueur 1 que le duel a était refusé
            event.getJDA().retrieveUserById(p.p1.getId()).queue(u ->{
                u.openPrivateChannel().queue(dm -> {
                    dm.sendMessageEmbeds(new EmbedBuilder()
                            .setColor(Main.getBlue())
                            .setDescription("<:non:958523955126358086> "+event.getUser().getAsMention()+" a refusé le duel contre <@"+p.p1.getId()+">")
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
            Membre.getMembre(p.p1.getId()).removePoints(p.mise);
            Membre.getMembre(p.p2.getId()).removePoints(p.mise);

            //On initialise les mots de chaque joueur
            p.setupWords();

            //On créer le channel où se trouvera le duel
            event.getJDA().getGuildById(Main.getGuildId()).createTextChannel("duel", event.getJDA().getGuildById(Main.getGuildId()).getCategoryById(Main.getCATEGORYGAME()))
                    .syncPermissionOverrides().queue(channel -> {
                        channel.getManager().putMemberPermissionOverride(Long.parseLong(p.p1.getHidden()), Permission.VIEW_CHANNEL.getRawValue(), 0)
                                .putMemberPermissionOverride(Long.parseLong(p.p2.getId()), Permission.VIEW_CHANNEL.getRawValue(), 0).queue();

                        //On initialise le channel du duel
                        p.channel = channel.getId();

                        //On envoie le messages des règles du jeu.
                        channel.sendMessageEmbeds(new EmbedBuilder()
                                        .setColor(Main.getBlue())
                                        .setDescription("<:pendu:960318773167030272> **Jeu du Pendu**\n" +
                                                "\n" +
                                                "**Joueur 1 :** <@"+p.p1.getId()+">\n" +
                                                "**Joueur 2 :** <@"+p.p2.getId()+">\n" +
                                                "\n" +
                                                "<:pendu:960318773167030272> **Règles :**\n" +
                                                "**•** Chaque joueurs possède son plateau.\n" +
                                                "**•** Vous avez **8** tentatives chacun(e).\n" +
                                                "**•** Trouvez votre mot et c'est **gagné !**\n" +
                                                "**•** Si les deux joueurs trouvent leur mots alors c'est une **égalité !** Chauqe joueur récupère les <:jeu:959785480227020800> **Point(s) de jeu** qui ont été misés.")
                                .build()).queue();

                        //On envoie les messages du jeu pour chaque joueur, qu'on modifiera
                        channel.sendMessageEmbeds(new EmbedBuilder()
                                        .setColor(Main.getBlue())
                                        .setDescription("Mot : "+p.p1.getWord()+"\n" +
                                                "Utilisés : \n" +
                                                "Cache : "+p.p1.getHidden())
                                        .setImage(affichage[p.p1.getRound()])
                                .build()).queue(message -> {
                                    p.p1.setMsg(message.getId());
                        });
                        channel.sendMessageEmbeds(new EmbedBuilder()
                                .setColor(Main.getBlue())
                                .setDescription("Mot : "+p.p2.getWord()+"\n" +
                                        "Utilisés : \n" +
                                        "Cache : "+p.p2.getHidden())
                                .setImage(affichage[p.p2.getRound()])
                                .build()).queue(message -> {
                                    p.p2.setMsg(message.getId());
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
        //On regarde si on se trouve bien dans un TextChannel
        if(!event.getChannelType().equals(ChannelType.TEXT)) return;

        //On retrouve une partie par rapport à l'ID du channel
        Pendu p = retrievePenduByChannel(event.getChannel().getId());
        //Si aucune partie ne correspond à ce channel on ne continue pas
        if(p == null) return;

        //Quoi qu'il arrive on supprime le message
        event.getMessage().delete().queue();
        //On regarde si le message ne contient qu'un caractère
        if(event.getMessage().getContentRaw().length() > 1) return;
        //Si c'est le cas, on transforme le message String --> Char
        Character c = event.getMessage().getContentRaw().charAt(0);

        //On retrouve le joueur par rapport à son id
        Player player = p.getPlayer(event.getAuthor().getId());
        //Si on ne le retrouve pas, on ne continue pas
        if(player == null) return;
        //Si le joueur a déjà essayé cette lettre, on ne continue pas
        if(player.getUse().contains(c)) return;
        //Sinon on rajoute cette lettre à la liste des lettres utilisés
        player.getUse().add(Character.toUpperCase(c));
        //On regarde si cette lettre fait partie du mot, si c'est le cas on change le hidden, si ce n'est pas le cas on rajoute un tour au joueur.
        player.checkChar(c);

        //On modifier le message du joueur.
        event.getTextChannel().retrieveMessageById(player.getMsg()).queue(message -> {
            message.editMessageEmbeds(new EmbedBuilder()
                            .setColor(Main.getBlue())
                            .setDescription("Mot : "+player.getWord()+"\n" +
                            "Utilisés : \n" +
                            "Cache : "+player.getHidden())
                            .setImage(affichage[player.getRound()])
                    .build()).queue();
        });

    }

    public static Pendu retrievePenduByChannel(String channel){
        for(Pendu p : penduList){
            if(channel.equals(p.channel)){
                return p;
            }
        }
        return null;
    }

    public void setupWords(){
        try(BufferedReader reader = new BufferedReader(new FileReader("mot.txt"))){
            String ligne = reader.readLine();
            Random rdm = new Random();
            for(int i = 0 ; i < rdm.nextInt(200) ; i++){
                ligne = reader.readLine();
            }
            this.p1.setWord(ligne);
            this.p2.setWord(ligne);
            while(this.p1.getWord().length() != this.p2.getWord().length() || this.p1.getWord().equals(this.p2.getWord())){
                for(int i = 0 ; i < rdm.nextInt(200) ; i++){
                    ligne = reader.readLine();
                }
                this.p2.setWord(ligne);
            }

            this.p1.setHidden();
            this.p2.setHidden();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Pendu getPenduByPlayer(String player){
        for(Pendu p : penduList){
            if(p.p1.equals(player) || p.p2.equals(player)){
                return p;
            }
        }
        return null;
    }

    public Player getPlayer(String id){
        if(id.equals(this.p1.getId())){
            return this.p1;
        }else if(id.equals(this.p2.getId())){
            return this.p2;
        }
        return null;
    }
}
