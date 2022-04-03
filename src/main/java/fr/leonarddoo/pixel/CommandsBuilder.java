package fr.leonarddoo.pixel;

import fr.leonarddoo.pixel.Main;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class CommandsBuilder extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().getGuildById(Main.getGuildid())
                .updateCommands()

                .addCommands(Commands.slash("profil", "Permet d'afficher le profil d'un membre.")
                        .addOption(OptionType.USER, "membre", "Membre", true))


                .addCommands(Commands.slash("rep", "Permet de connaître la réputation d'un membre.")
                        .addOption(OptionType.USER, "membre", "Membre", true))
                .addCommands(Commands.slash("addrep", "Permet d'ajouter de la réputation à un membre.")
                        .addOption(OptionType.USER, "membre", "Membre", true)
                        .addOption(OptionType.INTEGER, "montant", "Montant", true)
                        .addOption(OptionType.STRING, "raison", "Raison", true))
                .addCommands(Commands.slash("removerep", "Permet d'enlever de la réputation à un membre.")
                        .addOption(OptionType.USER, "membre", "Membre", true)
                        .addOption(OptionType.INTEGER, "montant", "Montant", true)
                        .addOption(OptionType.STRING, "raison", "Raison", true))


                .addCommands(Commands.slash("setupticket", "Permet de créer le panel de ticket."))


                .addCommands(Commands.slash("pendu", "Permet de lancer un pendu"))


                .queue();
    }
}
