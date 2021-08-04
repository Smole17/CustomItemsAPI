package ru.smole.example;

import ru.smole.CustomItems;
import ru.smole.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Test extends JavaPlugin implements CommandExecutor {

    private CustomItem customItem;

    @Override
    public void onEnable() {
        CustomItems customItems = CustomItems.getCustomItems(this);

        customItem =
                customItems
                        .registerItem("test", new ItemStack(Material.DIAMOND))
                        .handleEventInteract(event ->
                                event.getPlayer().sendTitle("work!", null, 20, 20, 20))
                        .build();

        getCommand("test").setExecutor(this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;
        ItemStack itemStack = customItem.getItemStack();

        player.getInventory().addItem(itemStack);

        return true;
    }
}
