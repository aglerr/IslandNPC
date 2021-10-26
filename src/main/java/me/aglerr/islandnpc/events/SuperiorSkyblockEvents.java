package me.aglerr.islandnpc.events;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandSchematicPasteEvent;
import me.aglerr.islandnpc.data.NPCTracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SuperiorSkyblockEvents implements Listener {

    private final NPCTracker npcTracker;
    public SuperiorSkyblockEvents(NPCTracker npcTracker){
        this.npcTracker = npcTracker;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onIslandSchematicPasted(IslandSchematicPasteEvent event){
        npcTracker.createNPC(event.getIsland().getUniqueId(), event.getLocation().clone(), true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onIslandDeletion(IslandDisbandEvent event){
        npcTracker.deleteNPCFromIsland(event.getIsland().getUniqueId());
    }

}
