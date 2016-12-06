package net.ddns.templex.vanillamode.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.util.ChatPaginator;

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

public class HelpCommand extends Command implements CommandExecutor {

	protected HelpCommand(VanillaCommandMap commandMap) {
		super("help", "Get help for different commands.", "/help [page|command name]",
				Arrays.asList(new String[] { "?" }));
		this.commandMap = commandMap;
	}

	private final VanillaCommandMap commandMap;

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {

		String commandName = "";
		int page = 1;
		int[] dim;

		if (args.length != 0) {
			try {
				page = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				commandName = args[0];
			}
		}

		if (!commandName.equals("")) {
			Command command = commandMap.getCommand(commandName);
			if (command == null) {
				sender.sendMessage(ChatColor.RED + "Unknown command. Try /help for a list of commands.");
				return true;
			}
			sender.sendMessage(ChatColor.RED + command.getUsage());
			return true;
		}
		
		if (page < 1) {
			sender.sendMessage(ChatColor.RED + "The number you have entered (" + page + ") is too small, it must be at least 1");
			return true;
		}

		List<Command> commands = commandMap.getCommands();
		List<String> commandStrings = new ArrayList<String>(commands.size());
				
		for (Command command : commands) {
			if (command.testPermissionSilent(sender)) {
				commandStrings.add(command.getName());
			}
		}
		
		if (sender instanceof ConsoleCommandSender) {
			dim = new int[] { ChatPaginator.UNBOUNDED_PAGE_HEIGHT, ChatPaginator.UNBOUNDED_PAGE_WIDTH };
		} else {
			dim = new int[] { ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 1,
					ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH };
		}

		int numTopic = (dim[0] - 2);
		int numPages = (int) Math.ceil((commandStrings.size() / (double) numTopic));
		
		if (page > numPages) {
			sender.sendMessage(ChatColor.RED + "The number you have entered (" + page + ") is too big, it must be at most " + numPages);
			return true;
		}
		
		Collections.sort(commandStrings, String.CASE_INSENSITIVE_ORDER);

		sender.sendMessage(ChatColor.DARK_GREEN + "--- Showing help page " + page + " of "
				+ numPages + " (/help <page>) ---");
		for (int i = (page - 1) * numTopic; i < page * numTopic && i < commandStrings.size(); i++) {
			sender.sendMessage(commandMap.getCommand(commandStrings.get(i)).getUsage());
		}
		sender.sendMessage(ChatColor.GREEN
				+ "Tip: Use the <tab> key while typing a command to auto-complete the command or its arguments");

		return true;
	}

	@Override
	public boolean testPermission(CommandSender sender) {
		return true;
	}

	@Override
	public boolean testPermissionSilent(CommandSender sender) {
		return true;
	}

	@Override
	public boolean register(CommandMap commandMap) {
		return commandMap.register(this.getName(), this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		return this.tabComplete(sender, alias, args, null);
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location)
			throws IllegalArgumentException {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(args, "Arguments cannot be null");
		Validate.notNull(alias, "Alias cannot be null");

		if (args.length == 1 && args[0] != null) {
			List<Command> commands = commandMap.getCommands();
			List<String> toReturn = new ArrayList<String>(commands.size());

			for (Command command : commands) {
				if (command.getName().startsWith(args[0])) {
					toReturn.add(command.getName());
				}
				for (String al : command.getAliases()) {
					if (al.startsWith(args[0])) {
						toReturn.add(al);
					}
				}
			}

			return toReturn;
		}

		return new ArrayList<String>(0);
	}

}
