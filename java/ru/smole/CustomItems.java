package ru.smole;

import ru.smole.utils.NMSUtils;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.smole.item.CustomItemManager;
import ru.smole.item.CustomItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CustomItems {

    private final Map<String, CustomItem> customItemMap = new HashMap<>();
    private String customItemName;

    protected static CustomItemHandlers handlers;

    // Main methods

    public static CustomItems getCustomItems(Plugin plugin) {
        CustomItems customItems = new CustomItems();
        handlers = new CustomItemHandlers(customItems);

        Bukkit.getPluginManager().registerEvents(handlers, plugin);

        return customItems;
    }

    public CustomItems registerItem(String itemName, ItemStack itemStack) {
        customItemMap.put(itemName, new CustomItemManager(itemName, itemStack));
        customItemName = itemName;

        return this;
    }

    public CustomItems handleEventInteract(Consumer<PlayerInteractEvent> consumer) {
        checkErrors();

        handlers.interactMap.put(customItemName.toLowerCase(), consumer);

        return this;
    }

    public CustomItems handleEventConsume(Consumer<PlayerItemConsumeEvent> consumer) {
        checkErrors();

        handlers.consumeMap.put(customItemName.toLowerCase(), consumer);

        return this;
    }

    public CustomItems handleEventSplash(Consumer<PotionSplashEvent> consumer) {
        checkErrors();

        handlers.splashMap.put(customItemName.toLowerCase(), consumer);

        return this;
    }

    public CustomItem build() {
        return customItemMap.getOrDefault(customItemName, null);
    }

    // Other methods

    public Map<String, CustomItem> getCustomItemsMap() {
        return customItemMap;
    }

    protected String getItemNameForTag(ItemStack item) {
        if (item != null) {
            NBTTagString nbt = NMSUtils.getTag(item, "smole_item");
            String name;
            if (nbt != null && (name = nbt.c_()) != null) {
                if (customItemMap.containsKey(name.toLowerCase()))
                    return name;
            }
        }
        return null;
    }

    // Helped methods

    protected void checkErrors() {
        if (customItemName == null)
            throw new IllegalArgumentException("Could not handle event bcs itemName == null");

        if (customItemMap.isEmpty() | !customItemMap.containsKey(customItemName))
            throw new IllegalArgumentException("Could not handle event bcs CustomItem is not exists");
    }

    private static class CustomItemHandlers implements Listener {

        private final Map<String, Consumer<PlayerInteractEvent>> interactMap;
        private final Map<String, Consumer<PlayerItemConsumeEvent>> consumeMap;
        private final Map<String, Consumer<PotionSplashEvent>> splashMap;

        private final CustomItems customItems;

        private CustomItemHandlers(CustomItems customItems) {
            this.customItems = customItems;

            interactMap = new HashMap<>();
            consumeMap = new HashMap<>();
            splashMap = new HashMap<>();
        }

        @EventHandler
        public void onInteract(PlayerInteractEvent event) {
            ItemStack itemStack = event.getItem();

            if (itemStack == null)
                return;

            String itemName = customItems.getItemNameForTag(itemStack);

            if (itemName == null)
                return;

            Consumer<PlayerInteractEvent> interact = interactMap.get(itemName);

            if (interact == null)
                return;

            interact.accept(event);
        }

        @EventHandler
        public void onConsume(PlayerItemConsumeEvent event) {
            ItemStack itemStack = event.getItem();

            if (itemStack == null)
                return;

            String itemName = customItems.getItemNameForTag(itemStack);

            if (itemName == null)
                return;

            Consumer<PlayerItemConsumeEvent> consume = consumeMap.get(itemName);

            if (consume == null)
                return;

            consume.accept(event);
        }

        @EventHandler
        public void onSplash(PotionSplashEvent event) {
            ItemStack itemStack = event.getPotion().getItem();

            if (itemStack == null)
                return;

            String itemName = customItems.getItemNameForTag(itemStack);

            if (itemName == null)
                return;

            Consumer<PotionSplashEvent> splash = splashMap.get(itemName);

            if (splash == null)
                return;

            splash.accept(event);
        }
    }
}
