package me.aglerr.islandnpc.commands.subcommands;

import me.aglerr.islandnpc.IslandNPC;
import me.aglerr.islandnpc.commands.SubCommand;
import me.aglerr.islandnpc.config.ConfigValue;
import me.aglerr.islandnpc.utils.Utils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends SubCommand {

    @Override
    public @NotNull String getName() {
        return "reload";
    }

    @Override
    public @Nullable String getPermission() {
        return "islandnpc.admin";
    }

    @Override
    public @NotNull List<String> parseTabCompletion(IslandNPC plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(IslandNPC plugin, CommandSender sender, String[] args) {
        plugin.reloadEverything();
        sender.sendMessage(Utils.color(ConfigValue.RELOAD_MESSAGE));
    }

}
