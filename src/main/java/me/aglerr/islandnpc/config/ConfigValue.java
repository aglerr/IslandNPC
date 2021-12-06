package me.aglerr.islandnpc.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigValue {

    public static String NPC_ENTITY_TYPE;
    public static String NPC_SKIN;

    public static List<String> HOLOGRAM_LINES;
    public static double HOLOGRAM_HEIGHT;

    public static double OFFSET_X;
    public static double OFFSET_Y;
    public static double OFFSET_Z;

    public static boolean DISABLE_GRAVITY;
    public static boolean LOOK_CLOSE;
    public static boolean ANCHOR;

    public static List<String> CLICK_COMMANDS;

    public static String NO_PERMISSION_MESSAGE;
    public static String RELOAD_MESSAGE;
    public static String INVALID_PLAYER;
    public static String TARGET_NO_ISLAND;
    public static String NPC_RESETTED;
    public static String MOVE_NPC_MESSAGE;

    public static List<String> HELP_MESSAGES;

    public static void initialize(){
        FileConfiguration config = ConfigManager.CONFIG.getConfig();

        NPC_ENTITY_TYPE = config.getString("islandNPC.entityType");
        NPC_SKIN = config.getString("islandNPC.skinName");

        HOLOGRAM_LINES = config.getStringList("islandNPC.hologram.lines");
        HOLOGRAM_HEIGHT = config.getDouble("islandNPC.hologram.height");

        OFFSET_X = config.getDouble("islandNPC.offset.x");
        OFFSET_Y = config.getDouble("islandNPC.offset.y");
        OFFSET_Z = config.getDouble("islandNPC.offset.z");

        DISABLE_GRAVITY = config.getBoolean("npcOptions.disableGravity");
        LOOK_CLOSE = config.getBoolean("npcOptions.lookClose");
        ANCHOR = config.getBoolean("npcOptions.anchor");

        CLICK_COMMANDS = config.getStringList("npcOptions.clickCommands");

        NO_PERMISSION_MESSAGE = config.getString("messages.noPermission");
        RELOAD_MESSAGE = config.getString("messages.reload");
        INVALID_PLAYER = config.getString("messages.invalidPlayer");
        TARGET_NO_ISLAND = config.getString("messages.targetNoIsland");
        NPC_RESETTED = config.getString("messages.npcResetted");
        MOVE_NPC_MESSAGE = config.getString("messages.moveNPCMessage");

        HELP_MESSAGES = config.getStringList("messages.help");
    }

}
