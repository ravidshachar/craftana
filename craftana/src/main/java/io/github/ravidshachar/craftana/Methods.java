package io.github.ravidshachar.craftana;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Methods {
	
	/**
	 * get Exception, return the stack trace in String type
	 * @param e some exception
	 * @return String trace
	 */
	public static final String stackTraceToString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();// stack trace as a string
	}
	
	/**
	 * This method draws a line from 2 Vectors
	 */
	public static void drawLine(Vector startpoint, Vector endpoint, Material mat) {
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
	}
	
	public static void drawRect(Vector startpoint, Vector endpoint, Material mat) {
		int lines = endpoint.getY() - startpoint.getY();
    	for (int i = 0; i <= lines; i++)
    		drawLine(startpoint.add(0, i, 0), endpoint.sub(0, lines - i, 0), mat);
    }
	
	public static int arrayMax (int[] arr) {
		int max = arr[0];
		for (int val : arr) {
			max = Math.max(val, max);
		}
		return max;
	}
	
	public static int arraySum (int[] arr) {
		int sum = 0;
		for (int val : arr) {
			sum += val;
		}
		return sum;
	}
}
