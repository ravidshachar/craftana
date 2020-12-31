package io.github.ravidshachar.craftana;

import static io.github.ravidshachar.craftana.Constants.*;

import org.bukkit.plugin.Plugin;

public class HistogramDashboard extends Dashboard<Histogram> {
	static int diffHorizontal = -1 * (histogramWidth + 2);
	static int diffVertical = -1 * (histogramHeight + 1);
	
	public HistogramDashboard(Plugin plugin, Vector firstCoords, Boolean isX) {
		super(plugin, diffHorizontal, diffVertical, isX, firstCoords);
	}
	
	/**
	 * histogram setter with static min & max values
	 */
	public void setHistogram(String histogramID, String socketPair, String query, double minValue, double maxValue) {
		if (panels.get(histogramID) == null) {
			MCExporter.histogramAmount.inc();
		}
		Vector leftCoords = firstCoords.add(getLeftCoords(histogramID));
		panels.put(histogramID, new Histogram(plugin, leftCoords, socketPair, query, minValue, maxValue, isX));
		panels.get(histogramID).clearPanel();
	}
	
	/*public void removePanel(String histogramID) {
		if (histograms.get(histogramID) == null)
			return;
		Histogram tempHistogram = histograms.remove(histogramID);
		tempHistogram.clearPanel();
	}*/
	
	protected Vector getLeftCoords(String histogramID) {
		char letter = histogramID.charAt(0);
		char number = histogramID.charAt(1);
		return new Vector(isX ? (letter - 'A') * diffHorizontal : 0, (number - '1') * diffVertical, isX ? 0 : (letter - 'A') * diffHorizontal);
	}
	
	/*public void updateDashboard() throws NumberFormatException, JSONException, IOException {
		for (Histogram histogram : histograms.values()) {
			histogram.displayQuery();
		}
	}*/
	
	/**
	 * This function clears current graphs as long as they're registered
	 */
	public void clearDashboard() {
		String histogramID;
		for (char i='A'; i <= 'D';i++) {
			for (char j='1'; j <= '2';j++) {
				histogramID = new StringBuilder().append(i).append(j).toString();
				removePanel(histogramID);
			}
		}
		MCExporter.histogramAmount.set(0);
	}
}
