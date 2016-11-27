package net.ddns.templex.vanillamode.command;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.command.Command;

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

@SuppressWarnings("deprecation")
public class CommandAdjuster extends Adjuster {

	private SimpleCommandMap simpleCommandMap;
	private Map<String, Command> knownCommands;
	
	public CommandAdjuster(VanillaMode plugin) {
		super(plugin);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected final void adjust() throws Exception {
		SimplePluginManager simplePluginManager = (SimplePluginManager) getPlugin().getServer().getPluginManager();

		Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
		commandMapField.setAccessible(true);

		this.simpleCommandMap = (SimpleCommandMap) commandMapField.get(simplePluginManager);

		Field simpleMapMap = simpleCommandMap.getClass().getDeclaredField("knownCommands");
		simpleMapMap.setAccessible(true);

		this.knownCommands = (Map<String, Command>) simpleMapMap.get(simpleCommandMap);

		vanillify(knownCommands);
		
		simpleMapMap.set(simpleCommandMap, knownCommands);
		commandMapField.set(simplePluginManager, simpleCommandMap);
		
		getPlugin().getLogger().info("Vanillified commands: " + simpleCommandMap.getCommands());

		simpleMapMap.setAccessible(false);
		commandMapField.setAccessible(false);
	}

	private void vanillify(Map<String, Command> knownCommands) {
		knownCommands.clear();
		addVanillaStandards(knownCommands);
	}

	private void addVanillaStandards(Map<String, Command> knownCommands) {
		for (ActiveVanillaCommand command : ActiveVanillaCommand.values()) {
			VanillaCommand commandObj = command.getCommand();
			knownCommands.put(commandObj.getName(), commandObj);
			for (String alias : commandObj.getAliases()) {
				knownCommands.put(alias, commandObj);
			}
		}
	}

	@Override
	public String getAdjustments() {
		StringBuilder sb = new StringBuilder();
		sb.append("Changed Help command described in org.bukkit.command.SimpleCommandMap to self implemented.\n");
		return sb.toString();
	}
	
	public Collection<Command> getCommands() {
		return simpleCommandMap.getCommands();
	}

}
