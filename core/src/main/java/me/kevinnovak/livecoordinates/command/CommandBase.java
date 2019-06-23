package me.kevinnovak.livecoordinates.command;

import me.kevinnovak.livecoordinates.AliasManager;
import me.kevinnovak.livecoordinates.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBase implements CommandExecutor {
    private Logger _logger;
    private AliasManager _aliasManager;

    public CommandBase(Logger logger, AliasManager aliasManager) {
        _logger = logger;
        _aliasManager = aliasManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        _logger.info("Command ran");
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if (args.length <= 0) {
            player.sendMessage("help");
            return true;
        }

        String subCommand = args[0];
        if (_aliasManager.isAlias("help", subCommand)) {
            player.sendMessage("help");
            return true;
        }

        if (_aliasManager.isAlias("toggle", subCommand)) {
            player.sendMessage("toggle");
            return true;
        }

        player.sendMessage("help");
        return true;
    }
}
