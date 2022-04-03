package fr.leonarddoo.pixel;

import fr.leonarddoo.pixel.reputation.Membre;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Profil extends ListenerAdapter {

    private Membre m;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!event.getName().equals("profil")) return;

        m = Membre.getMembre(event.getOption("membre").getAsUser().getId());

        event.replyEmbeds(new EmbedBuilder()
                .setTitle("\uD83D\uDC64 "+event.getOption("membre").getAsMember().getEffectiveName()+"#"+event.getOption("membre").getAsUser().getDiscriminator())
                .setThumbnail(event.getOption("membre").getAsMember().getEffectiveAvatarUrl())
                .setDescription("__Votre **réputation** détermine votre valeur sur le serveur.__\n" +
                        "**Inviter** des joueurs, les **aider** puis **discuter**. Ainsi, vous obtiendrez de la réputation.\n")
                .setColor(new Color(90, 207, 245))
                .addField("<:invite:959428010837155840> Invitations :", Integer.toString(m.getInvitations()), true)
                .addField("<:reputation:958738704757850132> Reputation :", Integer.toString(m.getReputation()), true)
                .addField("\uD83D\uDCE9 Messages :", Integer.toString(m.getMessages()), true)
                .addField("<:jeu:959785480227020800> Points de Jeu :", Integer.toString(m.getPoints()), true)
                .build()).queue();
    }
}
