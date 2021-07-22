package dev.tehbrian.thimble.arena;

import dev.tehbrian.thimble.Thimble;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class ArenaService {

    private final Thimble main;
    private final Map<String, Arena> arenaMap = new HashMap<>();

    public ArenaService(final Thimble main) {
        this.main = main;
    }

    public Arena getArena(final String name) {
        return this.arenaMap.get(name);
    }

    public Map<String, Arena> getArenaMap() {
        return this.arenaMap;
    }

    public Arena createArena(final String name, final Location spawn) {
        final Arena arena = new Arena(this.main, name, spawn);
        this.arenaMap.put(name, arena);
        return arena;
    }

    public void destroyArena(final String name) {
        this.arenaMap.remove(name);
    }

}
