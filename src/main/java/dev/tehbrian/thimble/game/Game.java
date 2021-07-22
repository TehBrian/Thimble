package dev.tehbrian.thimble.game;

import dev.tehbrian.thimble.Thimble;
import dev.tehbrian.thimble.arena.Arena;
import dev.tehbrian.thimble.arena.ArenaStatus;
import dev.tehbrian.thimble.arena.StopReason;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class Game implements Listener {

    private final Thimble main;
    private final Arena arena;
    private final Set<Material> playerMaterials;
    private final Map<UUID, GamePlayer> players = new HashMap<>();
    private final Set<Location> replacedBlocks = new HashSet<>();
    private final int minimumY;
    private int taskId;
    private Scoreboard scoreboard;
    private Objective objective;

    public Game(final Thimble main, final Arena arena, final Set<Material> playerMaterials, final int minimumY) {
        this.main = main;
        this.arena = arena;
        this.playerMaterials = playerMaterials;
        this.minimumY = minimumY;
    }

    public void checkPlayers() {
        for (final GamePlayer gamePlayer : this.players.values()) {
            final Player player = gamePlayer.getPlayer();
            if (player == null) {
                return;
            }

            final Location playerLoc = player.getLocation();
            final Block block = playerLoc.getBlock();
            final Block underBlock = block.getRelative(BlockFace.DOWN);

            if (block.getType() == Material.WATER) {
                int score = 1;
                if (this.isPlayerMaterial(block, BlockFace.NORTH)) {
                    score++;
                }
                if (this.isPlayerMaterial(block, BlockFace.WEST)) {
                    score++;
                }
                if (this.isPlayerMaterial(block, BlockFace.EAST)) {
                    score++;
                }
                if (this.isPlayerMaterial(block, BlockFace.SOUTH)) {
                    score++;
                }

                gamePlayer.addScore(score);
                this.objective.getScore(player.getName()).setScore(gamePlayer.getScore());

                block.setType(gamePlayer.getMaterial());
                this.replacedBlocks.add(block.getLocation());

                player.teleport(this.arena.getSpawn());
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
            } else if (player.isOnGround() && player
                    .getLocation()
                    .getY() < this.minimumY && this.playerMaterials.contains(underBlock.getType())) {
                player.teleport(this.arena.getSpawn());
            }
        }
    }


    private boolean isPlayerMaterial(final Block block, final BlockFace blockFace) {
        return this.playerMaterials.contains(block.getRelative(blockFace).getType());
    }

    public void join(final Player player) {
        this.players.computeIfAbsent(player.getUniqueId(), GamePlayer::new);
        this.main.getUserService().getUser(player).setArenaName(this.arena.getName());
    }

    public void leave(final Player player) {
        this.players.remove(player.getUniqueId());
        this.main.getUserService().getUser(player).setArenaName(null);
    }

    public void start() {
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.main, this::checkPlayers, 1L, 1L);
        Bukkit.getPluginManager().registerEvents(this, this.main);

        // FIXME this will break if there are more players than there are wool types
        final List<Material> materials = new ArrayList<>(this.playerMaterials);
        for (final GamePlayer gamePlayer : this.players.values()) {
            gamePlayer.setMaterial(materials.remove(new Random().nextInt(materials.size())));
        }

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective(
                "thimble_" + this.arena.getName(),
                "dummy",
                "thimble - " + this.arena.getName()
        );
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (final GamePlayer gamePlayer : this.players.values()) {
            final Player player = gamePlayer.getPlayer();
            this.objective.getScore(player.getName()).setScore(0);
            player.setScoreboard(this.scoreboard);
        }

        for (final GamePlayer gamePlayer : this.players.values()) {
            gamePlayer.getPlayer().teleport(this.arena.getSpawn());
        }

        this.arena.setStatus(ArenaStatus.IN_PROGRESS);
    }

    public void stop(final StopReason reason) {
        Bukkit.getScheduler().cancelTask(this.taskId);
        EntityDamageEvent.getHandlerList().unregister(this);

        for (final Location location : this.replacedBlocks) {
            location.getBlock().setType(Material.WATER);
        }

        for (final GamePlayer gamePlayer : this.players.values()) {
            final Player player = gamePlayer.getPlayer();
            player.setScoreboard(this.main.getServer().getScoreboardManager().getMainScoreboard());
        }

        for (final GamePlayer gamePlayer : this.players.values()) {
            gamePlayer.getPlayer().sendMessage(String.format("Congratulations, you're done! Your score was %s", gamePlayer.getScore()));
        }

        for (final GamePlayer gamePlayer : this.players.values()) {
            gamePlayer.getPlayer().teleport(gamePlayer.getPreviousLocation());
        }

        this.arena.setStatus(ArenaStatus.LOBBY);
    }

}
