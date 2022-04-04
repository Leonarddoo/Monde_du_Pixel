package fr.leonarddoo.pixel.ticket;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ManageTicket extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String idButton = event.getButton().getId();
        if(!(idButton.equals("fermer") || idButton.equals("prendre"))) return;
        if(!event.getMember().getRoles().contains(event.getGuild().getRoleById("955994372137189436"))) return;
        if(event.getButton().getId().equals("fermer")){
            event.getChannel().delete().queue();
        }else if(event.getButton().getId().equals("prendre")){
            event.replyEmbeds(new EmbedBuilder()
                            .setDescription(event.getMember().getAsMention()+" a pris le ticket.")
                            .setColor(new Color(90, 207, 245))
                    .build()).queue();
        }
    }
}
