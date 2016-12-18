package net.ddns.templex.vanillamode.command;

import java.lang.reflect.Field;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.plugin.PluginManager;

import net.ddns.templex.vanillamode.VanillaMode;
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

	private final VanillaCommandMap vanillaCommandMap;

	private CommandMap old;
	private Field serverMap;
	private Field pluginMap;
	
	public CommandAdjuster(VanillaMode plugin) {
		super(plugin);
		this.vanillaCommandMap = new VanillaCommandMap(plugin);
	}

	@Override
	protected final void adjust() throws Exception {
		PluginManager pm = Bukkit.getServer().getPluginManager();

		serverMap = CraftServer.class.getDeclaredField("commandMap");
		serverMap.setAccessible(true);
		old = CommandMap.class.cast(serverMap.get(Bukkit.getServer()));

		for (String command : old.tabComplete(Bukkit.getConsoleSender(), "")) {
			if (command.startsWith("minecraft:")) {
				vanillaCommandMap.register(null, old.getCommand(command.substring(10)));
				continue;
			}
			getPlugin().getLogger().info(command + " is not a vanilla command.");
		}
		
		vanillaCommandMap.register(null, old.getCommand("reload"));
		
		HelpCommand help = new HelpCommand(vanillaCommandMap);
		
		HelpInterceptor.help = help;
		
		vanillaCommandMap.register(null, help);

		if (vanillaCommandMap.getCommand("help") == null)
			getPlugin().getLogger().info("help command did not register.");

		serverMap.set(Bukkit.getServer(), vanillaCommandMap);
		serverMap.setAccessible(false);

		pluginMap = pm.getClass().getDeclaredField("commandMap");
		pluginMap.setAccessible(true);
		pluginMap.set(pm, vanillaCommandMap);
		pluginMap.setAccessible(false);
	}
	
	@Override
	protected void revert() throws Exception {
		serverMap.setAccessible(true);
		serverMap.set(Bukkit.getServer(), old);
		serverMap.setAccessible(false);
		
		pluginMap.setAccessible(true);
		pluginMap.set(Bukkit.getServer().getPluginManager(), old);
		pluginMap.setAccessible(false);
	}

	@Override
	public String getAdjustments() {
		StringBuilder sb = new StringBuilder();
		sb.append("Changed Help command described in org.bukkit.command.SimpleCommandMap to self implemented.\n");
		sb.append("Replaced SimpleCommandMap instance found in the server with our own.");
		return sb.toString();
	}

	public Collection<Command> getCommands() {
		return vanillaCommandMap.getCommands();
	}

	public VanillaCommandMap getCommandMap() {
		return vanillaCommandMap;
	}

}
