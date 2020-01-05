package me.m0dex.funquiz.inventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.questions.Question;
import me.m0dex.funquiz.utils.Common;
import me.m0dex.funquiz.utils.Messages;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionListInventory implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {

        Pagination pagination = contents.pagination();

        //Borders
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15)));

        //Random question
        contents.set(0, 4, ClickableItem.of(
                Inventories.createItem(new ItemStack(Material.REDSTONE, 1), Messages.INVENTORY_ASK_RANDOM.getMessage()),
                e -> player.performCommand("questions ask")));

        //Back button
        contents.set(5, 4, ClickableItem.of(
                Inventories.createItem(new ItemStack(Material.BARRIER, 1), Messages.INVENTORY_BACK.getMessage()),
                e -> {
                    Inventories.MainMenu.open(player);
                }));

        //Previous page
        contents.set(5, 2, ClickableItem.of(
                Inventories.createItem(new ItemStack(Material.ARROW, 1), Messages.INVENTORY_PREVIOUS.getMessage()),
                e -> Inventories.QuestionList.open(player, pagination.previous().getPage())
        ));

        //Next page
        contents.set(5, 6, ClickableItem.of(
                Inventories.createItem(new ItemStack(Material.ARROW, 1), Messages.INVENTORY_NEXT.getMessage()),
                e -> Inventories.QuestionList.open(player, pagination.next().getPage())
        ));



        List<Question> questions = FunQuiz.getInstance().getQuestionManager().getQuestions();
        ClickableItem[] questionItems = new ClickableItem[questions.size()];

        for(int i = 0; i < questions.size(); i++) {

            Question question = questions.get(i);
            ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();

            meta.setDisplayName(Common.applyColours(Messages.INVENTORY_QUESTION_NAME.getMessage("%name%-" + question.getName())));

            for(String part : WordUtils.wrap(Messages.INVENTORY_QUESTION_QUESTION.getMessage("%question%-" + question.getQuestion()), 50, ";", false).split(";"))
                lore.add(Common.applyColours((part.contains("&e") ? part : "&e" + part)));


            lore.add(Common.applyColours(Messages.INVENTORY_QUESTION_ANSWERS.getMessage("%answers%-" + question.getAnswers())));
            lore.add(Common.applyColours(Messages.INVENTORY_QUESTION_REWARDS.getMessage("%rewards%-" + question.getRewards())));

            meta.setLore(lore);

            item.setItemMeta(meta);

            questionItems[i] = ClickableItem.of(item, e -> {
                player.performCommand("questions ask " + question.getName());
                player.closeInventory();
            });
        }

        pagination.setItems(questionItems);
        pagination.setItemsPerPage(28);

        //Settings for the iterator + setting the bounds of the iterator
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1)
                .blacklist(1, 0)
                .blacklist(2, 0)
                .blacklist(3, 0)
                .blacklist(4, 0)
                .blacklist(1, 8)
                .blacklist(2, 8)
                .blacklist(3, 8)
                .blacklist(4, 8));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
