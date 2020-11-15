package io.github.ravidshachar.craftana;

import static io.github.ravidshachar.craftana.Constants.*;
import static io.github.ravidshachar.craftana.Methods.*;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;

/**
 * Physical representaion of a Clock type panel in Minecraft
 */
public final class Clock extends Panel {
	Vector leftCoords;
	public Vector endpoint;
	public Plugin plugin;
	double threshold;
	Boolean isX; // if true, the clock is set horizontally to the X axis, if false it is set to the Z axis
	
	public Clock(Plugin plugin, Vector leftCoords, String socketPair, String query, double threshold, Boolean isX) {
		super(socketPair, query);
		this.plugin = plugin;
		this.leftCoords = leftCoords;
		this.threshold = threshold;
		this.isX = isX;
	}
	
	public boolean percent(int perc) {
		if (perc > 100)
			return percent(100);
		if (perc < 0)
			return percent(0);
		double radians = Math.PI * perc / 100;
		Vector dist;
		Vector startpoint = leftCoords; //cords for the arrow startpoint for calculation
		if (endpoint != null) {
			clearLast();
		}
		endpoint = new Vector(); //coords for the arrow endpoint
		if (perc > 50) {
			startpoint = startpoint.add(1, 0, 0);
		}
		if (isX)
			dist = new Vector(-1 * (int)Math.round(radius*Math.cos(radians)), (int)Math.round(radius*Math.sin(radians)), 0);
		else
			dist = new Vector(0, (int)Math.round(radius*Math.sin(radians)), -1 * (int)Math.round(radius*Math.cos(radians)));
		endpoint = startpoint.add(dist);
		//plugin.getLogger().info("startpoint: " + startpoint);
		//plugin.getLogger().info("endpoint: " + endpoint);
		drawLine(leftCoords, endpoint, Material.GLASS);
		if (perc == 50)
			drawLine(leftCoords.add(1, 0, 0), endpoint.add(1, 0, 0), Material.GLASS);
		else
			drawLine(leftCoords.add(1, 0, 0), endpoint, Material.GLASS);
		return true;
	}
	
	/**
	 * This method clears the last line made, assuming it was drawn in the current session
	 */
	public void clearLast() {
		drawLine(leftCoords, endpoint, Material.AIR);
		drawLine(leftCoords.add(1, 0, 0), endpoint, Material.AIR);
		drawLine(leftCoords.add(1, 0, 0), endpoint.add(1, 0, 0), Material.AIR);
	}
	
	/**
	 * This method clears the entire clock type panel
	 */
	public void clearClock() {
		Vector startpoint, dist, endpoint = new Vector();
		double radians;
		for (int i = 0; i <= 100; i++) {
			startpoint = leftCoords;
			radians = Math.PI * i / 100;
			if (i > 50) {
				startpoint = startpoint.add(1, 0, 0);
			}
			dist = new Vector(-1 * (int)Math.round(radius*Math.cos(radians)), (int)Math.round(radius*Math.sin(radians)), 0);
			endpoint = startpoint.add(dist);
			drawLine(leftCoords, endpoint, Material.AIR);
			drawLine(leftCoords.add(1, 0, 0), endpoint, Material.AIR);
		}
		drawLine(leftCoords.add(1, 0, 0), leftCoords.add(1, radius, 0), Material.AIR);
	}
	
	/**
	 * TODO docs
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws NumberFormatException 
	 */
	public void displayQuery(int diffV, Boolean isDiffX) throws NumberFormatException, JSONException, IOException {
		percent((int) (100 * Double.parseDouble(this.Query()) / threshold));
		BlockString bs = new BlockString(this.Query(), leftCoords, diffV, isDiffX);
		bs.clearString();
		bs.drawString();
	}
}
