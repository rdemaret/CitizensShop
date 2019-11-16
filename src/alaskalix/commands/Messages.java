package alaskalix.commands;

import org.bukkit.ChatColor;

public class Messages {

	public static final String CREATE_PATTERN = ChatColor.RED + "/createShop <name>";
	public static final String ADD_PATTERN = ChatColor.RED + "/ctzShop add <case[0-26]> <buyPrice> <sellPrice>";
	public static final String REMOVE_PATTERN = ChatColor.RED + "/ctzShop remove <case[0-26]:all>";

	public static final String NO_ITEM_SELECTED = ChatColor.RED + "Pas d'item selectionné !";
	public static final String INVALID_ACTION = ChatColor.RED + "Action invalide !";
	
	public static final String SUCCESSFULL_OPERATION = ChatColor.GREEN + "Opération réussie !";
	
	public static String formatBuyLure(String price) {
		return ""+ChatColor.GREEN + ChatColor.BOLD + "Achat : " + price + "$";
	}
	
	public static String formatSellLure(String price) {
		return ""+ChatColor.RED + ChatColor.BOLD + "Vente : " + price + "$";
	}
	
	public static String formatBuyAccept(String price,double balance) {
		return ""+ChatColor.GREEN + ChatColor.BOLD + "Vous avez acheté pour "+ ChatColor.YELLOW + price + "$"
				+ChatColor.GREEN +", il vous reste : "+ ChatColor.YELLOW + balance + "$";
	}
	
	public static String formatBuyRefused(double balance) {
		return ""+ChatColor.RED + ChatColor.BOLD + "Vous n'avez pas assez d'argent, votre solde : "+ ChatColor.YELLOW + balance + "$";
	}
	
	public static String formatSellAccept(String price, double balance) {
		return ""+ChatColor.GREEN + ChatColor.BOLD + "Vous avez vendu pour "+ ChatColor.YELLOW + price + "$"
				+ChatColor.GREEN +", il vous reste : "+ ChatColor.YELLOW + balance + "$";
	}

	public static String formatSellRefused(double balance) {
		return ""+ChatColor.RED + ChatColor.BOLD + "Vous n'avez pas cet item en votre possession";
	}
	
	public static String multipleLineMessage(String... lines) {
		String message = "";
		for(String line : lines) {
			message += line + "\n";
		}
		return ChatColor.RED + message;
	}




}
