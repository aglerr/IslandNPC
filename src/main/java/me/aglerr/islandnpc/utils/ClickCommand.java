package me.aglerr.islandnpc.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ClickCommand {

    private static final Pattern pattern = Pattern.compile("(?<=\\[CONSOLE\\] |\\[PLAYER\\] |\\[MESSAGE\\] )");

    public static void handleClick(List<String> commands, Player player){
        commands.forEach(command -> handleClick(command, player));
    }

    public static void handleClick(String command, Player player){
        // Get the array from the splitted pattern
        String[] cmds = pattern.split(command);
        String tag = cmds[0];
        // Get the list of arguments
        List<String> taskList = new ArrayList<>(Arrays.asList(cmds).subList(1, cmds.length));
        String task = cmds.length > 1 ? String.join(" ", taskList) : "";
        // Get the final arguments with parsed placeholder
        String finalTask = Utils.parsePAPI(player, task
                .replace("{player}", player.getName()));
        // Check if the tag is console
        if(tag.equalsIgnoreCase("[CONSOLE] ")){
            // Execute command in a console
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalTask);
            return;
        }
        // Check if the tag is player
        if(tag.equalsIgnoreCase("[PLAYER] ")){
            // Make the player perform command
            player.performCommand(finalTask);
            return;
        }
        // Check if the tag is message
        if(tag.equalsIgnoreCase("[MESSAGE] ")){
            // Send player a message
            player.sendMessage(Utils.color(finalTask));
        }
    }

}
