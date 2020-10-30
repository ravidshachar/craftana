package io.github.ravidshachar.craftana;

import static io.github.ravidshachar.craftana.Constants.radius;
import static io.github.ravidshachar.craftana.Methods.drawLine;

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
		plugin.getLogger().info("startpoint: " + startpoint);
		plugin.getLogger().info("endpoint: " + endpoint);
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
	 * This method draws a line from 2 Vectors
	 */
	/*public void drawLine(Vector startpoint, Vector endpoint, Material mat) {
		int x1 = startpoint.getX();
        int y1 = startpoint.getY();
        int z1 = startpoint.getZ();
        int x2 = endpoint.getX();
        int y2 = endpoint.getY();
        int z2 = endpoint.getZ();
        int tipx = x1;
        int tipy = y1;
        int tipz = z1;
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int dz = Math.abs(z2 - z1);

        int dMax = Math.max(Math.max(dx, dy), dz);
        if (dMax == dx) {
        	//plugin.getLogger().info("dx");
            for (int domstep = 0; domstep <= dx; domstep++) {
                tipx = x1 + domstep * (x2 - x1 > 0 ? 1 : -1);
                tipy = (int) Math.round(y1 + domstep * ((double) dy) / ((double) dx) * (y2 - y1 > 0 ? 1 : -1));
                tipz = (int) Math.round(z1 + domstep * ((double) dz) / ((double) dx) * (z2 - z1 > 0 ? 1 : -1));
                //plugin.getLogger().info((new Vector(tipx, tipy, tipz)).toString());
                Block b = (new Location(Bukkit.getServer().getWorld("world"), tipx, tipy, tipz)).getBlock();
                b.setType(mat);
            }
        } else if (dMax == dy) {
        	//plugin.getLogger().info("dy");
            for (int domstep = 0; domstep <= dy; domstep++) {
                tipy = y1 + domstep * (y2 - y1 > 0 ? 1 : -1);
                tipx = (int) Math.round(x1 + domstep * ((double) dx) / ((double) dy) * (x2 - x1 > 0 ? 1 : -1));
                tipz = (int) Math.round(z1 + domstep * ((double) dz) / ((double) dy) * (z2 - z1 > 0 ? 1 : -1));
                //plugin.getLogger().info((new Vector(tipx, tipy, tipz)).toString());
                Block b = (new Location(Bukkit.getServer().getWorld("world"), tipx, tipy, tipz)).getBlock();
                b.setType(mat);
            }
        } else if (dMax == dz) {
        	//plugin.getLogger().info("dz");
            for (int domstep = 0; domstep <= dz; domstep++) {
                tipz = z1 + domstep * (z2 - z1 > 0 ? 1 : -1);
                tipy = (int) Math.round(y1 + domstep * ((double) dy) / ((double) dz) * (y2 - y1 > 0 ? 1 : -1));
                tipx = (int) Math.round(x1 + domstep * ((double) dx) / ((double) dz) * (x2 - x1 > 0 ? 1 : -1));
                //plugin.getLogger().info((new Vector(tipx, tipy, tipz)).toString());
                Block b = (new Location(Bukkit.getServer().getWorld("world"), tipx, tipy, tipz)).getBlock();
                b.setType(mat);
            }
        }
	}*/
	
	/**
	 * TODO docs
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws NumberFormatException 
	 */
	public void displayQuery() throws NumberFormatException, JSONException, IOException {
		percent((int) (100 * Double.parseDouble(this.Query()) / threshold));
	}
}
