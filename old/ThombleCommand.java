package dev.tehbrian.thimble.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.tehbrian.thimble.Thimble;
import dev.tehbrian.thimble.msesage.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import dev.tehbrian.thimble.arena.ArenaStatus;

@SuppressWarnings("unused")
@CommandAlias("thimble")
@Description("Various commands for Thimble.")
public class ThimbleCommand extends BaseCommand {

    private final Thimble main;

    public ThimbleCommand(Thimble main) {
        this.main = main;
    }

    @Subcommand("join")
    @CommandPermission("thimble.join")
    @Description("Join a game.")
    public void onJoin(Player player, @Single String arenaName) {
        dev.tehbrian.thimble.arena.Arena arena = main.getArenaManager().getArena(arenaName);
        if (arena == null) {
            player.sendMessage("Sorry, but there's no arena by that name.");
            return;
        }

        dev.tehbrian.thimble.game.Game game = arena.getGame();
        if (game == null) {
            player.sendMessage("Sorry, but there's no game going on in that arena right now.");
            return;
        }

        if (arena.getStatus() == ArenaStatus.IN_PROGRESS) {
            player.sendMessage("Sorry, but that game is in progress.");
            return;
        }

        if (main.getPlayerDataManager().getPlayerData(player).getArenaName() != null) {
            player.sendMessage("You can't do that, idiot. You're already in a game.");
        }

        game.join(player);
        player.sendMessage(String.format("Joined game in arena %s", arena.getName()));
    }

    @Subcommand("leave")
    @CommandPermission("thimble.leave")
    @Description("Leave a game.")
    public void onLeave(Player player, @Single String arenaName) {
        dev.tehbrian.thimble.arena.Arena arena = main.getArenaManager().getArena(arenaName);
        if (arena == null) {
            player.sendMessage("Sorry, but there's no arena by that name.");
            return;
        }

        dev.tehbrian.thimble.game.Game game = arena.getGame();
        if (game == null) {
            player.sendMessage("Sorry, but there's no game going on in that arena right now.");
            return;
        }

        game.leave(player);
        player.sendMessage(String.format("Left game in arena %s", arena.getName()));
    }

    @HelpCommand
    public void onHelp(Player player) {
        player.sendMessage("haha ur a furry");
    }

    @Subcommand("arena")
    public class Arena extends BaseCommand {

        @Subcommand("create")
        @CommandPermission("thimble.arena.create")
        @Description("Create an arena.")
        public void onArenaCreate(Player player, @Single String name, @Optional Location location) {
            if (location == null) {
                location = player.getLocation();
            }

            dev.tehbrian.thimble.arena.Arena arena = main.getArenaManager().createArena(name, location);

            main.getMessageHandler().sendMessageFormatted(player, Message.CREATE, arena.getName());
        }

        @Subcommand("destroy")
        @CommandPermission("thimble.arena.destroy")
        @Description("Destroy an arena.")
        public void onArenaDestroy(Player player, @Single String arenaName) {
            dev.tehbrian.thimble.arena.Arena arena = main.getArenaManager().getArena(arenaName);
            if (arena == null) {
                player.sendMessage("Sorry, but there's no arena by that name.");
                return;
            }

            player.sendMessage(String.format("Destroyed arena %s", arena.getName()));

            main.getArenaManager().destroyArena(arenaName);
        }

        @Subcommand("list")
        @CommandPermission("thimble.arena.list")
        @Description("List all arenas.")
        public void onArenaList(Player player) {
            StringBuilder message = new StringBuilder("Current arenas:");

            for (dev.tehbrian.thimble.arena.Arena arena : main.getArenaManager().getArenaMap().values()) {
                message.append(String.format(" %s,", arena.getName()));
            }

            player.sendMessage(message.toString());
        }

        @Subcommand("setspawn")
        @CommandPermission("thimble.arena.setspawn")
        @Description("Set arena spawn.")
        public void onArenaSetSpawn(Player player, String arenaName, @Optional Location location) {
            if (location == null) {
                location = player.getLocation();
            }

            dev.tehbrian.thimble.arena.Arena arena = main.getArenaManager().getArena(arenaName);
            if (arena == null) {
                player.sendMessage("Sorry, but there's no arena by that name.");
                return;
            }

            arena.setSpawn(location);

            player.sendMessage(String.format("Set spawn in arena %s to %s", arena.getName(), location.toString()));
        }

    }

    @Subcommand("game")
    public class Game extends BaseCommand {

        @Subcommand("start")
        @CommandPermission("thimble.game.start")
        @Description("Start a game.")
        public void onArenaStart(Player player, @Single String arenaName) {
            dev.tehbrian.thimble.arena.Arena arena = main.getArenaManager().getArena(arenaName);
            if (arena == null) {
                player.sendMessage("Sorry, but there's no arena by that name.");
                return;
            }

            arena.startGame();

            player.sendMessage(String.format("Started game in arena %s", arena.getName()));
        }

        @Subcommand("end")
        @CommandPermission("thimble.game.end")
        @Description("End a game.")
        public void onArenaEnd(Player player, @Single String arenaName) {
            dev.tehbrian.thimble.arena.Arena arena = main.getArenaManager().getArena(arenaName);
            if (arena == null) {
                player.sendMessage("Sorry, but there's no arena by that name.");
                return;
            }

            arena.stopGame();

            player.sendMessage(String.format("Ended game in arena %s", arena.getName()));
        }

    }

}
