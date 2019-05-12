package me.m0dex.funquiz.utils;

import me.m0dex.funquiz.FunQuiz;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public static void executeReward(Player player, String reward) {

		if(player == null || reward.equals(""))
			return;

		String[] args = reward.trim().split(" ");

		if(args.length != 3)
			return;

		if(args[0].equals("give")) {
			Material material = Material.matchMaterial(args[1]);
			int amount = tryParseInt(args[2]);

			if(material == null || amount == 0)
				return;

			ItemStack item = new ItemStack(material, amount);
			give(player, item);
			tell(player, Messages.REWARD_GIVE.getMessage("%item%-" + WordUtils.capitalizeFully(material.toString().toLowerCase().replace("_", " ")) + ";%amount%-" + amount));
		}
	}
}
