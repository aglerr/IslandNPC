package me.aglerr.islandnpc.utils;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.aglerr.islandnpc.config.ConfigManager;
import me.aglerr.mclibs.libs.ItemBuilder;
import me.aglerr.mclibs.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class Item {

    public static ItemStack MOVE_ITEM;

    public static void init(){
        FileConfiguration config = ConfigManager.CONFIG.getConfig();

        String material = config.getString("moveNPCItem.material");
        String name = config.getString("moveNPCItem.name");
        int customModelData = config.getInt("moveNPCItem.customModelData");
        boolean glowing = config.getBoolean("moveNPCItem.glowing");
        List<String> lore = config.getStringList("moveNPCItem.lore");

        ItemBuilder builder;

        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(material);
        if(!xMaterial.isPresent()){
            builder = new ItemBuilder(Material.BARRIER)
                    .name("&cInvalid Material Name!")
                    .lore("&7Please check your config to fix this!");
            MOVE_ITEM = builder.build();
            return;
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
        MOVE_ITEM = nbtItem.getItem();
    }

}
