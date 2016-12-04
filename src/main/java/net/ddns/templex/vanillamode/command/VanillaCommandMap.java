package net.ddns.templex.vanillamode.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import net.ddns.templex.vanillamode.VanillaMode;

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

public class VanillaCommandMap extends SimpleCommandMap implements CommandMap {

	private final VanillaMode plugin;
	private final HashMap<String, Command> commandMap;

	public VanillaCommandMap(VanillaMode plugin) {
		super(plugin.getServer());
		this.plugin = plugin;
		this.commandMap = new HashMap<String, Command>();
	}

	@Override
	public void setFallbackCommands() {
		// "What even are fallback commands?" - Vanilla
	}

	@Override
	public void registerAll(String fallbackPrefix, List<Command> commands) {
		for (Command command : commands) {
			register(fallbackPrefix, command);
		}
	}

	@Override
	public boolean register(String label, String fallbackPrefix, Command command) {
		if (commandMap == null)
			return false;
		commandMap.remove(label);
		return commandMap.put(label, command) != null;
	}

	@Override
	public boolean register(String fallbackPrefix, Command command) {
		return register(command.getLabel(), fallbackPrefix, command);
	}

	@Override
	public boolean dispatch(CommandSender sender, String cmdLine) throws CommandException {
		plugin.getLogger().info("Command dispatch called for sender " + sender.getName() + " executing " + cmdLine);

		String label = getLabelFromCmdLine(cmdLine);

		Command command = getCommand(label);
		if (command == null)
			return false;

		String[] args = getArgsFromCmdLine(cmdLine);

		// Handler taken from org.bukkit.command.SimpleCommandMap.
		try {
			command.execute(sender, label, args);
		} catch (CommandException e) {
			throw e;
		} catch (Throwable e) {
			throw new CommandException("Unhandled exception executing '" + cmdLine + "' in " + sender, e);
		}

		return true;
	}

	@Override
	public void clearCommands() {
		commandMap.clear();
	}

	public void removeCommand(Command command) {
		commandMap.remove(command.getName());
		for (String alias : command.getAliases())
			commandMap.remove(alias);
	}

	@Override
	public Command getCommand(String name) {
		for (Command command : commandMap.values()) {
			if (name.equals(command.getLabel())) {
				return command;
			}
			for (String alias : command.getAliases()) {
				if (name.equals(alias)) {
					return command;
				}
			}
		}
		return null;
	}

	public List<Command> getCommands() {
		return new ArrayList<Command>(commandMap.values());
	}
	
	public List<String> getCommandStrings() {
		return new ArrayList<String>(commandMap.keySet());
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String cmdLine) throws IllegalArgumentException {
		return tabComplete(sender, cmdLine, null);
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String cmdLine, Location location)
			throws IllegalArgumentException {
		if (sender instanceof Player && (cmdLine.startsWith("help") || cmdLine.startsWith("?")))
			return new ArrayList<String>(0);
		return super.tabComplete(sender, cmdLine, location);
	}

	public static String getLabelFromCmdLine(String cmdLine) {
		if (cmdLine == null)
			return null;
		cmdLine = cmdLine.trim();
		if (cmdLine.startsWith("/"))
			cmdLine = cmdLine.substring(1);

		int stringEnd = cmdLine.indexOf(' ');

		if (stringEnd == -1)
			return cmdLine;

		return cmdLine.substring(0, cmdLine.indexOf(' '));
	}

	public static String[] getArgsFromCmdLine(String cmdLine) {
		if (cmdLine == null)
			return null;

		boolean endsWithSpace = cmdLine.endsWith(" ");

		cmdLine = cmdLine.trim();
		if (cmdLine.startsWith("/"))
			cmdLine = cmdLine.substring(1);

		if (endsWithSpace)
			cmdLine = cmdLine.concat(" ");

		int stringEnd = cmdLine.indexOf(' ');

		if (stringEnd == -1)
			return new String[0];

		return cmdLine.substring(stringEnd + 1).split(" ");
	}

}
