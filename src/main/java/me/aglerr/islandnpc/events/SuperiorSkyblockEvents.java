package me.aglerr.islandnpc.events;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandSchematicPasteEvent;
import me.aglerr.islandnpc.data.NPCTracker;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SuperiorSkyblockEvents implements Listener {

    private final NPCTracker npcTracker;
    public SuperiorSkyblockEvents(NPCTracker npcTracker){
        this.npcTracker = npcTracker;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIslandSchematicPasted(IslandSchematicPasteEvent event){
        System.out.println("IslandSchematicPasteEvent is being called");
        Bukkit.broadcastMessage("Creating island npc for " + event.getIsland().getOwner().getName());
        npcTracker.createNPC(event.getIsland().getUniqueId(), event.getLocation().clone(), true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIslandDeletion(IslandDisbandEvent event){
        System.out.println("IslandDisbandEvent is being called");
        Bukkit.broadcastMessage("Deleting island npc for " + event.getIsland().getOwner().getName());
        npcTracker.deleteNPCFromIsland(event.getIsland().getUniqueId());
    }

}
