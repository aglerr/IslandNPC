package me.aglerr.islandnpc.utils;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static boolean hasCustomModelData(){
        return Bukkit.getVersion().contains("1.14") ||
                Bukkit.getVersion().contains("1.15") ||
                Bukkit.getVersion().contains("1.16") ||
                Bukkit.getVersion().contains("1.17");
    }

    public static List<String> parsePAPI(Player player, List<String> text){
        List<String> texts = new ArrayList<>();
        text.forEach(line -> texts.add(parsePAPI(player, line)));
        return texts;
    }

    public static String parsePAPI(Player player, String text){
        if(DependencyHandler.isPlaceholderAPI()){
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }

    public static List<ItemFlag> parseItemFlags(List<String> lines){
        List<ItemFlag> itemFlags = new ArrayList<>();
        for(String line : lines){
            if(!isValidItemFlag(line)){
                continue;
            }
            itemFlags.add(ItemFlag.valueOf(line));
        }
        return itemFlags;
    }

    public static boolean isValidItemFlag(String text){
        try{
            ItemFlag.valueOf(text);
            return true;
        } catch (IllegalArgumentException ex){
            return false;
        }
    }

    public static Island getBentoIsland(Player player){
        User user = BentoBox.getInstance().getPlayersManager().getUser(player.getUniqueId());
        World world = null;

        for(GameModeAddon gameModeAddon : BentoBox.getInstance().getAddonsManager().getGameModeAddons()){
            if(!gameModeAddon.getDescription().getName().equalsIgnoreCase("BSkyblock")) {
                continue;
            }
            world = gameModeAddon.getOverWorld();
        }
        if(world == null){
            return null;
        }
        return BentoBox.getInstance().getIslandsManager().getIsland(world, user);
    }

    public static com.bgsoftware.superiorskyblock.api.island.Island getSuperiorIsland(Player player){
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        if(superiorPlayer.getIsland() == null){
            return null;
        }
        return superiorPlayer.getIsland();
    }

}
