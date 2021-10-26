package me.aglerr.islandnpc.commands;

import me.aglerr.islandnpc.IslandNPC;
import me.aglerr.islandnpc.commands.subcommands.HelpCommand;
import me.aglerr.islandnpc.commands.subcommands.ReloadCommand;
import me.aglerr.islandnpc.commands.subcommands.ResetCommand;
import me.aglerr.islandnpc.config.ConfigValue;
import me.aglerr.lazylibs.libs.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommandMap = new HashMap<>();

    private final IslandNPC plugin;
    public MainCommand(IslandNPC plugin){
        this.plugin = plugin;

        this.subCommandMap.put("help", new HelpCommand());
        this.subCommandMap.put("reload", new ReloadCommand());
        this.subCommandMap.put("reset", new ResetCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if(args.length == 0){
            sendHelpMessages(sender);
            return true;
        }

        SubCommand subCommand = this.subCommandMap.get(args[0].toLowerCase());

        if(subCommand == null){
            sendHelpMessages(sender);
            return true;
        }

        if(subCommand.getPermission() != null){
            if(!(sender.hasPermission(subCommand.getPermission()))){
                sender.sendMessage(Common.color(ConfigValue.NO_PERMISSION_MESSAGE));
                return true;
            }
        }

        subCommand.execute(plugin, sender, args);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if(args.length == 1){
            List<String> suggestions = new ArrayList<>();

            subCommandMap.forEach((name, subcommand) -> {
                if(subcommand.getPermission() == null || sender.hasPermission(subcommand.getPermission())){
                    suggestions.add(subcommand.getName());
                }
            });

            return suggestions;
        }

        if(args.length >= 2){
            SubCommand subCommand = this.subCommandMap.get(args[0].toLowerCase());
            if(subCommand == null){
                return new ArrayList<>();
            }
            if(subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission())){
                return subCommand.parseTabCompletion(plugin, sender, args);
            }
        }

        return new ArrayList<>();
    }

    private void sendHelpMessages(CommandSender sender){
        ConfigValue.HELP_MESSAGES.forEach(message ->
                sender.sendMessage(Common.color(message)));
    }

}
