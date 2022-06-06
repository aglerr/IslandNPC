package me.aglerr.islandnpc.commands.subcommands;

import me.aglerr.islandnpc.IslandNPC;
import me.aglerr.islandnpc.commands.SubCommand;
import me.aglerr.islandnpc.config.ConfigValue;
import me.aglerr.islandnpc.utils.Utils;
import me.aglerr.mclibs.libs.Common;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends SubCommand {
    @Override
    public @NotNull String getName() {
        return "help";
    }

    @Override
    public @Nullable String getPermission() {
        return "islandnpc.help";
    }

    @Override
    public @NotNull List<String> parseTabCompletion(IslandNPC plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(IslandNPC plugin, CommandSender sender, String[] args) {
        ConfigValue.HELP_MESSAGES.forEach(message ->
                Common.sendMessage(sender, message));
    }
}
