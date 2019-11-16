package alaskalix.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
			
			if(validCommandAdd(args))return commandAdd(sender, args, player);
			if(validCommandRemove(args))return commandRemove(sender, args, player);
			
			notify(Messages.multipleLineMessage(Messages.ADD_PATTERN,Messages.REMOVE_PATTERN),player);
			return false;
		}
		return false;
	}

	private boolean commandRemove(CommandSender sender, String[] args, Player player) {
		if (validCommandRemove(args)) {
			int npcId = getNPCId(sender);
			
			if(args[1].equals("all"))return removeEntryFromConfig("",npcId,player);
			return removeEntryFromConfig(args[1],npcId,player);

		} else {
			notify(Messages.REMOVE_PATTERN,player);
			return false;
		}
	}

	private boolean commandAdd(CommandSender sender, String[] args, Player player) {
		
		ItemStack item = player.getInventory().getItemInMainHand();
		int id = getNPCId(sender);
		
		if (item.getType() == Material.AIR) {
			notify(Messages.NO_ITEM_SELECTED,player);
			return false;
		}
		
		if (containConfig(id))return addItem(args, player, item, id);
		
		notify(Messages.INVALID_ACTION,player);
		return false;

	}

	private boolean addItem(String[] args, Player player, ItemStack item, int id) {
		ItemMeta meta = item.getItemMeta();
		ItemStack copyItem = item.clone();
		List<String> list = new ArrayList<String>();
		list.add(Messages.formatBuyLure(args[2]));
		list.add(Messages.formatSellLure(args[3]));
		meta.setLore(list);

		copyItem.setItemMeta(meta);

		getConfigFromId(id).set(args[1], copyItem);
		saveAndNotify(Messages.SUCCESSFULL_OPERATION,player);
		return true;
	}
	
	private NPC getNPC(CommandSender sender) {
		return CitizensAPI.getDefaultNPCSelector().getSelected(sender);
	}

	private int getNPCId(CommandSender sender) {
		NPC npc = getNPC(sender);
		if (npc == null) return -1; 
		return npc.getId();
	}
	
	private boolean removeEntryFromConfig(String entry, int npcId, Player player) {
		if(containConfig(npcId)) {
			ConfigurationSection cs = getConfigFromId(npcId);
			
			//All
			if(entry.isEmpty()) {
				for(int i = 0; i<27; i++) {
					String temp = Integer.toString(i);
					if(cs.contains(temp)) {
						cs.set(temp, null);
						saveAndNotify(Messages.SUCCESSFULL_OPERATION,player);
						return true;
					}
				}
			}
			
			//Specified index exist in config
			if(cs.contains(entry)) {
				cs.set(entry, null);
				saveAndNotify(Messages.SUCCESSFULL_OPERATION,player);
				return true;
			}
			
			//Specified index doesn't exist in config
			saveAndNotify(Messages.INVALID_ACTION,player);
			return false;
			
			
		}else {
			saveAndNotify(Messages.INVALID_ACTION,player);
			return false;
		}
	}

	private ConfigurationSection getConfigFromId(int npcId) {
		return plugin.getConfig().getConfigurationSection(Integer.toString(npcId));
	}

	private void saveAndNotify(String message, Player player) {
		plugin.saveConfig();
		notify(message,player);
	}
	
	private void notify(String message, Player player) {
		player.sendMessage(message);
	}

	private boolean containConfig(int id) {
		return id != -1 && plugin.getConfig().isConfigurationSection(Integer.toString(id)) && getConfigFromId(id)!=null;
	}

	private boolean validCommandAdd(String[] args) {
		return args.length == 4 && isInteger(args[1]) && isInteger(args[2]) && isInteger(args[3]) && Integer.parseInt(args[1]) >= 0
				&& Integer.parseInt(args[1]) <= 26 && Integer.parseInt(args[2]) > 0 && Integer.parseInt(args[3]) > 0;
	}

	private boolean validCommandRemove(String[] args) {
		return args.length == 2 && isInteger(args[1]) && Integer.parseInt(args[1]) >= 0 && Integer.parseInt(args[1]) <= 26
				||args.length == 2 && args[1].equals("all");
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
