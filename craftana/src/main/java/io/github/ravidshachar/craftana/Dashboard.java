package io.github.ravidshachar.craftana;

import java.io.IOException;
import java.util.HashMap;

//import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;

//import static io.github.ravidshachar.craftana.Methods.*;
//import static io.github.ravidshachar.craftana.Constants.*;


public class Dashboard {
	Plugin plugin;
	HashMap<String, Clock> clocks; // String clockID : Clock clockObject
	int diffH;
	int diffV;
	Boolean isDiffX;
	Vector firstCoords;
	
	public Dashboard(Plugin plugin, Vector firstCoords, int diffH, int diffV, Boolean isDiffX) {
		this.plugin = plugin;
		clocks = new HashMap<String, Clock>();
		this.firstCoords = firstCoords;
		this.diffH = diffH;
		this.diffV = diffV;
		this.isDiffX = isDiffX;
	}
	
	public void setClock(String clockID, String socketPair, String query, double threshold) {
		Vector leftCoords = firstCoords.add(getLeftCoords(clockID));
		clocks.put(clockID, new Clock(plugin, leftCoords, socketPair, query, threshold, isDiffX));
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
			clock.displayQuery(diffV, isDiffX);
			/*Vector bsStart = clock.leftCoords;
			BlockString bs = new BlockString(clock.Query(), bsStart, diffV, isDiffX);
			drawRect(bs.leftCorner,bs.leftCorner.add(isDiffX ? maxText : 0, 4, isDiffX ? 0 : maxText),Material.AIR);
			bs.drawString();*/
		}
	}
	
	public void clearDashboard() {
		//Clock tempClock;
		String clockID;
		for (char i='A'; i <= 'E';i++) {
			for (char j='1'; j <= '3';j++) {
				clockID = new StringBuilder().append(i).append(j).toString();
				removeClock(clockID);
				plugin.getLogger().info(clockID);
				plugin.getLogger().info(getLeftCoords(clockID).toString());
				//tempClock = new Clock(plugin, firstCoords.add(getLeftCoords(clockID)), "", "", 100, isDiffX);
				//tempClock.clearClock();
			}
		}
	}
}
