package io.github.ravidshachar.craftana;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import static io.github.ravidshachar.craftana.Constants.*;

import java.io.IOException;

import static io.github.ravidshachar.craftana.Methods.*;

public class CraftanaCommandExecutor implements CommandExecutor, Listener {
	private final craftana plugin;
	public Vector endpoint;
	ClockDashboard clockDashboard;
	GraphDashboard graphDashboard;
	
	public CraftanaCommandExecutor(craftana plugin) {
		this.plugin = plugin; // Store the plugin
		clockDashboard = new ClockDashboard(plugin, firstArrowCoords, diffX, diffY, true);
		graphDashboard = new GraphDashboard(plugin, firstGraphCoords, false);
	}
	
	@Override
	/**
	 * switch case for the commands, validates them and then calls the relevant functions
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		plugin.getLogger().info("Got to onCommand!");
		switch (cmd.getName().toLowerCase()) {
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
			case "setgraph":
				if (args.length < 5) {
					sender.sendMessage("Must have 5 arguments");
					return false;
				}
				if (args[0].length() != 2 || 
					args[0].charAt(0) < 'F' || 
					args[0].charAt(0) > 'G' ||
					args[0].charAt(1) < '1' ||
					args[0].charAt(1) > '2') {
						sender.sendMessage("clockID must be a letter (F-G) and a number(1-2) in that order");
						return false;
					}
				try {
					Integer.parseInt(args[2]);
				}
				catch(Exception e) {
					sender.sendMessage("step must be a number!");
					return false;
				}
				try {
					Integer.parseInt(args[3]);
				}
				catch(Exception e) {
					sender.sendMessage("threshold must be a number!");
					return false;
				}
				int step = Integer.parseInt(args[2]);
				double graph_threshold = Double.parseDouble(args[3]);
				StringBuilder graph_query = new StringBuilder(args[4]);
				for (int arg = 5; arg < args.length; arg++)
					graph_query.append(" ").append(args[arg]);
				return setGraph(args[0], args[1], step, graph_threshold, graph_query.toString(), sender);
			case "cleardashboard":
				clockDashboard.clearDashboard();
				return true;
		}
		return false;
	}
	
	/**
	 * This method gets a query, socket pair and a threshold, as well as row and column and creates a new panel in game
	 */
	public boolean setClock(String clockID,String socketPair, double threshold, String query, CommandSender sender) {
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
	
	public boolean setGraph(String graphID, String socketPair, int step, double threshold, String query, CommandSender sender) {
		graphDashboard.setGraph(graphID, socketPair, query, step, threshold);
		try {
			plugin.updateAll(this);
		}
		catch (IOException e) {
			plugin.getLogger().info(stackTraceToString(e));
			plugin.getLogger().info("***HANDLED***");
			sender.sendMessage("Unknown Host " + socketPair);
			graphDashboard.removeGraph(graphID);
			return false;
		}
		catch (Exception e) {
			plugin.getLogger().info(stackTraceToString(e));
			return false;
		}
		return true;
	}
}
