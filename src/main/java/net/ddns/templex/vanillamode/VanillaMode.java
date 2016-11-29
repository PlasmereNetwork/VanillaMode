package net.ddns.templex.vanillamode;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.ddns.templex.vanillamode.chat.ScoreboardChatIntegration;
import net.ddns.templex.vanillamode.command.CommandAdjuster;
import net.ddns.templex.vanillamode.command.HelpInterceptor;
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

/**
 * VanillaMode main Plugin class. This class enables all changes to the Bukkit
 * server instance in order to go full out Vanilla with optimizations.
 * 
 * @author vtcakavsmoace vtcakavsmoace@gmail.com
 * @version initial-development
 */
public final class VanillaMode extends JavaPlugin {

	private Adjuster[] adjusters;
	private Listener[] listeners;

	@Override
	public void onEnable() {
		/*
		 * TODO: - Disable all other plugins. - Re-vanillify. This will most
		 * likely require a restart, but implementation has not been finalized.
		 */
		applyAdjustments();
		registerListeners();
		getLogger().info("VanillaMode enabled successfully.");
	}

	private void applyAdjustments() {
		getLogger().info("Adjustments initiated.");
		adjusters = new Adjuster[] { new CommandAdjuster(this), };

		for (Adjuster adjuster : adjusters) {
			adjuster.run();
		}
		getLogger().info("Adjustments applied.");
	}

	private void registerListeners() {
		getLogger().info("Begun registering listeners.");
		listeners = new Listener[] { 
				new ScoreboardChatIntegration(this), 
				new HelpInterceptor(),
		};

		for (Listener listener : listeners) {
			getServer().getPluginManager().registerEvents(listener, this);
		}
		// TODO Register other listeners.
		getLogger().info("Listeners registered.");
	}
	
	public <T> T getAdjuster(Class<T> clazz) {
		if (!clazz.getSuperclass().equals(Adjuster.class))
			return null;
		for (Adjuster adjuster : adjusters) {
			if (clazz.isInstance(adjuster))
				return clazz.cast(adjuster);
		}
		return null;
	}

	@Override
	public void onDisable() {
		/*
		 * TODO: - Unload everything.
		 */
	}

}
