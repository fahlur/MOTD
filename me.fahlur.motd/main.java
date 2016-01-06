package me.fahlur.motd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class main extends JavaPlugin implements Listener {
	
	public List<String> motdlist = new ArrayList<String>();

	public String chooseRandom() {
        ArrayList<String> list = (ArrayList<String>) getConfig().getStringList("motd.system-multi");
        Random random = new Random();
        int selector = random.nextInt(list.size());
        String chosenReward = list.get(selector);
        list = null; random = null; selector = -1;
        return chosenReward;
	}
	
	@Override
	public void onEnable(){
		getConfig().options().copyDefaults(true);
		saveConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable(){}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		String motd = getConfig().getString("motd.ingame");
		motd = motd.replaceAll("&", "§");
		p.sendMessage(motd);
	}
	
	
	
	@EventHandler
	public void onServerPing(ServerListPingEvent e){
		
		boolean enabled = getConfig().getBoolean("settings.system-motd");
		boolean multimotd = getConfig().getBoolean("settings.multi-motd");
		if (enabled){
			
			if (multimotd){
				String motd = chooseRandom();
				motd = motd.replaceAll("&", "\u00A7");
				e.setMotd(motd);
			}else{
				String motd = getConfig().getString("motd.system");
				motd = motd.replaceAll("&", "\u00A7");
				e.setMotd(motd);
			}
			
			
		}		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (cmd.getName().equalsIgnoreCase("motd")){
			if (!sender.hasPermission("motd.check")){
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
				return true;
			}
			if (args.length == 1){
				if (args[0].equalsIgnoreCase("reload")){
					if (!sender.hasPermission("motd.reload")){
						sender.sendMessage(ChatColor.RED + "You are not permitted to do this!");
						return true;
					}
					reloadConfig();
					saveConfig();
					sender.sendMessage(ChatColor.RED + "[MOTD] " + ChatColor.GRAY + "Reloaded Configuration File!");
					Bukkit.getLogger().info("[MOTD]: " + sender.getName() + " performed a configuration reload");
					return true;
				}
			}
			String motd = getConfig().getString("motd.ingame");
			motd = motd.replaceAll("&", "§");
			String system = getConfig().getString("motd.system");
			system = system.replaceAll("&", "\u00A7");
			
			sender.sendMessage(ChatColor.GREEN + "In-Game MOTD: " + motd);
			sender.sendMessage(ChatColor.GREEN + "System MOTD: " + system);
			
			return true;
		}
		
		
		if (cmd.getName().equalsIgnoreCase("setmotd")) {
            if (!sender.hasPermission("motd.set")) {
                    sender.sendMessage(ChatColor.RED + "You are not permitted to do this!");
                    return true;
            }
            if (args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Please specify a message!");
                    return true;
            }
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                    str.append(args[i] + " ");
            }
            String motd = str.toString();
            getConfig().set("motd.ingame", motd);
            saveConfig();
            String newmotd = getConfig().getString("motd.ingame");
            motd = motd.replaceAll("&", "§");
            sender.sendMessage(ChatColor.GREEN + "MOTD set to: " + newmotd);
            return true;
		    }
		    if (cmd.getName().equalsIgnoreCase("setsystemmotd")) {
		            if (!sender.hasPermission("motd.set")) {
		                    sender.sendMessage(ChatColor.RED + "You are not permitted to do this!");
		                    return true;
		            }
		            if (args.length == 0) {
		                    sender.sendMessage(ChatColor.RED + "Please specify a message!");
		                    return true;
		            }
		            StringBuilder str = new StringBuilder();
		            for (int i = 0; i < args.length; i++) {
		                    str.append(args[i] + " ");
		            }
		            String motd = str.toString();
		            getConfig().set("motd.system", motd);
		            saveConfig();
		            String system = getConfig().getString("motd.system");
		            system = system.replaceAll("&", "§");
		            sender.sendMessage(ChatColor.GREEN + "MOTD set to: " + system);
		            return true;
		    }
		
		  
		
		
		
		return true;
	}
	

}
