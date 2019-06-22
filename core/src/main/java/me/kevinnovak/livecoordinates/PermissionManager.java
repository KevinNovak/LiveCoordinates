package me.kevinnovak.livecoordinates;

import org.bukkit.entity.Player;

public class PermissionManager {
    public static boolean hasPermission(Player player, Permission permission) {
        return player.hasPermission(permission.getText());
    }
}
