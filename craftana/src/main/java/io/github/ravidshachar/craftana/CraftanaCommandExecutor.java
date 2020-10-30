package io.github.ravidshachar.craftana;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
/*import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;*/
import org.bukkit.event.Listener;

import static io.github.ravidshachar.craftana.Constants.*;
//import io.github.ravidshachar.craftana.Clock;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONException;

import static io.github.ravidshachar.craftana.Methods.*;

public class CraftanaCommandExecutor implements CommandExecutor, Listener {
	private final craftana plugin;
	public Vector endpoint;
	Dashboard clockDashboard;
	
	public CraftanaCommandExecutor(craftana plugin) {
		this.plugin = plugin; // Store the plugin
		clockDashboard = new Dashboard(plugin, firstArrowCoords, diffX, diffY, true);
	}
	
	@Override
	/**
	 * switch case for the commands, validates them and then calls the relevant functions
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		plugin.getLogger().info("Got to onCommand!");
		switch (cmd.getName().toLowerCase()) {
			/*case "percent":
				if (args.length != 1) {
					sender.sendMessage("Should only have 1 argument!");
					return false;
				}
				try {
					Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					sender.sendMessage("Argument must be a number!");
					return false;
				}
				int perc = Integer.parseInt(args[0]);
				if (perc < 0 || perc > 100) {
					sender.sendMessage("Argument must be between 0 and 100!");
					return false;
				}
				return percent(perc);*/
			case "getmetric":
				// The query may have spaces in it which will be different arguments, this part basically ensures it is all
				// cluttered together to one string
				/*StringBuilder message = new StringBuilder(args[0]);
				for (int arg = 1; arg < args.length; arg++)
					message.append(" ").append(args[arg]);
				
				try {
					return getMetric(message.toString());
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//BlockString test = new BlockString("ABC");
				//<FOR TESTING>
				//</FOR TESTING>
				//test.drawString(args[0], new Vector(159, 80, -75), true);
			case "setclock":
				if (args.length < 4) {
					sender.sendMessage("Must have 4 arguments");
					return false;
				}
				if (args[0].length() != 2 || 
					args[0].charAt(0) < 'A' || 
					args[0].charAt(0) > 'E' ||
					args[0].charAt(1) < '1' ||
					args[0].charAt(1) > '3') {
					sender.sendMessage("clockID must be a letter (A-E) and a number(1-3) in that order");
					return false;
				}
				try {
					Double.parseDouble(args[2]);
				}
				catch(Exception e) {
					sender.sendMessage("threshold must be a number!");
					return false;
				}
				double threshold = Double.parseDouble(args[2]);
				StringBuilder query = new StringBuilder(args[3]);
				for (int arg = 4; arg < args.length; arg++)
					query.append(" ").append(args[arg]);
				return setClock(args[0], args[1], threshold, query.toString());
			case "cleardashboard":
				clockDashboard.clearDashboard();
				return true;
				
		}
		return false;
	}
	
	/**
	 * This command tests percent on clock algorithm
	 */
	/*public boolean percent(int perc) {
		double radians = Math.PI * perc / 100;
		Vector startpoint = leftCoords; //cords for the arrow startpoint for calculation
		if (endpoint != null) {
			clearLast(leftCoords);
		}
		endpoint = new Vector(); //coords for the arrow endpoint
		if (perc > 50) {
			startpoint = startpoint.add(new Vector(1, 0, 0));
		}
		Vector dist = new Vector(-1 * (int)Math.round(radius*Math.cos(radians)), (int)Math.round(radius*Math.sin(radians)), 0);
		endpoint = startpoint.add(dist);
		plugin.getLogger().info("startpoint: " + startpoint);
		plugin.getLogger().info("endpoint: " + endpoint);
		drawLine(leftCoords, endpoint, Material.GLASS);
		if (perc == 50)
			drawLine(leftCoords.add(new Vector(1, 0, 0)), endpoint.add(new Vector(1, 0, 0)), Material.GLASS);
		else
			drawLine(leftCoords.add(new Vector(1, 0, 0)), endpoint, Material.GLASS);
		return true;
	}*/
	
	/**
	 * This method clears the last line made, assuming it was drawn in the current session
	 */
	/*public void clearLast(Vector leftCoords) {
		drawLine(leftCoords, endpoint, Material.AIR);
		drawLine(leftCoords.add(new Vector(1, 0, 0)), endpoint, Material.AIR);
		drawLine(leftCoords.add(new Vector(1, 0, 0)), endpoint.add(new Vector(1, 0, 0)), Material.AIR);
	}*/
	
	/**
	 * This method clears the entire clock type panel
	 */
	/*public void clearClock(Vector leftCoords, int radius) {
		Vector startpoint, dist, endpoint = new Vector();
		double radians;
		for (int i = 0; i <= 100; i+=2) {
			startpoint = leftCoords;
			radians = Math.PI * i / 100;
			if (i > 50) {
				startpoint = startpoint.add(new Vector(1, 0, 0));
			}
			dist = new Vector(-1 * (int)Math.round(radius*Math.cos(radians)), (int)Math.round(radius*Math.sin(radians)), 0);
			endpoint = startpoint.add(dist);
			drawLine(leftCoords, endpoint, Material.AIR);
			drawLine(leftCoords.add(new Vector(1, 0, 0)), endpoint, Material.AIR);
		}
		drawLine(leftCoords.add(new Vector(1, 0, 0)), endpoint.add(new Vector(1, 0, 0)), Material.AIR);
	}*/
	
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
	 * This method uses a custom query and returns the result from the prometheus host registered to the server as a broadcast
	*/
	public boolean getMetric(String query) throws IOException, JSONException {
		Panel myPanel = new Panel("localhost:9090", query);
		plugin.getLogger().info(String.format("http://%s/api/v1/query?query=", "localhost:9090") + URLEncoder.encode(query, StandardCharsets.UTF_8.toString()));
		//String myQuery = myPanel.Query("100 - (avg by (instance) (irate(node_cpu_seconds_total{mode=\"idle\"}[5m])) * 100)");
		String myQuery = myPanel.Query();
		plugin.getServer().broadcastMessage(myQuery);
		return true;
	}
	
	/**
	 * This method gets a query, socket pair and a threshold, as well as row and column and creates a new panel in game
	 */
	public boolean setClock(String clockID,String socketPair, double threshold, String query) {
		clockDashboard.setClock(clockID, socketPair, query, threshold);
		try {
			plugin.updateAll(this);
		}
		catch (Exception e) {
			plugin.getLogger().info(stackTraceToString(e));
			return false;
		}
		return true;
	}
}
