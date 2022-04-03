package fr.leonarddoo.pixel.ticket;

import fr.leonarddoo.pixel.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CreatePanel extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!event.getName().equals("setupticket")) return;
        if(!event.getMember().getRoles().contains(event.getGuild().getRoleById(Main.getPixelteam()))) return;
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
    }
}
