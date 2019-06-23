package me.kevinnovak.livecoordinates;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;

public class CommandManager {
    HashMap<String, List<String>> _commands;
    Logger _logger;

    CommandManager(YamlConfiguration commandConfig, Logger logger) {
        _logger = logger;

        _commands = new HashMap<>();
        for (String key : commandConfig.getKeys(false)) {
            try {
                List<String> aliases = commandConfig.getStringList(key);
                _commands.put(key, aliases);
            } catch (Exception ex) {
                _logger.error("Could not load a command. There may be a problem with the command config file.");
                _logger.error(ex.toString());
            }
        }
    }

    public boolean isCommand(String command, String entry) {
        List<String> aliases = _commands.get(command);
        for (String alias : aliases) {
            if (entry.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }
}
