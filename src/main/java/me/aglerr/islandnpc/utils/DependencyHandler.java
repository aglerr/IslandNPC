package me.aglerr.islandnpc.utils;

import me.aglerr.islandnpc.IslandNPC;
import me.aglerr.islandnpc.events.SuperiorSkyblockEvents;
import me.aglerr.mclibs.libs.Common;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;

public class DependencyHandler {

    private static boolean SUPERIOR_SKYBLOCK;

    public static void loadDependencies(IslandNPC plugin){

        PluginManager pm = Bukkit.getPluginManager();

        if(pm.getPlugin("SuperiorSkyblock2") != null){
            SUPERIOR_SKYBLOCK = true;
            Common.log("Found SuperiorSkyblock2, registering events...");
            Common.log("- IslandSchematicPasteEvent");
            Common.log("- IslandDisbandEvent");

            pm.registerEvents(new SuperiorSkyblockEvents(plugin.getNPCTracker()), plugin);
        }

    }

    public static boolean isSuperiorSkyblock(){
        return SUPERIOR_SKYBLOCK;
    }

}
