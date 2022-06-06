package me.aglerr.islandnpc.commands.subcommands;

import me.aglerr.islandnpc.IslandNPC;
import me.aglerr.islandnpc.commands.SubCommand;
import me.aglerr.islandnpc.config.ConfigValue;
import me.aglerr.islandnpc.utils.Item;
import me.aglerr.islandnpc.utils.Utils;
import me.aglerr.mclibs.libs.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MoveNPCCommand extends SubCommand {

    @Override
    public @NotNull String getName() {
        return "movenpc";
    }

    @Override
    public @Nullable String getPermission() {
        return "islandnpc.movenpc";
    }

    @Override
    public @Nullable List<String> parseTabCompletion(IslandNPC plugin, CommandSender sender, String[] args) {
        if(args.length == 2){
            return null;
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(IslandNPC plugin, CommandSender sender, String[] args) {
        if(args.length == 1 && !(sender instanceof Player)){
            Common.sendMessage(sender, "&cUsage: /islandnpc movenpc (player)");
        } else {
            Player player = (Player) sender;
            Common.sendMessage(player, ConfigValue.MOVE_NPC_MESSAGE);
            if(player.getInventory().contains(Item.MOVE_ITEM)){
                return;
            }
            player.getInventory().addItem(Item.MOVE_ITEM);
        }
        if(args.length == 2 && sender.hasPermission("islandnpc.movenpc.others")){
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null){
                Common.sendMessage(sender, ConfigValue.INVALID_PLAYER);
                return;
            }
            Common.sendMessage(player, ConfigValue.MOVE_NPC_MESSAGE);
            if(player.getInventory().contains(Item.MOVE_ITEM)){
                return;
            }
            player.getInventory().addItem(Item.MOVE_ITEM);
        } else {
            Common.sendMessage(sender, ConfigValue.NO_PERMISSION_MESSAGE);
        }
    }

}
