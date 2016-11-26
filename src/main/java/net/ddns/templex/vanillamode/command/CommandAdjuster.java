package net.ddns.templex.vanillamode.command;

import java.lang.reflect.Field;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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

			simpleCommandMap.clearCommands();
			simpleCommandMap.register("minecraft", new HelpCommand());

			commandMapField.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getAdjustments() {
		StringBuilder sb = new StringBuilder();
		sb.append("Changed Help command described in org.bukkit.command.SimpleCommandMap to self implemented.\n");
		return sb.toString();
	}

}
