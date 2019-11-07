package me.m0dex.funquiz.inventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QuestionListInventory implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {

        //Borders
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15)));

        //Random question
        contents.set(0, 4, ClickableItem.of(
                Inventories.createItem(new ItemStack(Material.REDSTONE, 1), Messages.INVENTORY_ASK_RANDOM.getMessage()),
                e -> player.performCommand("/questions ask")));

        //Back button
        contents.set(5, 4, ClickableItem.of(
                Inventories.createItem(new ItemStack(Material.BARRIER, 1), Messages.INVENTORY_BACK.getMessage()),
                e -> {
                    player.closeInventory();
                    Inventories.MainMenu.open(player);
                }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
