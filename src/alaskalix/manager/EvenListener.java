package alaskalix.manager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import alaskalix.main.Main;
import alaskalix.shop.Shop;
import net.citizensnpcs.api.event.NPCRemoveEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class EvenListener implements Listener {
	
	private Main plugin;

	public EvenListener(Main main) {
		plugin = main;
	}

	@EventHandler
	public void onRightClickNPC(NPCRightClickEvent even) {
		Player p = even.getClicker();
		if (!p.getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.STICK))) {
			Shop shop = new Shop(even, plugin);
			if (shop.isShop()) {
				shop.open();
			} else {
				return;
			}
		}
	}

	@EventHandler
	public void onRemoveNPC(NPCRemoveEvent even) {
		plugin.getConfig().set(Integer.toString(even.getNPC().getId()), null);
		plugin.saveConfig();
	}

	@EventHandler
	public void onClick(InventoryClickEvent even) {
		Player p = (Player) even.getWhoClicked();
		ItemStack item = even.getCurrentItem();
		if (item == null) {
			return;
		}

		if (even.getView().getTitle().equals("§8NPC SHOP")) {

			for (int i = 0; i < p.getInventory().getSize(); i++) {
				if (item.isSimilar(p.getInventory().getContents()[i])) {
					even.setCancelled(true);
					return;
				}
			}
			even.setCancelled(true);

			String price = item.getItemMeta().getLore().get(0);
			price = price.substring(price.lastIndexOf(' ') + 1, price.lastIndexOf('$'));

			buyItem(p, item, price);
			return;
		}
	}

	private void buyItem(Player p, ItemStack item, String price) {
		if (plugin.getEconomy().has(p, Integer.parseInt(price))) {
			plugin.getEconomy().withdrawPlayer(p, Integer.parseInt(price));
			p.sendMessage(
					"§2Vous avez acheté pour §e" + price + "$§2, il vous reste : §e" + plugin.getEconomy().getBalance(p) + "$");
			ItemStack newItem = new ItemStack(item.getType());
			newItem.setAmount(item.getAmount());
			p.getInventory().addItem(newItem);
		} else {
			p.sendMessage("§cVous n'avez pas assez d'argent, votre solde : §e" + plugin.getEconomy().getBalance(p) + "$");
		}
	}
}
