package dev.tehbrian.thimble.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class User {

    private final UUID uuid;
    private String arenaName;

    public User(final UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public String getArenaName() {
        return this.arenaName;
    }

    public void setArenaName(final String arenaName) {
        this.arenaName = arenaName;
    }

}
