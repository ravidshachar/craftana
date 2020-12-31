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
	int step;
	Long timestamp = -1L;
	
	/**
	 * Constructor with static threshold
	 */
	public Graph(Plugin plugin, Vector leftCoords, String socketPair, String query, int step, double maxValue, Boolean isX) {
		super(plugin, leftCoords, socketPair, query, maxValue, isX);
		this.step = step;
	}
	
	/**
	 * Constructor with auto threshold
	 */
	public Graph(Plugin plugin, Vector leftCoords, String socketPair, String query, int step, Boolean isX) {
		super(plugin, leftCoords, socketPair, query, isX);
		this.step = step;
	}
	
	/**
	 * clears graph
	 */
	public void clearPanel() {
		drawRect(leftCoords, leftCoords.add(isX ? graphWidth : 0, graphHeight, isX ? 0 : graphWidth), Material.AIR);
	}
	
	/**
	 * receive an int array with a length of <steps> and display it
	 */
	public void drawGraph(int[] heights) {
		Vector currCoords = leftCoords; // This Vector represents the bottom coords of the current value we're drawing
		clearPanel();
		for (int i = 0; i + 1 < steps; i++) {
			new Location(
					Bukkit.getServer().getWorld("world"), 
					currCoords.getX(), 
					currCoords.getY() + heights[i], 
					currCoords.getZ()
					).getBlock().setType(graphMat); // draw current height (height[i])
			
			// We will now establish the current block and next block to see which is the bigger/smaller one
			// and draw the block(s) in the column that is in-between the two values to make it look like a line graph
			
			int min = Math.min(heights[i], heights[i + 1]);
			int max = Math.max(heights[i], heights[i+1]);
			if (min == max) {
				new Location(
						Bukkit.getServer().getWorld("world"), 
						currCoords.getX() + (isX ? 1 : 0), 
						currCoords.getY() + min, 
						currCoords.getZ() + (isX ? 0 : 1)
				).getBlock().setType(graphMat); // in case the two blocks are the same, draw the block in-between
			}
			else if (max - min == 1) {
				new Location(
						Bukkit.getServer().getWorld("world"), 
						currCoords.getX() + (isX ? 1 : 0), 
						currCoords.getY() + heights[i], 
						currCoords.getZ() + (isX ? 0 : 1)
				).getBlock().setType(graphMat); // in case the difference is just 1 block in either direction draw it in the height of the first block
			}
			else {
				// if the difference between the two is greater than 2, draw all blocks in-between the two heights
				// in the column that is between them (non-inclusive)
				for (int j = min + 1; j < max; j++) {
					new Location(
							Bukkit.getServer().getWorld("world"), 
							currCoords.getX() + (isX ? 1 : 0), 
							currCoords.getY() + j, 
							currCoords.getZ() + (isX ? 0 : 1)
					).getBlock().setType(graphMat);
				}
			}
			currCoords = currCoords.add(isX ? 2 : 0, 0, isX ? 0 : 2); // adjust the currCoords for the next column (value)
		}
		
		// The loop runs short by one block so we fill it in here
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
		
		// if the difference between the current time and timestamp has exceeded the step duration, add step duration to timestamp
		// it is preferable to work like this and not with current time only because for any step duration that is different than
		// the prometheus refresh rate you'll get graphs that are constantly changing completely as they'll grab different 
		// timestamps each time
		while (System.currentTimeMillis() / 1000L - timestamp >= step) {
			timestamp += step;
		}
		String[] values = this.RangeQuery(timestamp, step, steps);
		int[] heights = new int[steps];
		if (maxValue == -1)
			this.maxValue = this.getMaxValue();
		if (autoBoundaries && Double.parseDouble(values[values.length - 1]) > maxValue)
			this.maxValue = Double.parseDouble(values[values.length - 1]);
		for (int i = 1; i <= steps; i++) {
			// Going from the end to the beginning, if values has a value, divide it by the threshold and multiply by graphHeight
			// to get proportional pillar height
			if (values.length - i >= 0)
				heights[steps - i] = Math.min((int) (graphHeight * Double.parseDouble(values[values.length - i]) / maxValue), graphHeight);
			else
				heights[steps - i] = 0;
		}
		drawGraph(heights);
	}
}
