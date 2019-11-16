package alaskalix.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import alaskalix.commands.CitizensShop;
import alaskalix.commands.CreateNpcShop;
import alaskalix.manager.EvenListener;
import net.milkbowl.vault.economy.Economy;

import java.util.logging.Logger;

public class Main extends JavaPlugin{

	private static final Logger log = Logger.getLogger("Minecraft");
	private static Economy econ = null;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(new EvenListener(this), this);
		if (getServer().getPluginManager().getPlugin("Citizens") == null
				|| getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
			getLogger().log(java.util.logging.Level.SEVERE, "Citizens 2.0 not found or not enabled");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getCommand("createShop").setExecutor(new CreateNpcShop(this));
		getCommand("ctzShop").setExecutor(new CitizensShop(this));
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public Economy getEconomy() {
		return econ;
	}

}
