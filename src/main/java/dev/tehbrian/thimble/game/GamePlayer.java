package dev.tehbrian.thimble.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GamePlayer {

    private final UUID uuid;

    private int score = 0;
    private Material material = Material.STONE;
    private Location previousLocation;

    public GamePlayer(final UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(final int score) {
        this.score = score;
    }

    public void addScore(final int score) {
        this.score += score;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(final Material material) {
        this.material = material;
    }

    public Location getPreviousLocation() {
        return this.previousLocation;
    }

    public void setPreviousLocation(final Location previousLocation) {
        this.previousLocation = previousLocation;
    }

}
