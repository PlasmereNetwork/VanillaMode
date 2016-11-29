package net.ddns.templex.vanillamode.command;

import org.bukkit.command.Command;
import org.bukkit.command.defaults.*;

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
public enum ActiveVanillaCommand {
	ACHIEVEMENT(new AchievementCommand()),
	BAN(new BanCommand()),
	BANIP(new BanIpCommand()),
	BANLIST(new BanListCommand()),
	CLEAR(new ClearCommand()),
	DEFAULTGAMEMODE(new DefaultGameModeCommand()),
	DEOP(new DeopCommand()),
	DIFFICULTY(new DifficultyCommand()),
	EFFECT(new EffectCommand()),
	ENCHANT(new EnchantCommand()),
	EXP(new ExpCommand()),
	GAMEMODE(new GameModeCommand()),
	GAMERULE(new GameRuleCommand()),
	GIVE(new GiveCommand()),
	HELP(new HelpCommand()),
	KICK(new KickCommand()),
	KILL(new KillCommand()),
	LIST(new ListCommand()),
	ME(new MeCommand()),
	OP(new OpCommand()),
	PARDON(new PardonCommand()),
	PARDONIP(new PardonIpCommand()),
	PLAYSOUND(new PlaySoundCommand()),
	SAVE(new SaveCommand()),
	SAVEOFF(new SaveOffCommand()),
	SAVEON(new SaveOnCommand()),
	SAY(new SayCommand()),
	SCOREBOARD(new ScoreboardCommand()),
	SEED(new SeedCommand()),
	SETIDLETIMEOUT(new SetIdleTimeoutCommand()),
	SPREADPLAYERS(new SpreadPlayersCommand()),
	STOP(new StopCommand()),
	TELEPORT(new TeleportCommand()),
	TELL(new TellCommand()),
	TESTFOR(new TestForCommand()),
	TIME(new TimeCommand()),
	TOGGLEDOWNFALL(new ToggleDownfallCommand()),
	WEATHER(new WeatherCommand()),
	WHITELIST(new WhitelistCommand());
	
	private final Command command;
	
	private ActiveVanillaCommand(Command command) {
		this.command = command;
	}
	
	public Command getCommand() {
		return command;
	}
	
}
