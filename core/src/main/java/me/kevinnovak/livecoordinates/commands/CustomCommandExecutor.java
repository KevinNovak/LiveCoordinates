package me.kevinnovak.livecoordinates.commands;

import org.bukkit.entity.Player;

public interface CustomCommandExecutor {
    void execute(Player player);

    void execute(Player player, String[] args);
}
