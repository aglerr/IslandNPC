package me.aglerr.islandnpc.commands;

import me.aglerr.islandnpc.IslandNPC;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SubCommand {

    @NotNull
    public abstract String getName();

    @Nullable
    public abstract String getPermission();

    @Nullable
    public abstract List<String> parseTabCompletion(IslandNPC plugin, CommandSender sender, String[] args);

    public abstract void execute(IslandNPC plugin, CommandSender sender, String[] args);

}
