package dev.tehbrian.thimble;

import dev.tehbrian.thimble.arena.ArenaService;
import dev.tehbrian.thimble.game.GameListener;
import dev.tehbrian.thimble.user.UserService;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Thimble extends JavaPlugin {

    private ArenaService arenaManager;
    private UserService playerDataManager;

    @Override
    public void onEnable() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new GameListener(), this);

        this.setupConfig();
        this.setupCommands();
    }

    private void setupConfig() {
        this.saveDefaultConfig();
    }

    private void setupCommands() {
        // Deleted.
    }

    public ArenaService getArenaService() {
        if (this.arenaManager == null) {
            this.arenaManager = new ArenaService(this);
        }
        return this.arenaManager;
    }

    public UserService getUserService() {
        if (this.playerDataManager == null) {
            this.playerDataManager = new UserService();
        }
        return this.playerDataManager;
    }

}

