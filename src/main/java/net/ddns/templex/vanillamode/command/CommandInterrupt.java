package net.ddns.templex.vanillamode.command;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import net.ddns.templex.vanillamode.VanillaMode;

public class CommandInterrupt implements Listener  {
	
	private final VanillaMode plugin;
	private final CommandAdjuster commandAdjuster;
	
	public CommandInterrupt(VanillaMode plugin) {
		this.plugin = plugin;
		this.commandAdjuster = plugin.getAdjuster(CommandAdjuster.class);
		fixTab();
	}
	
	private void fixTab() {
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public boolean onTabCompleteEvent(TabCompleteEvent event) {
		String buffer = event.getBuffer();
		if (!buffer.trim().startsWith("/"))
			return true;
		switch (buffer) {
		case "/":
			
		}
		return true;
	}
	
	
	
}
