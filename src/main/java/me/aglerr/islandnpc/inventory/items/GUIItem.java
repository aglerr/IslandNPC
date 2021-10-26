package me.aglerr.islandnpc.inventory.items;

import me.aglerr.islandnpc.utils.ClickCommand;
import me.aglerr.islandnpc.utils.Utils;
import me.aglerr.lazylibs.libs.ItemBuilder;
import me.aglerr.lazylibs.libs.XMaterial;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class GUIItem {

    private final String material;
    private final int amount;
    private final String name;
    private final List<Integer> slots;
    private final boolean glowing;
    private final List<String> itemFlags;
    private final List<String> lore;
    private final List<String> leftCommands;
    private final List<String> rightCommands;
    private final int customModelData;

    public GUIItem(String material, int amount, String name, List<Integer> slots, boolean glowing, List<String> itemFlags, List<String> lore, List<String> leftCommands, List<String> rightCommands, int customModelData) {
        this.material = material;
        this.amount = amount;
        this.name = name;
        this.slots = slots;
        this.glowing = glowing;
        this.itemFlags = itemFlags;
        this.lore = lore;
        this.leftCommands = leftCommands;
        this.rightCommands = rightCommands;
        this.customModelData = customModelData;
    }

    public String getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public List<String> getItemFlags() {
        return itemFlags;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<String> getLeftCommands() {
        return leftCommands;
    }

    public List<String> getRightCommands() {
        return rightCommands;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public ItemStack getItemStack(Player player){
        ItemBuilder builder;

        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(material);
        if(!xMaterial.isPresent()){
            builder = new ItemBuilder(Material.BARRIER)
                    .name("&cInvalid Material Name!")
                    .lore("&7Please check your config to fix this!");
            return builder.build();
        }
        builder = new ItemBuilder(xMaterial.get().parseItem())
                .name(Utils.parsePAPI(player, name))
                .lore(Utils.parsePAPI(player, lore))
                .amount(amount == 0 ? 1 : amount)
                .flags(Utils.parseItemFlags(itemFlags));

        if(glowing){
            builder.enchant(Enchantment.ARROW_INFINITE).flags(ItemFlag.HIDE_ENCHANTS);
        }

        if(Utils.hasCustomModelData()){
            builder.customModelData(customModelData);
        }

        return builder.build();
    }

    public void handleLeftClickCommands(Player player){
        ClickCommand.handleClick(leftCommands, player);
    }

    public void handleRightClickCommands(Player player){
        ClickCommand.handleClick(rightCommands, player);
    }

}
