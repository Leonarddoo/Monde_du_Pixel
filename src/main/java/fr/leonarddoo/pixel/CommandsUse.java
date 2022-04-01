package fr.leonarddoo.pixel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CommandsUse extends ListenerAdapter {


    private String id;
    private int montant;
    private String raison;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        switch (event.getName()){

            case "rep":
                id = event.getOption("membre").getAsUser().getId();

                if(!Membre.contains(id)){
                    Membre.getMembreList().add(new Membre(id, 0, 0, 0));
                }

                Membre m = Membre.retrieve(id);

                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("\uD83D\uDC64 "+event.getOption("membre").getAsMember().getEffectiveName()+"#"+event.getOption("membre").getAsUser().getDiscriminator())
                        .setThumbnail(event.getOption("membre").getAsMember().getEffectiveAvatarUrl())
                        .setDescription("__Votre **réputation** détermine votre valeur sur le serveur.__\n" +
                                "**Inviter** des joueurs, les **aider** puis **discuter**. Ainsi, vous obtiendrez de la réputation.\nㅤ")
                        .setColor(new Color(90, 207, 245))
                        .addField("<:invite:959428010837155840> Invitations", Integer.toString(m.getInvitations()), true)
                        .addField("<:reputation:958738704757850132> Reputation", Integer.toString(m.getReputation()), true)
                        .addField("\uD83D\uDCE9 Messages", Integer.toString(m.getMessages()), true)
                        .build()).queue();
                break;


            case "addrep":
                if(!event.getMember().getRoles().contains(event.getGuild().getRoleById("955994372137189436"))) return;

                id = event.getOption("membre").getAsMember().getId();
                montant = event.getOption("montant").getAsInt();
                raison = event.getOption("raison").getAsString();

                if(Membre.contains(id)){
                    Membre.retrieve(id).addReputation(montant);
                }else{
                    Membre.getMembreList().add(new Membre(id, montant, 0, 0));
                }
                event.replyEmbeds(new EmbedBuilder()
                        .setDescription("Vous venez de rajouter "+montant+" de réputation à <@"+id+"> pour raison : " + raison)
                        .build()).queue();
                break;

            case "removerep":
                if(!event.getMember().getRoles().contains(event.getGuild().getRoleById("955994372137189436"))) return;
                id = event.getOption("membre").getAsMember().getId();
                montant = event.getOption("montant").getAsInt();
                raison = event.getOption("raison").getAsString();

                if(Membre.contains(id)){
                    Membre.retrieve(id).removeReputation(montant);
                }else{
                    Membre.getMembreList().add(new Membre(id, 0, 0, 0));
                }

                event.replyEmbeds(new EmbedBuilder()
                                .setDescription("Vous venez de retirer "+montant+" de réputation à <@"+id+"> pour raison : " + raison)
                        .build()).queue();
                break;
        }
    }

}
