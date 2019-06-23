package me.kevinnovak.livecoordinates.utils;

import me.kevinnovak.livecoordinates.models.Permission;
import org.bukkit.entity.Player;

public class PermissionManager {
    public static boolean hasPermission(Player player, Permission permission) {
        return player.hasPermission(permission.getText());
    }
}
