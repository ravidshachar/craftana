package io.github.ravidshachar.craftana;

import static io.github.ravidshachar.craftana.Constants.*;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.plugin.Plugin;
import org.json.JSONException;

public class HistogramDashboard {
	Plugin plugin;
	HashMap<String, Histogram> histograms; // String histogramID : Histogram histogramObject
	int diffHorizontal;
	int diffVertical;
	Boolean isX;
	Vector firstCoords;
	
	public HistogramDashboard(Plugin plugin, Vector firstCoords, Boolean isX) {
		this.plugin = plugin;
		histograms = new HashMap<String, Histogram>();
		this.diffHorizontal = -1 * (histogramWidth + 2);
		this.diffVertical = -1 * (histogramHeight + 1);
		this.firstCoords = firstCoords;
		this.isX = isX;
	}
	
	/**
	 * histogram setter with static min & max values
	 */
	public void setHistogram(String histogramID, String socketPair, String query, double minValue, double maxValue) {
		if (histograms.get(histogramID) == null) {
			MCExporter.histogramAmount.inc();
		}
		Vector leftCoords = firstCoords.add(getLeftCoords(histogramID));
		histograms.put(histogramID, new Histogram(plugin, leftCoords, socketPair, query, minValue, maxValue, isX));
		histograms.get(histogramID).clearHistogram();
	}
	
	public void removeHistogram(String histogramID) {
		if (histograms.get(histogramID) == null)
			return;
		Histogram tempHistogram = histograms.remove(histogramID);
		tempHistogram.clearHistogram();
	}
	
	private Vector getLeftCoords(String histogramID) {
		char letter = histogramID.charAt(0);
		char number = histogramID.charAt(1);
		return new Vector(isX ? (letter - 'A') * diffHorizontal : 0, (number - '1') * diffVertical, isX ? 0 : (letter - 'A') * diffHorizontal);
	}
	
	public void updateDashboard() throws NumberFormatException, JSONException, IOException {
		for (Histogram histogram : histograms.values()) {
			histogram.displayQuery();
		}
	}
	
	/**
	 * This function clears current graphs as long as they're registered
	 */
	public void clearDashboard() {
		String histogramID;
		for (char i='A'; i <= 'D';i++) {
			for (char j='1'; j <= '2';j++) {
				histogramID = new StringBuilder().append(i).append(j).toString();
				removeHistogram(histogramID);
			}
		}
		MCExporter.histogramAmount.set(0);
	}
}
