package fr.leonarddoo.pixel;

import fr.leonarddoo.pixel.reputation.Membre;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class CommandsUse extends ListenerAdapter {

    private Membre m;
    private String id;
    private int montant;
    private String raison;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        try {
            id = event.getOption("membre").getAsUser().getId();

            if (!Membre.contains(id)) {
                Membre.getMembreList().add(new Membre(id, 0, 0, 0));
            }

            m = Membre.retrieve(id);
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        switch (event.getName()){
            case "profil":
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("\uD83D\uDC64 "+event.getOption("membre").getAsMember().getEffectiveName()+"#"+event.getOption("membre").getAsUser().getDiscriminator())
                        .setThumbnail(event.getOption("membre").getAsMember().getEffectiveAvatarUrl())
                        .setDescription("__Votre **réputation** détermine votre valeur sur le serveur.__\n" +
                                "**Inviter** des joueurs, les **aider** puis **discuter**. Ainsi, vous obtiendrez de la réputation.\n")
                        .setColor(new Color(90, 207, 245))
                        .addField("<:invite:959428010837155840> Invitations", Integer.toString(m.getInvitations()), true)
                        .addField("<:reputation:958738704757850132> Reputation", Integer.toString(m.getReputation()), true)
                        .addField("\uD83D\uDCE9 Messages", Integer.toString(m.getMessages()), true)
                        .build()).queue();
                break;

            case "rep":
                event.replyEmbeds(new EmbedBuilder()
                            .setDescription(event.getOption("membre").getAsMember().getAsMention()+" possède <:reputation:958738704757850132> **"+Membre.retrieve(event.getOption("membre").getAsUser().getId()).getReputation()+"** de réputation")
                            .setColor(new Color(90, 207, 245))
                        .build()).queue();
                break;

            case "addrep":
                if(!event.getMember().getRoles().contains(event.getGuild().getRoleById("955994372137189436"))) return;

                id = event.getOption("membre").getAsMember().getId();
                montant = event.getOption("montant").getAsInt();
                raison = event.getOption("raison").getAsString();

                m.addReputation(montant);
                event.replyEmbeds(new EmbedBuilder()
                        .setDescription("Vous venez de rajouter "+montant+" de réputation à <@"+id+"> pour raison : " + raison)
                        .build()).queue();
                break;

            case "removerep":
                if(!event.getMember().getRoles().contains(event.getGuild().getRoleById("955994372137189436"))) return;
                id = event.getOption("membre").getAsMember().getId();
                montant = event.getOption("montant").getAsInt();
                raison = event.getOption("raison").getAsString();

                m.removeReputation(montant);

                event.replyEmbeds(new EmbedBuilder()
                                .setDescription("Vous venez de retirer "+montant+" de réputation à <@"+id+"> pour raison : " + raison)
                        .build()).queue();
                break;

            case "setupticket":
                event.getTextChannel().sendMessageEmbeds(new EmbedBuilder()
                                .setDescription("<:botpixela:958470990281723925> Vous souhaitez **ouvrir un ticket** ? \n" +
                                        "\n" +
                                        "**• Sélectionne** dans le menu ci-dessous la raison qui te pousses à nous contacter.")
                                .setColor(new Color(90, 207, 245))
                        .build()).setActionRow(SelectMenu.create("tickets")
                                .setRequiredRange(1, 1)
                                .setPlaceholder("Sélectionner une raison")
                                .addOption("Informations personnelles", "perso", "Nous t'avons divulgué/publié des infos personnelles ?", Emoji.fromUnicode("\uD83D\uDD10"))
                                .addOption("Publicités interdites", "pub", "Tu as reçu une publicité en privé ?", Emoji.fromUnicode("\uD83D\uDCE2"))
                                .addOption("Signaler un membre", "signalement", "Tu as reçu des insultes, menaces ou autres ?", Emoji.fromUnicode("\uD83D\uDDE3️"))
                                .addOption("Pings abusifs", "ping", "On te mentionne à répétitions ?", Emoji.fromUnicode("\uD83D\uDCCC"))
                                .addOption("Signaler un bug(s)", "bug", "Tu as vu quelque chose qui ne va pas ?", Emoji.fromUnicode("⚠️"))
                                .addOption("GiveAway", "giveaway", "Tu as gagné le giveaway mais tu n'as rien reçu ?", Emoji.fromUnicode("\uD83C\uDF81"))
                                .addOption("Demande de partenariat", "partenariat", "Tu compte être un de nos partenaires ?", Emoji.fromUnicode("\uD83C\uDF40"))

                        .build()).queue();
                break;
        }
    }

}
