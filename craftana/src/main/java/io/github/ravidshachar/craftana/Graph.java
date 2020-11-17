package io.github.ravidshachar.craftana;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;

import static io.github.ravidshachar.craftana.Constants.*;
import static io.github.ravidshachar.craftana.Methods.*;

import java.io.IOException;

/**
 * 
 * @author ravid
 * Physical representaion of a query range with a graph
 */
public final class Graph extends Panel {

	Plugin plugin;
	Vector leftCoords;
	int step;
	double threshold;
	Boolean isX;
	Boolean autoThreshold;
	Long timestamp = -1L;
	
	/**
	 * Constructor with static threshold
	 */
	public Graph(Plugin plugin, Vector leftCoords, String socketPair, String query, int step, double threshold, Boolean isX) {
		super(socketPair, query);
		this.plugin = plugin;
		this.leftCoords = leftCoords;
		this.step = step;
		this.threshold = threshold;
		this.isX = isX;
		this.autoThreshold = false;
	}
	
	/**
	 * Constructor with auto threshold
	 */
	public Graph(Plugin plugin, Vector leftCoords, String socketPair, String query, int step, Boolean isX) {
		super(socketPair, query);
		this.plugin = plugin;
		this.leftCoords = leftCoords;
		this.step = step;
		this.threshold = -1;
		this.isX = isX;
		this.autoThreshold = true;
	}
	
	/**
	 * clears graph
	 */
	public void clearGraph() {
		drawRect(leftCoords, leftCoords.add(isX ? graphWidth : 0, graphHeight, isX ? 0 : graphWidth), Material.AIR);
	}
	
	/**
	 * receive an int array with a length of <steps> and display it
	 */
	public void drawPillars(int[] heights) {
		Vector currCoords = leftCoords;
		clearGraph();
		for (int i = 0; i + 1 < steps; i++) {
			new Location(
					Bukkit.getServer().getWorld("world"), 
					currCoords.getX(), 
					currCoords.getY() + heights[i], 
					currCoords.getZ()
					).getBlock().setType(graphMat);
			/*new Location(
					Bukkit.getServer().getWorld("world"), 
					currCoords.getX() + (isX ? 2 : 0), 
					currCoords.getY() + heights[i + 1], 
					currCoords.getZ() + (isX ? 0 : 2)
					).getBlock().setType(graphMat);*/
			int min = Math.min(heights[i], heights[i + 1]);
			int max = Math.max(heights[i], heights[i+1]);
			if (min == max) {
				new Location(
						Bukkit.getServer().getWorld("world"), 
						currCoords.getX() + (isX ? 1 : 0), 
						currCoords.getY() + min, 
						currCoords.getZ() + (isX ? 0 : 1)
				).getBlock().setType(graphMat);
			}
			else if (max - min == 1) {
				new Location(
						Bukkit.getServer().getWorld("world"), 
						currCoords.getX() + (isX ? 1 : 0), 
						currCoords.getY() + heights[i], 
						currCoords.getZ() + (isX ? 0 : 1)
				).getBlock().setType(graphMat);
			}
			else {
				for (int j = min + 1; j < max; j++) {
					new Location(
							Bukkit.getServer().getWorld("world"), 
							currCoords.getX() + (isX ? 1 : 0), 
							currCoords.getY() + j, 
							currCoords.getZ() + (isX ? 0 : 1)
					).getBlock().setType(graphMat);
				}
			}
			currCoords = currCoords.add(isX ? 2 : 0, 0, isX ? 0 : 2);
		}
		new Location(
				Bukkit.getServer().getWorld("world"), 
				currCoords.getX(), 
				currCoords.getY() + heights[steps - 1], 
				currCoords.getZ()
				).getBlock().setType(graphMat);
	}
	
	/**
	 * displays the query starting with the left coords
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public void displayQuery() throws JSONException, IOException {
		if (timestamp == -1) {
			timestamp = System.currentTimeMillis() / 1000L;
		}
		while (System.currentTimeMillis() / 1000L - timestamp >= step) {
			timestamp += step;
		}
		String[] values = this.RangeQuery(timestamp, step);
		int[] heights = new int[steps];
		if (threshold == -1)
			this.threshold = this.maxValue();
		if (autoThreshold && Double.parseDouble(values[values.length - 1]) > threshold)
			this.threshold = Double.parseDouble(values[values.length - 1]);
		for (int i = 1; i <= steps; i++) {
			// Going from the end to the beginning, if values has a value, divide it by the threshold and multiply by graphHeight
			// to get proportional pillar height
			if (values.length - i >= 0)
				heights[steps - i] = (int) (graphHeight * Double.parseDouble(values[values.length - i]) / threshold);
			else
				heights[steps - i] = 0;
		}
		drawPillars(heights);
	}
}
