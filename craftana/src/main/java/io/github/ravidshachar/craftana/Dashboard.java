package io.github.ravidshachar.craftana;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.plugin.Plugin;
import org.json.JSONException;

public abstract class Dashboard<T extends Panel> {
	Plugin plugin;
	HashMap<String, T> panels; // String graphID : Graph graphObject
	int diffHorizontal;
	int diffVertical;
	Boolean isX;
	Vector firstCoords;
	
	public Dashboard(Plugin plugin, int diffHorizontal, int diffVertical, Boolean isX, Vector firstCoords) {
		this.plugin = plugin;
		panels = new HashMap<String, T>();
		this.diffHorizontal = diffHorizontal;
		this.diffVertical = diffVertical;
		this.isX = isX;
		this.firstCoords = firstCoords;
	}
	
	public void removePanel(String panelID) {
		if (panels.get(panelID) ==null)
			return;
		Panel tempPanel = panels.remove(panelID);
		tempPanel.clearPanel();
	}
	
	protected abstract Vector getLeftCoords(String panelID);
	
	public void updateDashboard() throws NumberFormatException, JSONException, IOException {
		for (T panel : panels.values()) {
			panel.displayQuery();
		}
	}
	
	public abstract void clearDashboard();
}
