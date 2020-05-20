package me.m0dex.funquiz.utils;

import me.m0dex.funquiz.FunQuiz;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Common {

	static FunQuiz instance = FunQuiz.getInstance();

	/**
	 * Sends a message to a player.
	 *
	 * @param sender	<code>CommandSender</code> command sender
	 * @param message	<code>String</code> message to be sent
	 */
	public static void tell(CommandSender sender, String message) {
		
		sender.sendMessage(applyColours(message));
		
	}

	/**
	 * Sends a message to a player.
	 *
	 * @param sender	<code>CommandSender</code> command sender
	 * @param message	<code>String[]</code> array of messages to be sent
	 */
	public static void tell(CommandSender sender, String[] message) {
		
		sender.sendMessage(applyColours(message));
		
	}

	/**
	 * Sends a message to a player.
	 *
	 * @param sender	<code>CommandSender</code> command sender
	 * @param message	<code>Messages</code> message to be sent
	 */
	public static void tell(CommandSender sender, Messages message) {
		
		sender.sendMessage(applyColours(message.getMessage()));
	}

	/**
	 * Broadcasts a message to all players.
	 *
	 * @param message	<code>String</code> message to be broadcasted
	 */
	public static void broadcast(String message) {
		
		Bukkit.broadcastMessage(applyColours(message));
		
	}

	/**
	 * Broadcasts a message to all players except for those in specified worlds
	 *
	 * @param message
	 * @param disabledWorlds
	 */
	public static void broadcast(String message, List<String> disabledWorlds) {

		message = applyColours(message);

		for(Player player : Bukkit.getOnlinePlayers()) {

			if (!disabledWorlds.contains(player.getWorld().getName().toLowerCase())) {
				player.sendMessage(message);
			}
		}

		instance.getServer().getConsoleSender().sendMessage(message);
	}

	/**
	 * Broadcasts multiple messages to all players.
	 *
	 * @param message	<code>String[]</code> messages to be broadcasted
	 */
	public static void broadcast(String[] message) {
		
		String[] colourizedMsg = applyColours(message);
		
		for(int i = 0; i < colourizedMsg.length; i++)
			if(colourizedMsg[i] != "")
				Bukkit.broadcastMessage(colourizedMsg[i]);
		
	}

	/**
	 * Plays a sound for one player
	 *
	 * @param player
	 * @param sound
	 */
	public static void playSound(Player player, Sound sound) {

		player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
	}

	/**
	 * Plays a sound for all players on the server
	 *
	 * @param sound
	 */
	public static void broadcastSound(Sound sound, List<String> disabledWorlds) {

		if (sound == null)
			return;

		for (Player player : instance.getServer().getOnlinePlayers()) {
			if(!disabledWorlds.contains(player.getWorld().getName().toLowerCase()))
				playSound(player, sound);
		}
	}

	/**
	 * Checks if the command sender has permission and sends them a message if they don't.
	 *
	 * @param sender		<code>CommandSender</code> command sender
	 * @param permission	<code>String</code> permission node
	 * @return				<code>true</code> if the sender has permission;
	 * 						<code>false</code> if they don't.
	 */
	public static boolean hasPermission(CommandSender sender, String permission) {
		
		if(!sender.hasPermission(permission)) {
			tell(sender, Messages.NO_PERMISSION);
			return false;
		}
		
		return true;
	}

	/**
	 * Checks if the command sender has permission and sends them a specified message if they don't.
	 *
	 * @param sender		<code>CommandSender</code> command sender
	 * @param permission	<code>String</code> permission node
	 * @param message		<code>Messages</code> message to be sent
	 * @return              <code>true</code> if the sender has permission;
	 *						<code>false</code> if they don't.
	 */
	public static boolean hasPermission(CommandSender sender, String permission, Messages message) {

		if(!sender.hasPermission(permission)) {
			tell(sender, message);
			return false;
		}

		return true;
	}

	/**
	 * Checks if the specified player is online.
	 *
	 * @param playerName	<code>String</code> Players name
	 * @return				<code>true</code> if the player is online;
	 * 						<code>false</code> if they're not.
	 */
	public static boolean isOnline(String playerName) {
		
		if(instance.getServer().getPlayer(playerName) != null)
			return true;
		else
			return false;
		
	}

	/**
	 * Applies specified colour codes to the message.
	 *
	 * @param message 	<code>String</code> message to be colourized
	 * @return			<code>String</code> message with applied colour codes;
	 * 					<code>String</code> empty string.
	 */
	public static String applyColours(String message) {	
		
		if(!message.isEmpty())
			return ChatColor.translateAlternateColorCodes('&', message);
		else
			return "";
		
	}

	/**
	 * Applies specified colour codes to the messages.
	 *
	 * @param message	<code>String[]</code> messages to be colourized
	 * @return			<code>String[]</code> messages with applied colour codes.
	 */
	public static String[] applyColours(String[] message) {
		
		String[] output = new String[message.length];
		
		for(int i = 0; i < message.length; i++) {
			
			if(!message[i].isEmpty())
				output[i] = ChatColor.translateAlternateColorCodes('&', message[i]);
			else
				output[i] = "";
				
		}
		
		return output;
	}

	/**
	 * Strips colours from a message.
	 *
	 * @param message	<code>String</code> message to be decolourized
	 * @return			<code>String</code> decolourized message;
	 * 					<code>String</code> empty String.
	 */
	public static String stripColours(String message) {
		
		if(!message.isEmpty())
			return ChatColor.stripColor(message);
		else
			return "";
		
	}

	/**
	 * Strips colours from all messages.
	 *
	 * @param message	<code>String[]</code> messages to be decolourized
	 * @return			<code>String[]</code> array of decolourized messages.
	 */
	public static String[] stripColours(String[] message) {
		
		String[] output = new String[message.length];
		
		for(int i = 0; i < message.length; i++) {
			
			if(!message[i].isEmpty())
				output[i] = ChatColor.stripColor(message[i]);
			else
				output[i] = "";
			
		}
		
		return output;
	}

	/**
	 * Tries to parse <code>String</code> as <code>Integer</code>.
	 *
	 * @param text	<code>String</code> text to be converted into Integer
	 * @return		<code>int</code> the text as a number or 0 if and error happened.
	 */
	public static int tryParseInt(String text) {

		int output = 0;
		
		try {
			output = Integer.parseInt(text);
		} catch(Exception ex) {
			instance.getLogger().severe("Failed to parse string -> int: " + ex.getMessage());
		}
		
		return output;
	}

	/**
	 * Gives the specified player the specified item. Drops the item on the ground if their inventory is full.
	 *
	 * @param player	<code>Player</code> player
	 * @param item		<code>ItemStack</code> item to be given to the player
	 */
	public static void give(Player player, ItemStack item) {

        if(player.getInventory().firstEmpty() == -1)
            player.getWorld().dropItem(player.getLocation(), item);
        else
            player.getInventory().addItem(item);
    }

	/**
	 * Parses the reward
	 * @param player Player to reward
	 * @param reward Text containing the reward
	 */
	public static void executeReward(Player player, String reward) {

		if(player == null || reward.equals(""))
			return;

		String[] args = reward.trim().split(" ");

		if(args[0].equalsIgnoreCase("give")) {
			Material material = Material.matchMaterial(args[1].toLowerCase());
			int amount = tryParseInt(args[2]);

			if(material == null || amount == 0)
				return;

			ItemStack item = new ItemStack(material, amount);
			give(player, item);
			tell(player, Messages.REWARD_GIVE.getMessage("%item%-" + WordUtils.capitalizeFully(material.toString().toLowerCase().replace("_", " ")) + ";%amount%-" + amount));
		} else if (args[0].equalsIgnoreCase("money")){

			if (instance.getEconomy() == null) {
				instance.getLogger().severe("Couldn't execute reward '" + reward + "' - Not hooked into Vault economy!");
				return;
			}

			int amount = tryParseInt(args[1]);

			if (amount >= 0)
				instance.getEconomy().depositPlayer(player, amount);
			else
				instance.getEconomy().withdrawPlayer(player, amount);

			tell(player, Messages.REWARD_MONEY.getMessage("%amount%-" + amount));
		} else if (args[0].equalsIgnoreCase("custom")) {

			ConsoleCommandSender console = instance.getServer().getConsoleSender();

			String command = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace("%player%", player.getName());

			if (instance.getSettings().debug)
				instance.getLogger().info("Executing custom reward command \"" + command + "\" for player " + player.getName());

			instance.getServer().dispatchCommand(console, command);
		}
	}
}
