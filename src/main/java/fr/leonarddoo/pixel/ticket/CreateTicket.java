package fr.leonarddoo.pixel.ticket;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CreateTicket extends ListenerAdapter {

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        event.getMessage().editMessageEmbeds(event.getMessage().getEmbeds()).setActionRow(event.getSelectMenu()).queue();

        boolean find = false;
        for(TextChannel c : event.getGuild().getTextChannels()){
            if(event.getUser().getId().equals(c.getTopic())){
                find = true;
                break;
            }
        }

        if(!find) {
            event.getGuild().getCategoryById("959723293571690527").createTextChannel("ticket-"+event.getMember().getEffectiveName()).queue(c -> {
                c.getManager().setTopic(event.getUser().getId()).queue();
                c.sendMessageEmbeds(new EmbedBuilder()
                        .setTitle("Nouveau Ticket")
                        .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                        .setColor(new Color(90, 207, 245))
                        .setDescription(event.getUser().getAsMention() + " a ouvert un ticket.")
                        .addField("Raison", event.getSelectedOptions().get(0).getEmoji().getAsMention()+" "+event.getSelectedOptions().get(0).getLabel(), false)
                        .build()).setActionRow(Button.success("prendre", "Prendre le ticket"), Button.danger("fermer", "Fermer le ticket")).queue();
                event.getUser().openPrivateChannel().queue(dm -> {
                    dm.sendMessageEmbeds(new EmbedBuilder()
                                    .setDescription("Votre ticket se trouve ici : "+c.getAsMention())
                                    .setColor(new Color(90, 207, 245))
                            .build()).queue();
                },new ErrorHandler()
                    .handle(ErrorResponse.CANNOT_SEND_TO_USER,
                            e -> System.out.println("Impossible d'envoyer le message.")));
            });
        }
    }
}
