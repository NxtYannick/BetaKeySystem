package de.nico.betakey.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import de.nico.betakey.BetaKey;
import de.nico.betakey.components.language.Language;

public class BetaUICommand extends PluginCommand<BetaKey> {
    public BetaUICommand(BetaKey plugin) {
        super(Language.getNP("command-name"), plugin);
        this.setDescription(Language.getNP("command-description"));
        this.setAliases(new String[] {Language.getNP("command-alias"), "beta", "bui", "btui"});
        this.setPermission("beta.cmd");
        this.setUsage(Language.getNP("command-usage"));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender.isPlayer()) {
            Player player = (Player) sender;
            if(player.hasPermission(this.getPermission())) {
                this.getPlugin().getWindows().openBetaForm(player);
                player.sendPopup(Language.getNP("command-message"));
            }
        }
        return false;
    }
}
