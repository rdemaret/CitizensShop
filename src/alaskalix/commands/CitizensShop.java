package alaskalix.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import alaskalix.main.Main;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class CitizensShop implements CommandExecutor {

	private Main plugin;

	public CitizensShop(Main main) {
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String message, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(ChatColor.RED + "/citizensShop add <case> <prix>\n/citizensShop remove <case>");
				return false;
			} else {
				switch (args[0]) {
				case "add":
					return commandAdd(sender, args, player);

				case "remove":
					return commandRemove(sender, args, player);

				default:
					player.sendMessage(ChatColor.RED + "/citizensShop add <case> <prix>\n/citizensShop remove <case>");
					return false;
				}
			}
		} else {
			return false;
		}
	}

	private boolean commandRemove(CommandSender sender, String[] args, Player player) {
		if (validCommandRemove(args)) {
			int id = getNPCId(sender);
			if (containConfig(id)) {

				plugin.getConfig().getConfigurationSection(Integer.toString(id)).set(args[1], null);
				plugin.saveConfig();
				player.sendMessage("§2Item supprimé !");
				return true;
			} else {
				player.sendMessage("§ePas d'item a cet endroit ou NPC non valide !");
				return false;
			}
		} else {
			player.sendMessage(ChatColor.RED + "/citizensShop remove <case[0-26]>");
			return false;
		}
	}

	private boolean commandAdd(CommandSender sender, String[] args, Player player) {
		if (validCommandAdd(args)) {
			ItemStack item = player.getInventory().getItemInMainHand();
			if (item.getType() == Material.AIR) {
				player.sendMessage("§ePas d'item selectionné !");
				return false;
			}
			int id = getNPCId(sender);

			if (containConfig(id)) {

				return addItem(args, player, item, id);
			} else {
				player.sendMessage("§ePas de vendeur valide selectionné !");
				return false;

			}
		} else {
			player.sendMessage(ChatColor.RED + "/citizensShop add <case[0-26]> <prix>");
			return false;
		}
	}

	private boolean addItem(String[] args, Player player, ItemStack item, int id) {
		ItemMeta meta = item.getItemMeta();
		ItemStack copyItem = item.clone();
		ArrayList<String> list = new ArrayList<String>();
		list.add("prix : " + args[2] + "$");
		meta.setLore(list);

		copyItem.setItemMeta(meta);

		plugin.getConfig().getConfigurationSection(Integer.toString(id)).set(args[1], copyItem);
		plugin.saveConfig();
		player.sendMessage("§2Item ajouté !");
		return true;
	}

	private int getNPCId(CommandSender sender) {
		NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
		if (npc == null) {
			return -1;
		} else {
			int id = npc.getId();
			return id;
		}

	}

	private boolean containConfig(int id) {
		return id != -1 && plugin.getConfig().isConfigurationSection(Integer.toString(id));
	}

	private boolean validCommandAdd(String[] args) {
		return args.length == 3 && isInteger(args[1]) && isInteger(args[2]) && Integer.parseInt(args[1]) >= 0
				&& Integer.parseInt(args[1]) <= 26 && Integer.parseInt(args[2]) > 0;
	}

	private boolean validCommandRemove(String[] args) {
		return args.length == 2 && isInteger(args[1]) && Integer.parseInt(args[1]) >= 0
				&& Integer.parseInt(args[1]) <= 26;
	}

	public boolean isInteger(String chaine) {
		try {
			Integer.parseInt(chaine);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}
}
