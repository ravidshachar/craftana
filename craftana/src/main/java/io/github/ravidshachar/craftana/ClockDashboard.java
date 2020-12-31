package io.github.ravidshachar.craftana;

//import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

//import static io.github.ravidshachar.craftana.Methods.*;
import static io.github.ravidshachar.craftana.Constants.*;


public class ClockDashboard extends Dashboard<Clock> {
	static int diffHorizontal = diffX; // Horizontal difference between two clocks
	static int diffVertical = diffY; // Vertical difference between two clocks
	
	public ClockDashboard(Plugin plugin, Vector firstCoords, Boolean isX) {
		super(plugin, diffHorizontal, diffVertical, isX, firstCoords);
	}
	
	public void setClock(String clockID, String socketPair, String query, double maxValue) {
		if (panels.get(clockID) == null) {
			MCExporter.clockAmount.inc();
		}
		Vector leftCoords = firstCoords.add(getLeftCoords(clockID));
		panels.put(clockID, new Clock(plugin, leftCoords, socketPair, query, maxValue, isX));
		panels.get(clockID).clearPanel();
	}
	
	/**
	 * clock setter with auto threshold
	 */
	public void setClock(String clockID, String socketPair, String query) {
		if (panels.get(clockID) == null) {
			MCExporter.clockAmount.inc();
		}
		Vector leftCoords = firstCoords.add(getLeftCoords(clockID));
		panels.put(clockID, new Clock(plugin, leftCoords, socketPair, query, isX));
		panels.get(clockID).clearPanel();
	}
	
	public void removePanel(String clockID) {
		if (panels.get(clockID) == null)
			return;
		Clock tempClock = panels.remove(clockID);
		tempClock.clearPanel();
		BlockString bs = new BlockString("0.000", tempClock.leftCoords, diffVertical, isX);
		bs.clearString();
	}
	
	protected Vector getLeftCoords(String clockID) {
		char letter = clockID.charAt(0);
		char number = clockID.charAt(1);
		return new Vector(isX ? (letter - 'E') * diffHorizontal : 0, -1 * (number - '1') * diffVertical, isX ? 0 : (letter - 'E') * diffHorizontal);
	}
	
	/*public void updateDashboard() throws NumberFormatException, JSONException, IOException {
		for (Clock clock : clocks.values()) {
			clock.displayQuery();
		}
	}*/
	
	public void clearDashboard() {
		String clockID;
		for (char i='E'; i <= 'H';i++) {
			for (char j='1'; j <= '3';j++) {
				clockID = new StringBuilder().append(i).append(j).toString();
				removePanel(clockID);
				plugin.getLogger().info(clockID);
				plugin.getLogger().info(getLeftCoords(clockID).toString());
			}
		}
		MCExporter.clockAmount.set(0);
	}
}
