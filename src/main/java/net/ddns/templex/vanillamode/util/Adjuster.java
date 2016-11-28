package net.ddns.templex.vanillamode.util;

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

public abstract class Adjuster implements Runnable {
	
	private final VanillaMode plugin;
	
	public Adjuster(VanillaMode plugin) {
		this.plugin = plugin;
	}
	
	protected abstract void adjust() throws Exception;
	
	public final void run() {
		plugin.getLogger().info(this.getClass().getName() + " adjustments started.");
		try {
			adjust();
			plugin.getLogger().info(this.getClass().getName() + " adjustments succeeded.");
		} catch (Exception e) {
			plugin.getLogger().warning(this.getClass().getName() + " adjustments failed. Stacktrace:");
			e.printStackTrace();
		}
	}
	
	public abstract String getAdjustments();

	public VanillaMode getPlugin() {
		return plugin;
	}
		
	public final String toString() {
		return this.getClass().getSimpleName();
	}

}
