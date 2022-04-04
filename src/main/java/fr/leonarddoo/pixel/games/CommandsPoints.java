package fr.leonarddoo.pixel.games;

import fr.leonarddoo.pixel.Main;
import fr.leonarddoo.pixel.Membre;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandsPoints extends ListenerAdapter {

    private Membre m;
    private int montant;
    private String raison;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if(!event.getName().contains("pdj")) return;
        if(!event.getMember().getRoles().contains(event.getGuild().getRoleById(Main.getPixelteam()))) return;

        m = Membre.getMembre(event.getOption("membre").getAsMember().getId());
        montant = event.getOption("montant").getAsInt();
        raison = event.getOption("raison").getAsString();

        switch (event.getName()){
            case "addpdj":
                m.addPoints(montant);
                event.replyEmbeds(new EmbedBuilder()
                                .setColor(Main.getBlue())
                                .setDescription(event.getMember().getAsMention()+" a ajouté <:jeu:959785480227020800> **"+montant+" Points de Jeu** dans la poche de "+event.getOption("membre").getAsMember().getAsMention()+"\n" +
                                        "\n" +
                                        "**Raison(s) :** "+raison)
                        .build()).queue();
                break;


            case "removepdj":
                m.removePoints(montant);
                event.replyEmbeds(new EmbedBuilder()
                        .setColor(Main.getBlue())
                        .setDescription(event.getMember().getAsMention()+" a retiré <:jeu:959785480227020800> **"+montant+" Points de Jeu** dans la poche de "+event.getOption("membre").getAsMember().getAsMention()+"\n" +
                                "\n" +
                                "**Raison(s) :** "+raison)
                        .build()).queue();
                break;
        }


    }
}
