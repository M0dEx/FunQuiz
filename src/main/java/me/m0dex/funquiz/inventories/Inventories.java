package me.m0dex.funquiz.inventories;

import fr.minuskube.inv.SmartInventory;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Inventories {

    public static final SmartInventory MainMenu = SmartInventory.builder()
            .id("mainMenu")
            .provider(new MainMenuInventory())
            .size(3, 9)
            .title(Common.applyColours(Messages.INVENTORY_MAIN_MENU_TITLE.getMessage()))
            .build();

    public static final SmartInventory QuestionList = SmartInventory.builder()
            .id("questionList")
            .provider(new QuestionListInventory())
            .size(6, 9)
            .title(Common.applyColours(Messages.INVENTORY_QUESTION_LIST_TITLE.getMessage()))
            .build();

    public static ItemStack createItem(ItemStack item, String title) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Common.applyColours(title));
        item.setItemMeta(meta);

        return item;
    }
}
