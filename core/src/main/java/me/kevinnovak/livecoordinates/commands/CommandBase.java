package me.kevinnovak.livecoordinates.commands;

import me.kevinnovak.livecoordinates.utils.AliasManager;
import me.kevinnovak.livecoordinates.utils.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBase implements CommandExecutor {
    private Logger _logger;
    private AliasManager _aliasManager;

    // Commands
    private CustomCommandExecutor _commandHelp = new CommandHelp();
    private CustomCommandExecutor _commandToggle = new CommandToggle();

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
            _commandHelp.execute(player);
            return true;
        }

        String subCommand = args[0];
        if (_aliasManager.isAlias("help", subCommand)) {
            _commandHelp.execute(player);
            return true;
        }

        if (_aliasManager.isAlias("toggle", subCommand)) {
            _commandToggle.execute(player);
            return true;
        }

        _commandHelp.execute(player);
        return true;
    }
}
