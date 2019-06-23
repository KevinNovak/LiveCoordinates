package me.kevinnovak.livecoordinates.commands;

import org.bukkit.entity.Player;

public class CommandToggle implements CustomCommandExecutor {
    @Override
    public void execute(Player player) {
        player.sendMessage("toggle");
    }

    @Override
    public void execute(Player player, String[] args) {
        execute(player);
    }
}
