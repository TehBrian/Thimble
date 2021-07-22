package dev.tehbrian.thimble.arena;

import dev.tehbrian.thimble.Thimble;
import dev.tehbrian.thimble.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.Set;

public class Arena {

    private final Thimble main;
    private final String name;
    private final Set<Material> playerMaterials = Tag.WOOL.getValues();
    private ArenaStatus status = ArenaStatus.LOBBY;
    private Location spawn;
    private Game game;

    public Arena(final Thimble main, final String name, final Location spawn) {
        this.main = main;
        this.name = name;
        this.spawn = spawn;
    }

    public String getName() {
        return this.name;
    }

    public Location getSpawn() {
        return this.spawn;
    }

    public void setSpawn(final Location spawn) {
        this.spawn = spawn;
    }

    public Game getGame() {
        if (this.game == null) {
            this.game = new Game(this.main, this, this.playerMaterials, 30);
        }
        return this.game;
    }

    public void countdown(final int seconds) {
        Bukkit.getScheduler().runTaskLater(this.main, this::startGame, seconds * 20);
        this.status = ArenaStatus.COUNTDOWN;
    }

    public void startGame() {
        this.getGame().start();
        this.status = ArenaStatus.STARTING;
    }

    public void stopGame() {
        this.getGame().stop(StopReason.FORCE);

    }

    public void stopGame(final StopReason reason) {
        this.getGame().stop(reason);
        this.status = ArenaStatus.STOPPING;
    }

    public ArenaStatus getStatus() {
        return this.status;
    }

    public void setStatus(final ArenaStatus status) {
        this.status = status;
    }

}
