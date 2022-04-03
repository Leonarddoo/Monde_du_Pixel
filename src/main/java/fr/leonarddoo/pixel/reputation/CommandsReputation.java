package fr.leonarddoo.pixel.reputation;

import fr.leonarddoo.pixel.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CommandsReputation extends ListenerAdapter {

    private String id;
    private int montant;
    private String raison;
    private Membre m;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if(!event.getName().contains("rep")) return;

        m = Membre.getMembre(event.getOption("membre").getAsUser().getId());

        switch (event.getName()) {

            case "rep":
                event.replyEmbeds(new EmbedBuilder()
                        .setDescription(event.getOption("membre").getAsMember().getAsMention() + " possède <:reputation:958738704757850132> **" + m.getReputation() + "** de réputation")
                        .setColor(new Color(90, 207, 245))
                        .build()).queue();
                break;

            case "addrep":
                if (!event.getMember().getRoles().contains(event.getGuild().getRoleById(Main.getPixelteam()))) return;

                id = event.getOption("membre").getAsMember().getId();
                montant = event.getOption("montant").getAsInt();
                raison = event.getOption("raison").getAsString();

                m.addReputation(montant);
                event.replyEmbeds(new EmbedBuilder()
                        .setDescription("Vous venez de rajouter " + montant + " de réputation à <@" + id + "> pour raison : " + raison)
                        .build()).queue();
                break;

            case "removerep":
                if (!event.getMember().getRoles().contains(event.getGuild().getRoleById(Main.getPixelteam()))) return;
                id = event.getOption("membre").getAsMember().getId();
                montant = event.getOption("montant").getAsInt();
                raison = event.getOption("raison").getAsString();

                m.removeReputation(montant);

                event.replyEmbeds(new EmbedBuilder()
                        .setDescription("Vous venez de retirer " + montant + " de réputation à <@" + id + "> pour raison : " + raison)
                        .build()).queue();
                break;
        }
    }
}
