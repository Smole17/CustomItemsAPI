package ru.smole.utils;

import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSUtils {
    public static ItemStack setTag(ItemStack itemStack, String tag, NBTBase value) {
        net.minecraft.server.v1_12_R1.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsTag = nms.getTag();
        if (nmsTag == null)
            nmsTag = new NBTTagCompound();
        nmsTag.set(tag, value);
        nms.setTag(nmsTag);
        return new ItemStack(CraftItemStack.asBukkitCopy(nms));
    }

    public static <T extends NBTBase> T getTag(ItemStack itemStack, String tag) {
        net.minecraft.server.v1_12_R1.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsTag = nms.getTag();
        if (nmsTag == null)
            nmsTag = new NBTTagCompound();
        return nmsTag.get(tag) == null ? null : (T) nmsTag.get(tag);
    }
}
