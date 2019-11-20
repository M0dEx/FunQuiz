package me.m0dex.funquiz.inventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.questions.Question;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MainMenuInventory implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {

        //Borders
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15)));

        //Exit button
        contents.set(2, 8, ClickableItem.of(
                Inventories.createItem(new ItemStack(Material.BARRIER, 1), Messages.INVENTORY_EXIT.getMessage()),
                e -> player.closeInventory()));

        //Question list button
        contents.set(1, 4, ClickableItem.of(
                Inventories.createItem(new ItemStack(Material.SAPLING, 1), Messages.INVENTORY_QUESTION_LIST.getMessage()),
                e -> {
                    Inventories.QuestionList.open(player);
                }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}