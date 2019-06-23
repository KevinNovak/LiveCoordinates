package me.kevinnovak.livecoordinates.commands;

import org.bukkit.entity.Player;

public class CommandHelp implements CustomCommandExecutor {
    @Override
    public void execute(Player player) {
        player.sendMessage("help");
    }

    @Override
    public void execute(Player player, String[] args) {
        execute(player);
    }
}
