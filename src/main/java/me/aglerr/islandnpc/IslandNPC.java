package me.aglerr.islandnpc;

import me.aglerr.islandnpc.commands.MainCommand;
import me.aglerr.islandnpc.config.ConfigManager;
import me.aglerr.islandnpc.config.ConfigValue;
import me.aglerr.islandnpc.data.NPCTracker;
import me.aglerr.islandnpc.events.BentoBoxEvents;
import me.aglerr.islandnpc.events.MoveNPCEvent;
import me.aglerr.islandnpc.events.NPCInteractEvent;
import me.aglerr.islandnpc.events.SuperiorSkyblockEvents;
import me.aglerr.islandnpc.inventory.items.ItemsLoader;
import me.aglerr.islandnpc.inventory.libs.LazyInventoryManager;
import me.aglerr.islandnpc.utils.DependencyHandler;
import me.aglerr.lazylibs.LazyLibs;
import me.aglerr.lazylibs.libs.Common;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class IslandNPC extends JavaPlugin {

    private final NPCTracker npcTracker = new NPCTracker();
    private final ItemsLoader itemsLoader = new ItemsLoader();

    private static IslandNPC instance;

    @Override
    public void onEnable(){
        instance = this;
        LazyLibs.inject(this);
        Common.setPrefix("[IslandNPC]");

        ConfigManager.initialize();
        ConfigValue.initialize();
        itemsLoader.loadItems();

        npcTracker.loadIslandNPCs();
        registerEvents();
        registerCommands();

        Bukkit.getScheduler().runTask(this, npcTracker::reloadAllNPCs);
    }

    @Override
    public void onDisable(){
        npcTracker.saveIslandNPCs();
    }

    public static IslandNPC getInstance(){
        return instance;
    }

    public void reloadEverything(){
        ConfigManager.reloadAllConfigs();
        ConfigValue.initialize();

        itemsLoader.loadItems();
        npcTracker.reloadAllNPCs();
    }

    private void registerEvents(){
        DependencyHandler.findDependency();
        LazyInventoryManager.register(this);
        Bukkit.getPluginManager().registerEvents(new NPCInteractEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new MoveNPCEvent(npcTracker), this);

        if(DependencyHandler.isSuperiorSkyblock()){
            Bukkit.getPluginManager().registerEvents(new SuperiorSkyblockEvents(npcTracker), this);
        }

        if(DependencyHandler.isBentoBox()){
            Bukkit.getPluginManager().registerEvents(new BentoBoxEvents(npcTracker), this);
        }

        Common.log(ChatColor.RESET, "Successfully registered the events!");
    }

    private void registerCommands(){
        this.getCommand("islandnpc").setExecutor(new MainCommand(this));
    }

    public ItemsLoader getItemsLoader(){
        return itemsLoader;
    }

    public NPCTracker getNPCTracker(){
        return npcTracker;
    }

}
