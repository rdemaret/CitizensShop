package alaskalix.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import alaskalix.main.Main;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class CreateNpcShop implements CommandExecutor {
	
	private Main plugin;

	public CreateNpcShop(Main main) {
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length >= 1) {
				
				StringBuilder sb = new StringBuilder();
				for (String string : args) {
					sb.append(string);
				}
				
				NPCRegistry registry = CitizensAPI.getNPCRegistry();
				NPC npc = registry.createNPC(EntityType.PLAYER, sb.toString());
				npc.spawn(p.getLocation());
				
				plugin.getConfig().createSection(Integer.toString(npc.getId()));
				plugin.saveConfig();
				
				return true;
			}else {
				p.sendMessage(Messages.CREATE_PATTERN);
				return false;
			}
			
		} else {
			return false;
		}
	}

}
