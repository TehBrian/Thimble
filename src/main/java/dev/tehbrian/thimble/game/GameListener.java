package dev.tehbrian.thimble.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFallDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof final Player player) {
            final GamePlayer gamePlayer = null; // FIXME get the player somehow
            if (gamePlayer == null) {
                return;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler()
    public void onPlayerQuit(final PlayerQuitEvent event) {
        // TODO remove player from games on quit
    }

}
