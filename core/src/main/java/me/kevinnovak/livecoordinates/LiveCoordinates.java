package me.kevinnovak.livecoordinates;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.NumberFormat;

public class LiveCoordinates extends JavaPlugin implements Listener {
    private FileConfiguration _config;
    private Server _server;
    private Logger _logger;
    private InternalsProvider _internals;
    private BukkitScheduler _scheduler;

    private String _compassFormat;

    @Override
    public void onEnable() {
        super.onEnable();

        // Setup dependencies
        _server = Bukkit.getServer();
        _logger = new Logger(_server.getLogger(), "[LiveCoordinates]");
        _scheduler = _server.getScheduler();

        // Setup internals based on server version
        _logger.info("Determining server version.");
        try {
            String packageName = LiveCoordinates.class.getPackage().getName();
            String internalsName = _server.getClass().getPackage().getName().split("\\.")[3];
            _internals = (InternalsProvider) Class.forName(packageName + "." + internalsName).newInstance();
        } catch (Exception exception) {
            _logger.error("Could not find a valid implementation for this server version.");
            disablePlugin();
            return;
        }

        // Load config
        _logger.info("Loading config.");
        this.saveDefaultConfig();
        _config = this.getConfig();
        _compassFormat = ChatColor.translateAlternateColorCodes('&', _config.getString("compassFormat"));

        _logger.info("Registering events.");
        _server.getPluginManager().registerEvents(this, this);
        _scheduler.scheduleSyncRepeatingTask(this, () -> updateAllDisplays(), 0L, 20L);

        _logger.info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        _logger.info("Plugin disabled.");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        LocationVector fromVector = new LocationVector(event.getFrom());
        LocationVector toVector = new LocationVector(event.getTo());
        if (fromVector != toVector) {
            updateDisplay(event.getPlayer(), toVector);
        }
    }

    private void updateAllDisplays() {
        for (Player player : _server.getOnlinePlayers()) {
            updateDisplay(player);
        }
    }

    private void updateDisplay(Player player, LocationVector vector) {
        String message = _compassFormat
                .replace("{X}", NumberFormat.getIntegerInstance().format(vector.getX()))
                .replace("{Y}", NumberFormat.getIntegerInstance().format(vector.getY()))
                .replace("{Z}", NumberFormat.getIntegerInstance().format(vector.getZ()));
        _internals.sendActionBar(player, message);
    }

    private void updateDisplay(Player player) {
        LocationVector vector = new LocationVector(player.getLocation());
        updateDisplay(player, vector);
    }

    private void disablePlugin() {
        getServer().getPluginManager().disablePlugin(this);
    }
}
