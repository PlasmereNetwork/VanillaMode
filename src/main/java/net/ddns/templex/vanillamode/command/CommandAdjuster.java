package net.ddns.templex.vanillamode.command;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;

import net.ddns.templex.vanillamode.util.Adjuster;

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

public class CommandAdjuster extends Adjuster {

	public CommandAdjuster(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	public void run() {
		SimplePluginManager simplePluginManager = (SimplePluginManager) getPlugin().getServer().getPluginManager();
		try {
			Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			SimpleCommandMap simpleCommandMap = (SimpleCommandMap) commandMapField.get(simplePluginManager);

			Field simpleMapMap = simpleCommandMap.getClass().getDeclaredField("knownCommands");
			simpleMapMap.setAccessible(true);
			
			@SuppressWarnings("unchecked")
			Map<String, Command> knownCommands = (Map<String, Command>) simpleMapMap.get(simpleCommandMap);
			
			removeBukkitAdditions(knownCommands);
			
			vanillifyHelp(simpleCommandMap, knownCommands);

			commandMapField.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeBukkitAdditions(Map<String, Command> knownCommands) {
		knownCommands.remove("bukkit:help");
		knownCommands.remove("bukkit:version");
		knownCommands.remove("bukkit:reload");
		knownCommands.remove("bukkit:plugins");
		knownCommands.remove("bukkit:timings");
		knownCommands.remove("version");
		knownCommands.remove("reload");
		knownCommands.remove("plugins");
		knownCommands.remove("timings");
		knownCommands.remove("icanhasbukkit");
	}
	
	private void vanillifyHelp(SimpleCommandMap simpleCommandMap, Map<String, Command> knownCommands) {
		knownCommands.remove("help");
		simpleCommandMap.register("minecraft", new HelpCommand());
	}

	@Override
	public String getAdjustments() {
		StringBuilder sb = new StringBuilder();
		sb.append("Changed Help command described in org.bukkit.command.SimpleCommandMap to self implemented.\n");
		return sb.toString();
	}

}
