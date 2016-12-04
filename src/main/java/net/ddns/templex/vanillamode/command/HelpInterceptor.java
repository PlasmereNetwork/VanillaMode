package net.ddns.templex.vanillamode.command;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.TabCompleteEvent;

/* VanillaMode plugin for Bukkit: Take a few steps back to Vanilla.
 * Copyright (C) 2016  VTCAKAVSMoACE
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class HelpInterceptor implements Listener {

	static HelpCommand help;
	
	@EventHandler(priority = EventPriority.MONITOR)
	public boolean onTabCompleteEvent(TabCompleteEvent event) {
		if (!(event.getSender() instanceof Player))
			return true;
		
		boolean match = false;

		if (event.getBuffer().startsWith("/help ") || event.getBuffer().startsWith("/? ")) {
			event.getCompletions().clear();
			String alias = VanillaCommandMap.getLabelFromCmdLine(event.getBuffer());
			String[] args = VanillaCommandMap.getArgsFromCmdLine(event.getBuffer());
			event.getCompletions().addAll(help.tabComplete(event.getSender(), alias, args));
		} else if (event.getBuffer().equals("/")) {
			event.getCompletions().add("/help");
			event.getCompletions().add("/?");
		} else if ((match = "/help".startsWith(event.getBuffer())) | (match = "/?".startsWith(event.getBuffer()))) {
			event.getCompletions().add(match ? "/?" : "/help");
		}

		return true;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public boolean onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().startsWith("/help") || event.getMessage().startsWith("/?")) {
			event.setCancelled(true);
			String label = VanillaCommandMap.getLabelFromCmdLine(event.getMessage());
			String[] args = VanillaCommandMap.getArgsFromCmdLine(event.getMessage());
			help.execute(event.getPlayer(), label, args);
		}
		return true;
	}

}
