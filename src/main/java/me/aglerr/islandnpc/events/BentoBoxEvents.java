package me.aglerr.islandnpc.events;

import me.aglerr.islandnpc.data.NPCTracker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import world.bentobox.bentobox.api.events.island.IslandCreatedEvent;
import world.bentobox.bentobox.api.events.island.IslandDeleteEvent;
import world.bentobox.bentobox.api.events.island.IslandResettedEvent;
import world.bentobox.bentobox.database.objects.Island;

public class BentoBoxEvents implements Listener {

    private final NPCTracker npcTracker;
    public BentoBoxEvents(NPCTracker npcTracker){
        this.npcTracker = npcTracker;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onIslandCreated(IslandCreatedEvent event){
        Island island = event.getIsland();
        Location spawnLocation = island.getSpawnPoint(World.Environment.NORMAL);

        npcTracker.createNPC(island.getUniqueId(), spawnLocation, true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onIslandDeleted(IslandDeleteEvent event){
        Island island = event.getIsland();

        npcTracker.deleteNPCFromIsland(island.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onIslandResetted(IslandResettedEvent event){
        // Remove the NPC from the old island
        Island oldIsland = event.getOldIsland();
        npcTracker.deleteNPCFromIsland(oldIsland.getUniqueId());

        // And now, spawn the new NPC on the new island
        Island newIsland = event.getIsland();
        Location spawnLocation = newIsland.getSpawnPoint(World.Environment.NORMAL);
        npcTracker.createNPC(newIsland.getUniqueId(), spawnLocation, true);
    }

}
