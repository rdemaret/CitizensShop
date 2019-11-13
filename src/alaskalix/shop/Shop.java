package alaskalix.shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import alaskalix.main.Main;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class Shop {

	private NPCRightClickEvent even;
	private Main plugin;

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

	public boolean isShop() {
		return plugin.getConfig().isConfigurationSection(Integer.toString(getNPCID()));
	}

	private Inventory getShop() {
		if (isShop()) {
			Inventory shop = Bukkit.createInventory(null, 27, "§8NPC SHOP");
			for (int i = 0; i < shop.getSize(); i++) {
				if (plugin.getConfig().getConfigurationSection(Integer.toString(getNPCID()))
						.contains(Integer.toString(i))) {
					ItemStack item = plugin.getConfig().getConfigurationSection(Integer.toString(getNPCID()))
							.getItemStack(Integer.toString(i));
					shop.setItem(i, item);
				}
			}
			return shop;
		} else {
			return null;
		}
	}

}
