package net.ddns.templex.vanillamode.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
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

public class HelpCommand extends org.bukkit.command.defaults.HelpCommand {

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!testPermission(sender))
			return true;

		String command = "";
		int page = 1;
		int[] dim;

		if (args.length != 0) {
			try {
				page = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				command = args[0];
			}
		}

		HelpMap helpMap = Bukkit.getServer().getHelpMap();

		if (!command.equals("")) {
			HelpTopic topic = helpMap.getHelpTopic(command);
			if (topic == null) {
				sender.sendMessage(ChatColor.RED + "Unknown command. Try /help for a list of commands.");
				return true;
			}
			sender.sendMessage(ChatColor.RED + topic.getShortText());
			return true;
		}

		if (sender instanceof ConsoleCommandSender) {
			dim = new int[] { ChatPaginator.UNBOUNDED_PAGE_HEIGHT, ChatPaginator.UNBOUNDED_PAGE_WIDTH };
		} else {
			dim = new int[] { ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 1,
					ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH };
		}

		HelpTopic[] topics = (HelpTopic[]) helpMap.getHelpTopics().toArray();
		int numTopic = (dim[0] - 2);

		sender.sendMessage(ChatColor.DARK_GREEN + "--- Showing help page " + page + " of " + (topics.length / (dim[0] - 2)) + "(/help <page>) ---");
		for (int i = (page - 1) * numTopic; i < page * numTopic && i < topics.length; i++) {
			sender.sendMessage(ChatColor.RESET + topics[i % numTopic].getShortText());
		}
		sender.sendMessage(ChatColor.GREEN + "Tip: Use the <tab> key while typing a command to auto-complete the command or its arguments");

		return true;
	}
	
	

}
