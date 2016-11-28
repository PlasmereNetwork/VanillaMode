package net.ddns.templex.vanillamode.command;

import org.bukkit.command.defaults.*;

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
	HELP(new net.ddns.templex.vanillamode.command.HelpCommand()),
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
	
	private final org.bukkit.command.defaults.VanillaCommand command;
	
	private ActiveVanillaCommand(org.bukkit.command.defaults.VanillaCommand command) {
		this.command = command;
	}
	
	public org.bukkit.command.defaults.VanillaCommand getCommand() {
		return command;
	}
	
}
