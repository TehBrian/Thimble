package dev.tehbrian.thimble.user;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {

    private final Map<UUID, User> playerDataMap = new HashMap<>();

    public User getUser(final UUID uuid) {
        return this.playerDataMap.computeIfAbsent(uuid, User::new);
    }

    public User getUser(final Player player) {
        return this.getUser(player.getUniqueId());
    }

    public Map<UUID, User> getUserMap() {
        return this.playerDataMap;
    }

}
