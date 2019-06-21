package me.kevinnovak.livecoordinates;

import org.bukkit.entity.Player;

public interface InternalsProvider {
    void sendActionBar(Player player, String message);
}
