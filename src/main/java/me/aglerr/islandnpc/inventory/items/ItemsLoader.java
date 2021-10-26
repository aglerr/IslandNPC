package me.aglerr.islandnpc.inventory.items;

import me.aglerr.islandnpc.config.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ItemsLoader {

    private final List<GUIItem> inventoryItems = new ArrayList<>();

    public void loadItems(){
        FileConfiguration config = ConfigManager.INVENTORY.getConfig();
        // Clear the list first so it can be used for reload too.
        inventoryItems.clear();
        // If there is no items, just skip.
        if(!config.isConfigurationSection("items")){
            return;
        }
        // Then load all of the items.
        for(String configKey : config.getConfigurationSection("items").getKeys(false)){
            String path = "items." + configKey;
            String material = config.getString(path + ".material");
            int amount = config.getInt(path + ".amount");
            String name = config.getString(path + ".name");
            List<Integer> slots = config.getIntegerList(path + ".slots");
            boolean glowing = config.getBoolean(path + ".glowing");
            List<String> itemFlags = config.getStringList(path + ".itemFlags");
            List<String> lore = config.getStringList(path + ".lore");
            List<String> leftCommands = config.getStringList(path + ".leftClickCommands");
            List<String> rightCommands = config.getStringList(path + ".rightClickCommands");
            int customModelData = config.getInt(path + ".customModelData");
            // Finally add the item to the list
            GUIItem guiItem = new GUIItem(material, amount, name, slots, glowing, itemFlags, lore, leftCommands,
                    rightCommands, customModelData);
            this.inventoryItems.add(guiItem);
        }
    }

    public List<GUIItem> getInventoryItems(){
        return inventoryItems;
    }

}
