package de.nico.betakey.components.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import de.nico.betakey.BetaKey;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventListener implements Listener {
    private final BetaKey plugin;

    @EventHandler
    public void on(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if(!plugin.getBetaPlayers().containsKey(player.getName()) || !player.isOp()) {
            if(!plugin.getWhitelistedPlayers().contains(player.getName())) {
                if(plugin.getBetaMode()) {
                    if(plugin.getBetaPlayers().containsKey(player.getName())) {
                        event.setCancelled(false);
                    } else {
                        event.setCancelled(true);
                        plugin.getWindows().openEnterKeyForm(player);
                    }
                } else {
                    event.setCancelled(false);
                }
            } else {
                event.setCancelled(false);
            }
        } else {
            event.setCancelled(false);
        }
    }
}
