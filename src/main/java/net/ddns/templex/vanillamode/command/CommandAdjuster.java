package net.ddns.templex.vanillamode.command;

import java.lang.reflect.Field;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;

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

	public CommandAdjuster(VanillaMode plugin) {
		super(plugin);
		this.vanillaCommandMap = new VanillaCommandMap(plugin);
	}

	@Override
	protected final void adjust() throws Exception {
		vanillaCommandMap.clearCommands();
		
		for (ActiveVanillaCommand command : ActiveVanillaCommand.values()) {
			command.getCommand().register(vanillaCommandMap);
		}
		
		final Field f = CraftServer.class.getDeclaredField("commandMap");
		f.setAccessible(true);
		
		f.set(Bukkit.getServer(), vanillaCommandMap);
		
		f.setAccessible(false);
	}

	@Override
	public String getAdjustments() {
		StringBuilder sb = new StringBuilder();
		sb.append("Changed Help command described in org.bukkit.command.SimpleCommandMap to self implemented.\n");
		return sb.toString();
	}

	public Collection<Command> getCommands() {
		return vanillaCommandMap.getCommands();
	}

}
