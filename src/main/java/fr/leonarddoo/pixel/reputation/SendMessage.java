package fr.leonarddoo.pixel.reputation;

import fr.leonarddoo.pixel.Membre;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendMessage extends ListenerAdapter {

    private Membre m;

    private List<String> listChannels = new ArrayList<>(
            Arrays.asList("955919690838990943",
                    "956668272806350928",
                    "956668294490910831",
                    "956668495192535060",
                    "956724350793441310")
    );

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.getChannelType().equals(ChannelType.TEXT)) return;
        if(event.getAuthor().isBot()) return;
        if(!listChannels.contains(event.getChannel().getId())) return;

        m = Membre.getMembre(event.getAuthor().getId());
        m.addMessage();

        if(m.getMessages()%50 == 0){
            event.getMessage().replyEmbeds(new EmbedBuilder()
                    .setDescription(event.getMember().getAsMention()+" vient d'augmenter sa réputation de un grâce à ces "+m.getMessages()+" messages.")
                    .build()).queue();
        }
    }
}
