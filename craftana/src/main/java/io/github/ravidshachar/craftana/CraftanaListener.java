package io.github.ravidshachar.craftana;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CraftanaListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		MCExporter.playerAmount.inc();
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		MCExporter.playerAmount.dec();
	}
}
