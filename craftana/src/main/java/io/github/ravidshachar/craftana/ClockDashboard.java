package io.github.ravidshachar.craftana;

import java.io.IOException;
import java.util.HashMap;

//import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;

//import static io.github.ravidshachar.craftana.Methods.*;
//import static io.github.ravidshachar.craftana.Constants.*;


public class ClockDashboard {
	Plugin plugin;
	HashMap<String, Clock> clocks; // String clockID : Clock clockObject
	int diffH; // Horizontal difference between two clocks
	int diffV; // Vertical difference between two clocks
	Boolean isDiffX;
	Vector firstCoords;
	
	public ClockDashboard(Plugin plugin, Vector firstCoords, int diffH, int diffV, Boolean isDiffX) {
		this.plugin = plugin;
		clocks = new HashMap<String, Clock>();
		this.firstCoords = firstCoords;
		this.diffH = diffH;
		this.diffV = diffV;
		this.isDiffX = isDiffX;
	}
	
	public void setClock(String clockID, String socketPair, String query, double threshold) {
		if (clocks.get(clockID) == null) {
			MCExporter.clockAmount.inc();
		}
		Vector leftCoords = firstCoords.add(getLeftCoords(clockID));
		clocks.put(clockID, new Clock(plugin, leftCoords, socketPair, query, threshold, isDiffX));
		clocks.get(clockID).clearClock();
	}
	
	/**
	 * clock setter with auto threshold
	 */
	public void setClock(String clockID, String socketPair, String query) {
		if (clocks.get(clockID) == null) {
			MCExporter.clockAmount.inc();
		}
		Vector leftCoords = firstCoords.add(getLeftCoords(clockID));
		clocks.put(clockID, new Clock(plugin, leftCoords, socketPair, query, isDiffX));
		clocks.get(clockID).clearClock();
	}
	
	public void removeClock(String clockID) {
		if (clocks.get(clockID) == null)
			return;
		Clock tempClock = clocks.remove(clockID);
		tempClock.clearClock();
		BlockString bs = new BlockString("0.000", tempClock.leftCoords, diffV, isDiffX);
		bs.clearString();
	}
	
	private Vector getLeftCoords(String clockID) {
		char letter = clockID.charAt(0);
		char number = clockID.charAt(1);
		if (isDiffX)
			return new Vector((letter - 'A') * diffH, -1 * (number - '1') * diffV, 0);
		else
			return new Vector(0, (number - '1') * diffV, -1 * (letter - 'A') * diffH);
	}
	
	public void updateDashboard() throws NumberFormatException, JSONException, IOException {
		for (Clock clock : clocks.values()) {
			clock.displayQuery(diffV);
		}
	}
	
	public void clearDashboard() {
		String clockID;
		for (char i='A'; i <= 'E';i++) {
			for (char j='1'; j <= '3';j++) {
				clockID = new StringBuilder().append(i).append(j).toString();
				removeClock(clockID);
				plugin.getLogger().info(clockID);
				plugin.getLogger().info(getLeftCoords(clockID).toString());
			}
		}
		MCExporter.clockAmount.set(0);
	}
}
