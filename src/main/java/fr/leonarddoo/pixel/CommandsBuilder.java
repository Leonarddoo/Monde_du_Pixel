package fr.leonarddoo.pixel;

import fr.leonarddoo.pixel.Main;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.Option;

public class CommandsBuilder extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().getGuildById(Main.getGuildId())
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

                .addCommands(Commands.slash("addpdj", "Permet d'ajouter des points de jeu à un membre.")
                        .addOption(OptionType.USER, "membre", "Membre", true)
                        .addOption(OptionType.INTEGER, "montant", "Montant", true)
                        .addOption(OptionType.STRING, "raison", "Raison", true))
                .addCommands(Commands.slash("removepdj", "Permet d'enlever des points de jeu à un membre.")
                        .addOption(OptionType.USER, "membre", "Membre", true)
                        .addOption(OptionType.INTEGER, "montant", "Montant", true)
                        .addOption(OptionType.STRING, "raison", "Raison", true))

                .addCommands(Commands.slash("bingo", "Permet de lancer une partie de Bingo.")
                        .addOption(OptionType.USER, "adversaire", "Adversaire", true)
                        .addOption(OptionType.INTEGER, "mise", "Mise (1-3) inclus", true))
                .addCommands(Commands.slash("pendu", "Permet de lancer une partie de Pendu.")
                        .addOption(OptionType.USER, "adversaire", "Adversaire", true)
                        .addOption(OptionType.INTEGER, "mise", "Mise (1-3) inclus"))


                .queue();
    }
}
