package ru.velialcult.coloredprefix.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.velialcult.coloredprefix.menu.ChangePrefixColorMenu;
import ru.velialcult.library.bukkit.utils.PlayerUtil;

public class ColoredPrefixCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (PlayerUtil.senderIsPlayer(commandSender)) {
            Player player = (Player) commandSender;

            ChangePrefixColorMenu.generateInventory(player);
        }

        return false;
    }
}
