package com.bgsoftware.superiorskyblock.commands.command.admin;

import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.wrappers.SSuperiorPlayer;
import com.bgsoftware.superiorskyblock.Locale;
import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.commands.ICommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public final class CmdAdminMsg implements ICommand {

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("msg");
    }

    @Override
    public String getPermission() {
        return "superior.admin.msg";
    }

    @Override
    public String getUsage() {
        return "island admin msg <player-name> <msg...>";
    }

    @Override
    public int getMinArgs() {
        return 4;
    }

    @Override
    public int getMaxArgs() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return true;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        SuperiorPlayer targetPlayer = SSuperiorPlayer.of(args[2]);

        if(targetPlayer == null){
            Locale.INVALID_PLAYER.send(sender, args[2]);
            return;
        }

        if(!targetPlayer.asOfflinePlayer().isOnline()){
            Locale.PLAYER_NOT_ONLINE.send(sender);
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 3; i < args.length; i++)
            stringBuilder.append(" ").append(ChatColor.translateAlternateColorCodes('&', args[i]));

        Locale.sendMessage(targetPlayer, stringBuilder.toString().substring(1));
        Locale.MESSAGE_SENT.send(sender, targetPlayer.getName());
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        return null;
    }
}