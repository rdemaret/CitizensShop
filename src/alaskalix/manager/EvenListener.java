package alaskalix.manager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import alaskalix.commands.Messages;
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
		if (item == null)return;
		if (!even.getView().getTitle().equals(Shop.TITLE))return;
		if (!avoidSelfTrade(p,item)) {
			even.setCancelled(true);
			return;
		}
		
		switch(even.getClick()) {
			case LEFT : 
				even.setCancelled(true);
				String buyPrice = getSelectedItemPrice(item,0);
				buyItem(p, item, buyPrice);
				break;
			case RIGHT : 
				even.setCancelled(true);
				String sellPrice = getSelectedItemPrice(item,1);
				sellItem(p,item,sellPrice);
				break;
			default: 
				even.setCancelled(true);
				break;
		}
	}

	private String getSelectedItemPrice(ItemStack item,int line) {
		String price = item.getItemMeta().getLore().get(line);
		return price.substring(price.lastIndexOf(' ') + 1, price.lastIndexOf('$'));
	}

	//Evade self-buying
	private boolean avoidSelfTrade(Player p, ItemStack item) {
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (item.isSimilar(p.getInventory().getContents()[i])) {
				return false;
			}
		}
		return true;
	}

	private void buyItem(Player p, ItemStack item, String price) {
		if (plugin.getEconomy().has(p, Integer.parseInt(price))) {
			plugin.getEconomy().withdrawPlayer(p, Integer.parseInt(price));
			p.sendMessage(Messages.formatBuyAccept(price,plugin.getEconomy().getBalance(p)));
			p.getInventory().addItem(new ItemStack(item.getType(),item.getAmount()));
		} else {
			p.sendMessage(Messages.formatBuyRefused(plugin.getEconomy().getBalance(p)));
		}
	}
	
	private void sellItem(Player p, ItemStack item, String price) {
		if (p.getInventory().contains(item.getType(), item.getAmount())) {
			plugin.getEconomy().depositPlayer(p, Integer.parseInt(price));
			p.getInventory().removeItem(new ItemStack(item.getType(),item.getAmount()));
			p.sendMessage(Messages.formatSellAccept(price,plugin.getEconomy().getBalance(p)));
		} else {
			p.sendMessage(Messages.formatSellRefused(plugin.getEconomy().getBalance(p)));
		}
	}
}
