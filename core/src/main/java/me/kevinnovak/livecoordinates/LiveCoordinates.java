package me.kevinnovak.livecoordinates;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class LiveCoordinates extends JavaPlugin implements Listener {
    private FileConfiguration _config;
    private Server _server;
    private Logger _logger;
    private BukkitScheduler _scheduler;
    private CommandManager _commandManager;
    private InternalsProvider _internals;

    private String _coordinatesFormat;

    private List<String> baseAliases = Arrays.asList("lc", "livecoordinates", "livecoords", "livecoor", "livec", "lcoordinates", "lcoords", "lcoor");

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
        _coordinatesFormat = ChatColor.translateAlternateColorCodes('&', _config.getString("coordinatesFormat"));

        File commandsFile = new File(this.getDataFolder() + "/commands.yml");
        if (!commandsFile.exists()) {
            _logger.info("Copying default commands file.");
            this.saveResource("commands.yml", false);
        }
        _logger.info("Loading commands file.");
        YamlConfiguration commandConfig = YamlConfiguration.loadConfiguration(commandsFile);
        _commandManager = new CommandManager(commandConfig, _logger);

        _logger.info("Registering events.");
        _server.getPluginManager().registerEvents(this, this);

        long updateInterval = _config.getInt("updateInterval") / 50;
        _scheduler.scheduleSyncRepeatingTask(this, () -> updateAllDisplays(), 0L, updateInterval);

        _logger.info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        _logger.info("Plugin disabled.");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!PermissionManager.hasPermission(player, Permission.DISPLAY)) {
            return;
        }

        LocationVector fromVector = new LocationVector(event.getFrom());
        LocationVector toVector = new LocationVector(event.getTo());
        if (fromVector != toVector) {
            updateDisplay(event.getPlayer(), toVector);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        _logger.info("Command ran");
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        String command = cmd.getName().toLowerCase();
        _logger.info(command);
        if (!baseAliases.contains(command)) {
            return true;
        }

        if (args.length <= 0) {
            player.sendMessage("help");
            return true;
        }

        String subCommand = args[0];
        if (_commandManager.isCommand("help", subCommand)) {
            player.sendMessage("help");
            return true;
        }

        if (_commandManager.isCommand("toggle", subCommand)) {
            player.sendMessage("toggle");
            return true;
        }

        player.sendMessage("help");
        return true;
    }

    private void updateAllDisplays() {
        for (Player player : _server.getOnlinePlayers()) {
            if (!PermissionManager.hasPermission(player, Permission.DISPLAY)) {
                return;
            }
            updateDisplay(player);
        }
    }

    private void updateDisplay(Player player, LocationVector vector) {
        String message = _coordinatesFormat
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
        _server.getPluginManager().disablePlugin(this);
    }
}
