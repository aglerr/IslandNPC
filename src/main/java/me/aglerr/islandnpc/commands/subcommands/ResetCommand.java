package me.aglerr.islandnpc.commands.subcommands;

import com.bgsoftware.superiorskyblock.api.island.Island;
import me.aglerr.islandnpc.IslandNPC;
import me.aglerr.islandnpc.commands.SubCommand;
import me.aglerr.islandnpc.config.ConfigValue;
import me.aglerr.islandnpc.utils.DependencyHandler;
import me.aglerr.islandnpc.utils.Utils;
import me.aglerr.lazylibs.libs.Common;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ResetCommand extends SubCommand {

    @Override
    public @NotNull String getName() {
        return "reset";
    }

    @Override
    public @Nullable String getPermission() {
        return "islandnpc.admin";
    }

    @Override
    public @NotNull List<String> parseTabCompletion(IslandNPC plugin, CommandSender sender, String[] args) {

        if(args.length == 2){
            return Common.getOnlinePlayersByName();
        }

        return new ArrayList<>();
    }

    @Override
    public void execute(IslandNPC plugin, CommandSender sender, String[] args) {

        if(args.length < 2){
            sender.sendMessage(Common.color("&cUsage: /islandnpc reset <player>"));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if(player == null){
            sender.sendMessage(Common.color(ConfigValue.INVALID_PLAYER));
            return;
        }

        if(DependencyHandler.isSuperiorSkyblock()){
            Island island = Utils.getSuperiorIsland(player);
            if(island == null){
                sender.sendMessage(Common.color(ConfigValue.TARGET_NO_ISLAND));
                return;
            }
            plugin.getNPCTracker().deleteNPCFromIsland(island.getUniqueId());
            plugin.getNPCTracker().createNPC(island.getUniqueId(), island.getTeleportLocation(World.Environment.NORMAL), true);
        }

        if(DependencyHandler.isBentoBox()){
            world.bentobox.bentobox.database.objects.Island island = Utils.getBentoIsland(player);
            if(island == null){
                sender.sendMessage(Common.color(ConfigValue.TARGET_NO_ISLAND));
                return;
            }
            plugin.getNPCTracker().deleteNPCFromIsland(island.getUniqueId());
            plugin.getNPCTracker().createNPC(island.getUniqueId(), island.getSpawnPoint(World.Environment.NORMAL), true);
        }

    }

}
