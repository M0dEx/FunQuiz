package me.m0dex.funquiz.utils;

import me.m0dex.funquiz.FunQuiz;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Common {

	static FunQuiz instance = FunQuiz.getInstance();
	
	public static void tell(CommandSender sender, String message) {
		
		sender.sendMessage(applyColours(message));
		
	}
	
	public static void tell(CommandSender sender, String[] message) {
		
		sender.sendMessage(applyColours(message));
		
	}
	
	public static void tell(CommandSender sender, Messages message) {
		
		sender.sendMessage(applyColours(message.getMessage()));
	}
	
	public static void broadcast(String message) {
		
		Bukkit.broadcastMessage(applyColours(message));
		
	}
	
	public static void broadcast(String[] message) {
		
		String[] colourizedMsg = applyColours(message);
		
		for(int i = 0; i < colourizedMsg.length; i++)
			if(colourizedMsg[i] != "")
				Bukkit.broadcastMessage(colourizedMsg[i]);
		
	}
	
	public static boolean hasPermission(CommandSender sender, String permission) {
		
		if(!sender.hasPermission(permission)) {
			tell(sender, Messages.NO_PERMISSION);
			return false;
		}
		
		return true;
	}

	public static boolean hasPermission(CommandSender sender, String permission, Messages message) {

		if(!sender.hasPermission(permission)) {
			tell(sender, message);
			return false;
		}

		return true;
	}
	
	public static boolean isOnline(String playerName) {
		
		if(instance.getServer().getPlayer(playerName) != null)
			return true;
		else
			return false;
		
	}
	
	public static String applyColours(String message) {	
		
		if(!message.isEmpty())
			return ChatColor.translateAlternateColorCodes('&', message);
		else
			return "";
		
	}
	
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
	
	public static String stripColours(String message) {
		
		if(!message.isEmpty())
			return ChatColor.stripColor(message);
		else
			return "";
		
	}
	
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
	
	public static int tryParseInt(String text) {

		int output = 0;
		
		try {
			output = Integer.parseInt(text);
		} catch(Exception ex) {
			instance.getLogger().severe("Failed to parse string -> int: " + ex.getMessage());
		}
		
		return output;
	}

	public static void give(Player player, ItemStack item) {

        if(player.getInventory().firstEmpty() == -1)
            player.getWorld().dropItem(player.getLocation(), item);
        else
            player.getInventory().addItem(item);
    }
}
