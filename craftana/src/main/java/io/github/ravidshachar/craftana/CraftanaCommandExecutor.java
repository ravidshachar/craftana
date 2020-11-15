package io.github.ravidshachar.craftana;

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
			//case "getmetric":
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
				return setClock(args[0], args[1], threshold, query.toString(), sender);
			case "cleardashboard":
				clockDashboard.clearDashboard();
				return true;
		}
		return false;
	}
	
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
	public boolean setClock(String clockID,String socketPair, double threshold, String query, CommandSender sender) {
		plugin.getLogger().info(socketPair);
		clockDashboard.setClock(clockID, socketPair, query, threshold);
		try {
			plugin.updateAll(this);
		}
		catch (IOException e) {
			plugin.getLogger().info(stackTraceToString(e));
			plugin.getLogger().info("***HANDLED!***");
			sender.sendMessage("Unknown Host " + socketPair);
			clockDashboard.removeClock(clockID);
			return false;
		}
		catch (Exception e) {
			plugin.getLogger().info(stackTraceToString(e));
			return false;
		}
		return true;
	}
}
