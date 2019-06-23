package me.kevinnovak.livecoordinates.services;

import org.bukkit.entity.Player;

public interface InternalsProvider {
    void sendActionBar(Player player, String message);
}
