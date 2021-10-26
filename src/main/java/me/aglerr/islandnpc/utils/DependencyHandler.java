package me.aglerr.islandnpc.utils;

import me.aglerr.lazylibs.libs.Common;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;

public class DependencyHandler {

    private static boolean PLACEHOLDER_API = false;
    private static boolean SUPERIOR_SKYBLOCK = false;
    private static boolean BENTO_BOX = false;

    public static void findDependency(){

        PluginManager pm = Bukkit.getPluginManager();

        if(pm.getPlugin("PlaceholderAPI") != null){
            PLACEHOLDER_API = true;
        }

        if(pm.getPlugin("SuperiorSkyblock2") != null){
            SUPERIOR_SKYBLOCK = true;
            Common.log(ChatColor.RESET, "Found SuperiorSkyblock2, registering events...");
            Common.log(ChatColor.RESET, "- IslandSchematicPasteEvent");
            Common.log(ChatColor.RESET, "- IslandDisbandEvent");
        }
        if(pm.getPlugin("BentoBox") != null){
            BENTO_BOX = true;
            Common.log(ChatColor.RESET, "Found BentoBox, registering events...");
            Common.log(ChatColor.RESET, "- IslandCreatedEvent");
            Common.log(ChatColor.RESET, "- IslandDeleteEvent");
            Common.log(ChatColor.RESET, "- IslandResettedEvent");
        }

    }

    public static boolean isPlaceholderAPI(){
        return PLACEHOLDER_API;
    }

    public static boolean isSuperiorSkyblock(){
        return SUPERIOR_SKYBLOCK;
    }

    public static boolean isBentoBox(){
        return BENTO_BOX;
    }


}
