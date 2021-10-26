package me.aglerr.islandnpc.inventory;

import com.bgsoftware.superiorskyblock.api.island.Island;
import com.google.common.primitives.Ints;
import de.tr7zw.nbtapi.NBTItem;
import me.aglerr.islandnpc.IslandNPC;
import me.aglerr.islandnpc.config.ConfigManager;
import me.aglerr.islandnpc.config.ConfigValue;
import me.aglerr.islandnpc.inventory.items.GUIItem;
import me.aglerr.islandnpc.inventory.libs.LazyInventory;
import me.aglerr.islandnpc.utils.Utils;
import me.aglerr.lazylibs.libs.Common;
import me.aglerr.lazylibs.libs.Executor;
import me.aglerr.lazylibs.libs.ItemBuilder;
import me.aglerr.lazylibs.libs.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Optional;

public class NPCInventory extends LazyInventory {

    private final IslandNPC plugin;

    public NPCInventory(IslandNPC plugin, Island island, Player player) {
        super(ConfigValue.INVENTORY_SiZE, Common.color(ConfigValue.INVENTORY_TITLE));
        this.plugin = plugin;

        this.setAllItems(player);

        BukkitTask task = Executor.syncTimer(0L, 20L, () ->
                this.setAllItems(player));

        this.addCloseHandler(event -> task.cancel());
    }

    public NPCInventory(IslandNPC plugin, world.bentobox.bentobox.database.objects.Island island, Player player){
        super(ConfigValue.INVENTORY_SiZE, Common.color(ConfigValue.INVENTORY_TITLE));
        this.plugin = plugin;

        setAllItems(player);

        BukkitTask task = Executor.syncTimer(0L, 20L, () ->
                this.setAllItems(player));

        this.addCloseHandler(event -> task.cancel());
    }

    private void setAllItems(Player player){
        for(GUIItem guiItem : plugin.getItemsLoader().getInventoryItems()){
            this.setItems(Ints.toArray(guiItem.getSlots()), guiItem.getItemStack(player), event -> {
                if(event.getClick() == ClickType.LEFT){
                    guiItem.handleLeftClickCommands(player);
                }
                if(event.getClick() == ClickType.RIGHT){
                    guiItem.handleRightClickCommands(player);
                }
            });
        }
        // Place the Move NPC item if enabled.
        FileConfiguration config = ConfigManager.INVENTORY.getConfig();
        if(config.getBoolean("moveNPC.enabled")){
            // Get the slot
            List<Integer> slots = config.getIntegerList("moveNPC.gui.slots");
            // And place the item
            this.setItems(Ints.toArray(slots), getBarryGUI(), event -> {
                player.closeInventory();
                player.sendMessage(Common.color(ConfigValue.MOVE_NPC_MESSAGE));
                if(player.getInventory().contains(getBarryItems())){
                    return;
                }
                player.getInventory().addItem(getBarryItems());
            });
        }
    }

    private ItemStack getBarryGUI(){
        FileConfiguration config = ConfigManager.INVENTORY.getConfig();

        String path = "moveNPC.gui.";
        String material = config.getString(path + "material");
        String name = config.getString(path + "name");
        int customModelData = config.getInt(path + "customModelData");
        boolean glowing = config.getBoolean(path + "glowing");
        List<String> lore = config.getStringList(path + "lore");

        ItemBuilder builder;

        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(material);
        if(!xMaterial.isPresent()){
            builder = new ItemBuilder(Material.BARRIER)
                    .name("&cInvalid Material Name!")
                    .lore("&7Please check your config to fix this!");
            return builder.build();
        }
        builder = new ItemBuilder(xMaterial.get().parseItem())
                .name(name)
                .lore(lore)
                .amount(1);

        if(glowing){
            builder.enchant(Enchantment.ARROW_INFINITE).flags(ItemFlag.HIDE_ENCHANTS);
        }

        if(Utils.hasCustomModelData()){
            builder.customModelData(customModelData);
        }

        return builder.build();
    }

    private ItemStack getBarryItems(){
        FileConfiguration config = ConfigManager.INVENTORY.getConfig();

        String path = "moveNPC.items.";
        String material = config.getString(path + "material");
        String name = config.getString(path + "name");
        int customModelData = config.getInt(path + "customModelData");
        boolean glowing = config.getBoolean(path + "glowing");
        List<String> lore = config.getStringList(path + "lore");

        ItemBuilder builder;

        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(material);
        if(!xMaterial.isPresent()){
            builder = new ItemBuilder(Material.BARRIER)
                    .name("&cInvalid Material Name!")
                    .lore("&7Please check your config to fix this!");
            return builder.build();
        }
        builder = new ItemBuilder(xMaterial.get().parseItem())
                .name(name)
                .lore(lore)
                .amount(1);

        if(glowing){
            builder.enchant(Enchantment.ARROW_INFINITE).flags(ItemFlag.HIDE_ENCHANTS);
        }

        if(Utils.hasCustomModelData()){
            builder.customModelData(customModelData);
        }

        NBTItem nbtItem = new NBTItem(builder.build());
        nbtItem.setBoolean("moveNPC", true);

        return nbtItem.getItem();
    }

}
