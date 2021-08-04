package ru.smole.item;

import ru.smole.utils.NMSUtils;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.inventory.ItemStack;

public class CustomItemManager implements CustomItem {
    private final String itemName;
    private final ItemStack itemStack;

    public CustomItemManager(String itemName, ItemStack itemStack) {
        this.itemName = itemName;
        this.itemStack = itemStack;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public ItemStack getItemStack() {
        return NMSUtils.setTag(itemStack, "smole_item", new NBTTagString(itemName.toLowerCase()));
    }
}
