package me.kevinnovak.livecoordinates;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;

public class AliasManager {
    private HashMap<String, List<String>> _aliases;
    private Logger _logger;

    AliasManager(YamlConfiguration commandConfig, Logger logger) {
        _logger = logger;

        _aliases = new HashMap<>();
        for (String name : commandConfig.getKeys(false)) {
            try {
                List<String> aliases = commandConfig.getStringList(name);
                _aliases.put(name, aliases);
            } catch (Exception ex) {
                _logger.error("Could not load a command. There may be a problem with the command config file.");
                _logger.error(ex.toString());
            }
        }
    }

    public boolean isAlias(String name, String entry) {
        List<String> aliases = _aliases.get(name);
        for (String alias : aliases) {
            if (entry.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }
}
