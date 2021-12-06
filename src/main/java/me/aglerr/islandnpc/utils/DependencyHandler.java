package me.aglerr.islandnpc.utils;

import me.aglerr.islandnpc.IslandNPC;
import me.aglerr.islandnpc.events.SuperiorSkyblockEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;

public class DependencyHandler {

    private static boolean PLACEHOLDER_API;
    private static boolean SUPERIOR_SKYBLOCK;

    public static void loadDependencies(IslandNPC plugin){

        PluginManager pm = Bukkit.getPluginManager();

        if(pm.getPlugin("PlaceholderAPI") != null){
            PLACEHOLDER_API = true;
        }

        if(pm.getPlugin("SuperiorSkyblock2") != null){
            SUPERIOR_SKYBLOCK = true;
            Utils.log("Found SuperiorSkyblock2, registering events...");
            Utils.log("- IslandSchematicPasteEvent");
            Utils.log("- IslandDisbandEvent");

            pm.registerEvents(new SuperiorSkyblockEvents(plugin.getNPCTracker()), plugin);
        }

    }

    public static boolean isPlaceholderAPI(){
        return PLACEHOLDER_API;
    }

    public static boolean isSuperiorSkyblock(){
        return SUPERIOR_SKYBLOCK;
    }

}
