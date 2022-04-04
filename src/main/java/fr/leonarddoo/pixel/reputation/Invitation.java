package fr.leonarddoo.pixel.reputation;

import fr.leonarddoo.pixel.Main;
import fr.leonarddoo.pixel.Membre;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Invitation extends ListenerAdapter {

    private List<Invite> inviteList = new ArrayList<>();
    private Membre m;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().getGuildById(Main.getGuildId()).retrieveInvites().queue(invites -> {
            inviteList = invites;
        });
    }

    @Override
    public void onGuildInviteCreate(@NotNull GuildInviteCreateEvent event) {
        event.getGuild().retrieveInvites().queue(invites -> {
            inviteList = invites;
        });
    }

    @Override
    public void onGuildInviteDelete(@NotNull GuildInviteDeleteEvent event) {
        event.getGuild().retrieveInvites().queue(invites -> {
            inviteList = invites;
        });
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        event.getGuild().retrieveInvites().queue(invites -> {
            String id = null;
            for(int i = 0 ; i < inviteList.size() ; i++){
                if(inviteList.get(i).getUses() < invites.get(i).getUses()){
                    id = invites.get(i).getInviter().getId();
                    break;
                }
            }

            m = Membre.getMembre(id);
            m.addInvitation();
            m.addReputation(2);

            event.getGuild().getTextChannelById("957729000531308645").sendMessageEmbeds(new EmbedBuilder()
                            .setDescription("<:avatar:958465382610513931> <@"+id+"> à gagné **2** de réputation pour avoir invité " + event.getMember().getAsMention()+"\n" +
                                    "\n"+
                                    "\uD83D\uDCE9 Invitation au total : **"+m.getInvitations()+"**\n" +
                                    "\n"+
                                    "<:reputation:958738704757850132> Réputation : **"+m.getReputation()+"**")
                            .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                            .setColor(new Color(90, 207, 245))
                    .build()).queue();
        });
    }
}
