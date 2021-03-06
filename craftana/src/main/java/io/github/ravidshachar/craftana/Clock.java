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
	public Vector endpoint;
	
	/**
	 * constructor for a static threshold given by the player
	 */
	public Clock(Plugin plugin, Vector leftCoords, String socketPair, String query, double maxValue, Boolean isX) {
		super(plugin, leftCoords, socketPair, query, maxValue, isX);
	}
	
	/**
	 * constructor for a dynamic threshold (auto)
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public Clock(Plugin plugin, Vector leftCoords, String socketPair, String query, Boolean isX) {
		super(plugin, leftCoords, socketPair, query, isX);

	}
	
	public void percent(int perc) {
		if (perc > 100) {
			percent(100);
			return;
		}
		if (perc < 0) {
			percent(0);
			return;
		}
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
		drawLine(leftCoords, endpoint, clockMat);
		if (perc == 50)
			drawLine(leftCoords.add(1, 0, 0), endpoint.add(1, 0, 0), clockMat);
		else
			drawLine(leftCoords.add(1, 0, 0), endpoint, clockMat);
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
	public void clearPanel() {
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
	public void displayQuery() throws NumberFormatException, JSONException, IOException {
		double query = Double.parseDouble(this.Query());
		if (maxValue == -1)
			maxValue = this.getMaxValue();
		if (autoBoundaries && query > maxValue)
			this.maxValue = query;
		percent((int) (100 * query / maxValue));
		BlockString bs = new BlockString(this.Query(), leftCoords, diffY, isX);
		bs.clearString();
		bs.drawString();
	}
}
