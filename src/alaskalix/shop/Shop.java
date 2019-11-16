package alaskalix.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import alaskalix.main.Main;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

public class Shop {

	private NPCRightClickEvent even;
	private Main plugin;
	
	public static final String TITLE = ""+ChatColor.RED + ChatColor.BOLD + "NPC SHOP";

	public Shop(NPCRightClickEvent even, Main main) {
		this.even = even;
		this.plugin = main;
		
	}

	public void open() {
		getPlayer().openInventory(getShop());
	}

	private Player getPlayer() {
		return even.getClicker();
	}

	private int getNPCID() {
		return even.getNPC().getId();
	}
	
	public NPC getNPC() {
		return even.getNPC();
	}

	public boolean isShop() {
		return plugin.getConfig().isConfigurationSection(Integer.toString(getNPCID()));
	}
	
	public ConfigurationSection getConfigurationSection() {
		return plugin.getConfig().getConfigurationSection(Integer.toString(getNPCID()));
	}

	private Inventory getShop() {
		if (isShop()) {
			Inventory shop = Bukkit.createInventory(null, 27, TITLE);
			for (int i = 0; i < shop.getSize(); i++) {
				if (getConfigurationSection().contains(Integer.toString(i))) {
					ItemStack item = getConfigurationSection().getItemStack(Integer.toString(i));
					shop.setItem(i, item);
				}
			}
			return shop;
		} else {
			return null;
		}
	}

}
