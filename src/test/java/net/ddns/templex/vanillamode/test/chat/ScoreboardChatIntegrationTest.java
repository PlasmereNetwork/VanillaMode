package net.ddns.templex.vanillamode.test.chat;

import static org.junit.Assert.assertTrue;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.junit.Test;

import net.ddns.templex.vanillamode.chat.ScoreboardChatIntegration;
import net.ddns.templex.vanillamode.test.util.TestPlayer;

public class ScoreboardChatIntegrationTest {
	
	final ScoreboardChatIntegration sci = new ScoreboardChatIntegration();
	
	@Test
	public void onPlayerChatTestSimple() throws NoSuchMethodException, SecurityException {
		changeName("1");
		AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, TestPlayer.getInstance(), "This is a test.", null);
		assertTrue(sci.onPlayerChat(event));
		assertTrue(event.getFormat().equals("<1$s> %2$s"));
		assertTrue(String.format(event.getFormat(), event.getPlayer(), event.getMessage()).equals("<1$s> This is a test."));
	}
	
	@Test
	public void onPlayerChatTestFormatFailure() throws NoSuchMethodException, SecurityException {
		changeName("%1");
		AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, TestPlayer.getInstance(), "This is a test.", null);
		assertTrue(sci.onPlayerChat(event));
		assertTrue(event.getFormat().equals("<%%1$s> %2$s"));
		assertTrue(String.format(event.getFormat(), event.getPlayer(), event.getMessage()).equals("<%1$s> This is a test."));
	}
	
	
	
	private void changeName(String newName) throws NoSuchMethodException, SecurityException {
		TestPlayer.methods.put(Player.class.getMethod("getDisplayName"), new NameChangeMethodHandler(newName));
	}
	
	private class NameChangeMethodHandler implements TestPlayer.MethodHandler {
		
		private final String newName;
		
		public NameChangeMethodHandler(final String newName) {
			this.newName = newName;
		}

		@Override
		public Object handle(TestPlayer player, Object[] args) {
			return newName;
		}
		
	}
	
}
