package me.aglerr.islandnpc.data;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.aglerr.islandnpc.config.ConfigManager;
import me.aglerr.islandnpc.config.ConfigValue;
import me.aglerr.islandnpc.utils.DependencyHandler;
import me.aglerr.islandnpc.utils.Utils;
import me.aglerr.lazylibs.libs.Common;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.*;
import net.citizensnpcs.util.Anchor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;

import java.util.*;

public class NPCTracker {

    private final Map<UUID, Integer> SislandNPCs = new HashMap<>();
    private final Map<String, Integer> BislandNPCs = new HashMap<>();

    public boolean isInteractingOwnNPC(NPC npc, Player player){
        if(DependencyHandler.isSuperiorSkyblock()){
            SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
            if(superiorPlayer.getIsland() == null){
                return false;
            }
            if(!SislandNPCs.containsKey(superiorPlayer.getIsland().getUniqueId())){
                return false;
            }
            int ownNPC = SislandNPCs.get(superiorPlayer.getIsland().getUniqueId());
            return npc.getId() == ownNPC;
        }
        if(DependencyHandler.isBentoBox()){
            User user = BentoBox.getInstance().getPlayersManager().getUser(player.getUniqueId());
            World world = null;

            for(GameModeAddon gameModeAddon : BentoBox.getInstance().getAddonsManager().getGameModeAddons()){
                System.out.println("Gamemode: " + gameModeAddon.getDescription().getName());
                if(!gameModeAddon.getDescription().getName().equalsIgnoreCase("BSkyblock")) {
                    continue;
                }
                world = gameModeAddon.getOverWorld();
            }
            if(world == null){
                return false;
            }
            Island island = BentoBox.getInstance().getIslandsManager().getIsland(world, user);
            if(island == null){
                return false;
            }
            if(!BislandNPCs.containsKey(island.getUniqueId())){
                return false;
            }
            int ownNPC = BislandNPCs.get(island.getUniqueId());
            return npc.getId() == ownNPC;
        }
        return false;
    }

    public void moveNPC(String uuid, Location location){
        // If the island doesn't have NPC, just return
        if(!BislandNPCs.containsKey(uuid)){
            return;
        }
        // Get the NPC object
        NPC npc = CitizensAPI.getNPCRegistry().getById(BislandNPCs.get(uuid));
        npc.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
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

    public void createNPC(String uuid, Location location, boolean modify){
        // If the island already have npc, stop the code
        if(BislandNPCs.containsKey(uuid)){
            return;
        }
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.valueOf(ConfigValue.NPC_ENTITY_TYPE), getName());
        BislandNPCs.put(uuid, npc.getId());
        Location finalLocation = modify ? location.add(getNumber(ConfigValue.OFFSET_X), getNumber(ConfigValue.OFFSET_Y), getNumber(ConfigValue.OFFSET_Z)) : location;
        npc.spawn(finalLocation);
        addNPCDefaultTraits(npc);
    }

    public void createNPC(UUID uuid, Location location, boolean modify){
        // If the island already have npc, stop the code
        if(SislandNPCs.containsKey(uuid)){
            return;
        }
        // Create the NPC and teleport it to the desired location
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.valueOf(ConfigValue.NPC_ENTITY_TYPE), getName());
        SislandNPCs.put(uuid, npc.getId());
        Location finalLocation = modify ? location.add(getNumber(ConfigValue.OFFSET_X), getNumber(ConfigValue.OFFSET_Y), getNumber(ConfigValue.OFFSET_Z)) : location;
        npc.spawn(finalLocation);
        addNPCDefaultTraits(npc);
    }

    public void deleteNPCFromIsland(String uuid){
        // If the island doesn't have any NPC, just stop the code
        if(!BislandNPCs.containsKey(uuid)){
            return;
        }
        // If the island has NPC tracked but the NPC doesn't exist, stop the code
        NPC npc = CitizensAPI.getNPCRegistry().getById(BislandNPCs.get(uuid));
        if(npc == null){
            return;
        }
        npc.destroy();
        BislandNPCs.remove(uuid);
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
        Common.log(ChatColor.RESET, "&aTrying to save all Island NPCs");

        FileConfiguration config = ConfigManager.DATA.getConfig();
        SislandNPCs.forEach((uuid, npcId) -> {
            config.set("data." + uuid.toString(), npcId);
        });
        BislandNPCs.forEach((uuid, npcId) -> {
            config.set("bento." + uuid, npcId);
        });
        ConfigManager.DATA.saveConfig();

        long timePassed = startTime - System.currentTimeMillis();
        Common.log(ChatColor.RESET, "&aSuccessfully saved all Island NPCs (took {ms}ms)"
                .replace("{ms}", timePassed + ""));
    }

    public void loadIslandNPCs(){
        long startTime = System.currentTimeMillis();
        Common.log(ChatColor.RESET, "&aTrying to load all Island NPCs");

        FileConfiguration config = ConfigManager.DATA.getConfig();
        if(config.isConfigurationSection("data")){
            for(String uuid : config.getConfigurationSection("data").getKeys(false)){
                int npcId = config.getInt("data." + uuid);
                SislandNPCs.put(UUID.fromString(uuid), npcId);
            }
        }
        if(config.isConfigurationSection("bento")){
            for(String uuid : config.getConfigurationSection("bento").getKeys(false)){
                int npcId = config.getInt("bento." + uuid);
                BislandNPCs.put(uuid, npcId);
            }
        }

        long timePassed = startTime - System.currentTimeMillis();
        Common.log(ChatColor.RESET, "&aSuccessfully loaded all Island NPCs (took {ms}ms)"
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
        BislandNPCs.forEach((uuid, id) -> {
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
