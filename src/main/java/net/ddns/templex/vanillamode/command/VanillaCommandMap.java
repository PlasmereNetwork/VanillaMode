package net.ddns.templex.vanillamode.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.Validate;
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
		this.commandMap = new HashMap<String, Command>(ActiveVanillaCommand.values().length);
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
		return (commandMap.putIfAbsent(label, command) == null);
	}

	@Override
	public boolean register(String fallbackPrefix, Command command) {
		return register(command.getLabel(), fallbackPrefix, command);
	}

	@Override
	public boolean dispatch(CommandSender sender, String cmdLine) throws CommandException {
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

	@Override
	public Command getCommand(String name) {
		Command command = commandMap.get(name);
		if (command != null)
			return command;
		for (Command posCommand : commandMap.values())
			if (posCommand.getAliases().contains(name))
				return posCommand;
		return null;
	}

	public List<Command> getCommands() {
		return new ArrayList<Command>(commandMap.values());
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String cmdLine) throws IllegalArgumentException {
		return tabComplete(sender, cmdLine, null);
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String cmdLine, Location location)
			throws IllegalArgumentException {
		Validate.notNull(sender, "sender cannot be null.");
		Validate.notNull(cmdLine, "cmdLine cannot be null.");

		String commandName = getLabelFromCmdLine(cmdLine);

		String[] args = getArgsFromCmdLine(cmdLine);

		Command command = getCommand(commandName);

		if (command != null) {
			return command.tabComplete(sender, commandName, args);
		} else if (args.length == 0) {
			List<String> posNames = new ArrayList<String>();
			List<Command> commands = this.getCommands();

			for (Command posCommand : commands)
				if (posCommand.testPermissionSilent(sender))
					if (posCommand.getName().startsWith(commandName))
						posNames.add(posCommand.getName());
					else
						for (String alias : posCommand.getAliases())
							if (alias.startsWith(commandName))
								posNames.add(alias);
		}

		// TODO I know there's a better way to do this but cannot currently
		// find. Implementing.

		Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();

		List<String> posNames = new ArrayList<String>(players.size());

		for (Player player : players) {
			if (player.getName().startsWith(args[args.length - 1]))
				posNames.add(player.getName());
		}

		return posNames;
	}

	private String getLabelFromCmdLine(String cmdLine) {
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

	private String[] getArgsFromCmdLine(String cmdLine) {
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
