package me.aglerr.islandnpc.utils;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static boolean hasCustomModelData(){
        return Bukkit.getVersion().contains("1.14") ||
                Bukkit.getVersion().contains("1.15") ||
                Bukkit.getVersion().contains("1.16") ||
                Bukkit.getVersion().contains("1.17") ||
                Bukkit.getVersion().contains("1.18");
    }

    public static List<String> color(List<String> s){
        return s.stream().map(Utils::color).collect(Collectors.toList());
    }

    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> parsePAPI(Player player, List<String> texts){
        return texts.stream().map(text -> parsePAPI(player, text)).collect(Collectors.toList());
    }

    public static String parsePAPI(Player player, String text){
        if(DependencyHandler.isPlaceholderAPI()){
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }

    public static Island getSuperiorIsland(Player player){
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        if(superiorPlayer.getIsland() == null){
            return null;
        }
        return superiorPlayer.getIsland();
    }

    public static void log(String... messages){
        for (String message : messages) {
            Bukkit.getConsoleSender().sendMessage("[IslandNPC] " + color(message));
        }
    }

    public static void sendMessage(CommandSender sender, String message){
        sender.sendMessage(color(message));
    }

}
