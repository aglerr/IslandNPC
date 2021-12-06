package me.aglerr.islandnpc.events;

import com.bgsoftware.superiorskyblock.api.island.Island;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.aglerr.islandnpc.data.NPCTracker;
import me.aglerr.islandnpc.utils.DependencyHandler;
import me.aglerr.islandnpc.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MoveNPCEvent implements Listener {

    private final NPCTracker npcTracker;
    public MoveNPCEvent(NPCTracker npcTracker){
        this.npcTracker = npcTracker;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDropItem(PlayerDropItemEvent event){
        NBTItem nbtItem = new NBTItem(event.getItemDrop().getItemStack());
        if(!nbtItem.hasKey("moveNPC")){
            return;
        }
        event.setCancelled(true);
        event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack hand = event.getItem();
        if(hand == null || hand.getType() == Material.AIR){
            return;
        }
        NBTItem nbtItem = new NBTItem(hand);
        if(!nbtItem.hasKey("moveNPC")){
            return;
        }
        event.setCancelled(true);
        if(event.getClickedBlock() == null){
            return;
        }
        Location placedLocation = event.getClickedBlock().getLocation().clone().add(0.5, 1, 0.5);
        if(DependencyHandler.isSuperiorSkyblock()){
            Island island = Utils.getSuperiorIsland(player);
            if(island == null || !island.isInside(placedLocation)){
                return;
            }
            npcTracker.moveNPC(island.getUniqueId(), placedLocation);
        }
        player.setItemInHand(new ItemStack(Material.AIR));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        ItemStack hand = player.getItemInHand();
        if(hand == null || hand.getType() == Material.AIR){
            return;
        }
        NBTItem nbtItem = new NBTItem(hand);
        if(!nbtItem.hasKey("moveNPC")){
            return;
        }
        event.setCancelled(true);
    }
}
