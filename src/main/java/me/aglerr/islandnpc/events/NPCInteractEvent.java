package me.aglerr.islandnpc.events;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import me.aglerr.islandnpc.IslandNPC;
import me.aglerr.islandnpc.config.ConfigValue;
import me.aglerr.islandnpc.utils.ClickCommand;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class NPCInteractEvent implements Listener {

    private final IslandNPC plugin;
    public NPCInteractEvent(IslandNPC plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNPCInteract(NPCRightClickEvent event){
        Player player = event.getClicker();
        NPC npc = event.getNPC();
        if(!plugin.getNPCTracker().isInteractingOwnNPC(npc, player)){
            return;
        }
        ClickCommand.handleClick(ConfigValue.CLICK_COMMANDS, player);
    }
}
