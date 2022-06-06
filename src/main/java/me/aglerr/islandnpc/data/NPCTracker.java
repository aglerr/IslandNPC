package me.aglerr.islandnpc.data;

import com.bgsoftware.superiorskyblock.api.island.Island;
import me.aglerr.islandnpc.config.ConfigManager;
import me.aglerr.islandnpc.config.ConfigValue;
import me.aglerr.islandnpc.utils.DependencyHandler;
import me.aglerr.islandnpc.utils.Utils;
import me.aglerr.mclibs.libs.Common;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.*;
import net.citizensnpcs.util.Anchor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

public class NPCTracker {

    private final Map<UUID, Integer> SislandNPCs = new HashMap<>();

    public boolean isInteractingOwnNPC(NPC npc, Player player){
        if(DependencyHandler.isSuperiorSkyblock()){
            Island island = Utils.getSuperiorIsland(player);
            if(island == null ||
                !SislandNPCs.containsKey(island.getUniqueId())){
                return false;
            }
            return npc.getId() == SislandNPCs.get(island.getUniqueId());
        }
        return false;
    }

    public void moveNPC(UUID uuid, Location location){
        // If the island doesn't have NPC, just return
        if(!SislandNPCs.containsKey(uuid)){
            return;
        }
        // Get the NPC object
        NPC npc = CitizensAPI.getNPCRegistry().getById(SislandNPCs.get(uuid));
        npc.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public void createNPC(UUID uuid, Location location, boolean modify){
        // If the island already have npc, stop the code
        if(SislandNPCs.containsKey(uuid)){
            return;
        }
        // Create the NPC and teleport it to the desired location
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.valueOf(ConfigValue.NPC_ENTITY_TYPE), getName());
        Location finalLocation = modify ?
                location.add(getNumber(ConfigValue.OFFSET_X), getNumber(ConfigValue.OFFSET_Y), getNumber(ConfigValue.OFFSET_Z)) :
                location;
        npc.spawn(finalLocation);
        addNPCDefaultTraits(npc);
        SislandNPCs.put(uuid, npc.getId());
    }

    public void deleteNPCFromIsland(UUID uuid){
        // If the island doesn't have any NPC, just stop the code
        if(!SislandNPCs.containsKey(uuid)){
            return;
        }
        // If the island has NPC tracked but the NPC doesn't exist, stop the code
        NPC npc = CitizensAPI.getNPCRegistry().getById(SislandNPCs.get(uuid));
        if(npc == null){
            return;
        }
        npc.destroy();
        SislandNPCs.remove(uuid);
    }

    public void saveIslandNPCs(){
        long startTime = System.currentTimeMillis();
        Common.log("&rTrying to save all Island NPC(s)");

        FileConfiguration config = ConfigManager.DATA.getConfig();
        SislandNPCs.forEach((uuid, npcId) -> {
            config.set("ssb." + uuid.toString(), npcId);
        });
        ConfigManager.DATA.saveConfig();

        long timePassed = startTime - System.currentTimeMillis();
        Common.log("&rSuccessfully saved all Island NPC(s) - took {ms}ms"
                .replace("{ms}", timePassed + ""));
    }

    public void loadIslandNPCs(){
        long startTime = System.currentTimeMillis();
        Common.log("&rTrying to load all Island NPC(s)");

        FileConfiguration config = ConfigManager.DATA.getConfig();
        if(config.isConfigurationSection("ssb")){
            for(String uuid : config.getConfigurationSection("ssb").getKeys(false)){
                int npcId = config.getInt("ssb." + uuid);
                SislandNPCs.put(UUID.fromString(uuid), npcId);
            }
        }

        long timePassed = startTime - System.currentTimeMillis();
        Common.log("&rSuccessfully loaded all Island NPC(s) - took {ms}ms"
                .replace("{ms}", timePassed + ""));
    }

    private double getNumber(double number){
        String stringNumber = String.valueOf(number);
        if(stringNumber.contains("-")){
            return number;
        }
        return +number;
    }

    private void addNPCDefaultTraits(NPC npc){
        npc.setBukkitEntityType(EntityType.valueOf(ConfigValue.NPC_ENTITY_TYPE));
        npc.getOrAddTrait(LookClose.class).lookClose(ConfigValue.LOOK_CLOSE);
        npc.getOrAddTrait(Gravity.class).gravitate(ConfigValue.DISABLE_GRAVITY);
        if(ConfigValue.ANCHOR){
            Anchor anchor = npc.getOrAddTrait(Anchors.class).getAnchor("spawn");
            if(anchor != null){
                npc.getOrAddTrait(Anchors.class).removeAnchor(anchor);
            }
            npc.getOrAddTrait(Anchors.class).addAnchor("spawn", npc.getStoredLocation());
        }
        npc.getOrAddTrait(HologramTrait.class).clear();
        npc.getOrAddTrait(HologramTrait.class).setLineHeight(ConfigValue.HOLOGRAM_HEIGHT);
        npc.getOrAddTrait(HologramTrait.class).setDirection(HologramTrait.HologramDirection.TOP_DOWN);
        List<String> lines = new ArrayList<>(ConfigValue.HOLOGRAM_LINES);
        lines.remove(ConfigValue.HOLOGRAM_LINES.size() - 1);
        for(String line : lines){
            npc.getOrAddTrait(HologramTrait.class).addLine(line);
        }
        npc.getOrAddTrait(HologramTrait.class).run();
        if(npc.getEntity().getType() == EntityType.PLAYER){
            npc.getOrAddTrait(SkinTrait.class).setSkinName(ConfigValue.NPC_SKIN, true);
        }
    }

    public void reloadAllNPCs(){
        SislandNPCs.forEach((uuid, id) -> {
            NPC npc = CitizensAPI.getNPCRegistry().getById(id);
            if(npc != null){
                addNPCDefaultTraits(npc);
            }
        });
    }

    private String getName(){
        return ConfigValue.HOLOGRAM_LINES.get(ConfigValue.HOLOGRAM_LINES.size() - 1);
    }

}
