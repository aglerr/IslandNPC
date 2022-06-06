package me.aglerr.islandnpc;

import me.aglerr.islandnpc.commands.MainCommand;
import me.aglerr.islandnpc.config.ConfigManager;
import me.aglerr.islandnpc.config.ConfigValue;
import me.aglerr.islandnpc.data.NPCTracker;
import me.aglerr.islandnpc.events.MoveNPCEvent;
import me.aglerr.islandnpc.events.NPCInteractEvent;
import me.aglerr.islandnpc.utils.DependencyHandler;
import me.aglerr.islandnpc.utils.Item;
import me.aglerr.mclibs.MCLibs;
import me.aglerr.mclibs.libs.Common;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class IslandNPC extends JavaPlugin {

    private final NPCTracker npcTracker = new NPCTracker();

    @Override
    public void onEnable(){
        // MCLibs Init
        MCLibs.init(this);
        Common.setPrefix("[IslandNPC]");

        ConfigManager.initialize();
        ConfigValue.initialize();
        Item.init();

        npcTracker.loadIslandNPCs();
        registerEvents();
        registerCommands();

        Bukkit.getScheduler().runTask(this, npcTracker::reloadAllNPCs);
    }

    @Override
    public void onDisable(){
        npcTracker.saveIslandNPCs();
    }

    public void reloadEverything(){
        ConfigManager.reloadAllConfigs();
        ConfigValue.initialize();
        Item.init();

        npcTracker.reloadAllNPCs();
    }

    private void registerEvents(){
        DependencyHandler.loadDependencies(this);

        Bukkit.getPluginManager().registerEvents(new NPCInteractEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new MoveNPCEvent(npcTracker), this);
    }

    private void registerCommands(){
        this.getCommand("islandnpc").setExecutor(new MainCommand(this));
    }

    public NPCTracker getNPCTracker(){
        return npcTracker;
    }

}
