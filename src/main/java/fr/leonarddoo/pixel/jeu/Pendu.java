package fr.leonarddoo.pixel.jeu;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Pendu extends ListenerAdapter {

    private static String joueur = null;
    private static int essais = 0;

    private static final String[] affichage = {
            "https://cdn.discordapp.com/attachments/958700629201666069/959902463421468725/IMG_7199.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902479837986836/IMG_7200.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902489459687544/IMG_7201.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902498938822707/IMG_7202.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902505255464991/IMG_7203.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902516504584272/IMG_7204.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902528059895908/IMG_7205.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902538570809414/IMG_7206.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902547588550746/IMG_7208.jpg"
    };

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(joueur != null) return;
        if(!event.getName().equals("pendu")) return;

        joueur = event.getUser().getId();

        event.replyEmbeds(new EmbedBuilder()
                        .setTitle("Pendu de "+event.getMember().getEffectiveName())
                        .setImage(affichage[0])
                .build()).addActionRow(Button.secondary("Test", "test")).queue();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if(!event.getUser().getId().equals(joueur))
        if(!event.getButton().getId().equals("test")) return;
        essais++;
        event.getMessage().editMessageEmbeds(new EmbedBuilder()
                        .setTitle(event.getMessage().getEmbeds().get(0).getTitle())
                        .setImage(affichage[essais])
                .build()).queue();
    }
}
