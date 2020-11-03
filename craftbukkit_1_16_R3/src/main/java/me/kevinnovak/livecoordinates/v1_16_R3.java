package me.kevinnovak.livecoordinates;

import me.kevinnovak.livecoordinates.services.InternalsProvider;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class v1_16_R3 implements InternalsProvider {
    @Override
    public void sendActionBar(Player player, String message) {
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, ChatMessageType.GAME_INFO, player.getUniqueId());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
