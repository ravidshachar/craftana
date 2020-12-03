package io.github.ravidshachar.craftana;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static io.github.ravidshachar.craftana.Constants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.github.ravidshachar.craftana.Methods.*;

public class CraftanaCommandExecutor implements CommandExecutor, Listener {
	private final craftana plugin;
	public Vector endpoint;
	ClockDashboard clockDashboard;
	GraphDashboard graphDashboard;
	HistogramDashboard histogramDashboard;
	
	public CraftanaCommandExecutor(craftana plugin) {
		this.plugin = plugin; // Store the plugin
		clockDashboard = new ClockDashboard(plugin, firstArrowCoords, diffX, diffY, true);
		graphDashboard = new GraphDashboard(plugin, firstGraphCoords, false);
		histogramDashboard = new HistogramDashboard(plugin, firstHistogramCoords, false);
	}
	
	@Override
	/**
	 * switch case for the commands, validates them and then calls the relevant functions
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		plugin.getLogger().info("Got to onCommand!");
		switch (cmd.getName().toLowerCase()) {
		
			case "sethistogram":
				if (args.length < 5) {
					sender.sendMessage("Must have 5 arguments");
					return false;
				}
			
				if (args[0].length() != 2 || 
						args[0].charAt(0) < 'A' || 
						args[0].charAt(0) > 'D' ||
						args[0].charAt(1) < '1' ||
						args[0].charAt(1) > '2') 
				{
						sender.sendMessage("histogramID must be a letter (A-D) and a number(1-2) in that order");
						return false;
				}
			
				try {
					//if (!args[3].equalsIgnoreCase("auto"))
					Double.parseDouble(args[2]);
					Double.parseDouble(args[3]);
				}
				catch(Exception e) {
					sender.sendMessage("min and max values must be auto or numbers!");
					return false;
				}
			
				StringBuilder histogram_query = new StringBuilder(args[4]);
				for (int arg = 5; arg < args.length; arg++)
					histogram_query.append(" ").append(args[arg]);
				//if (args[3].equalsIgnoreCase("auto"))
				//	return setGraph(args[0], args[1], step, histogram_query.toString(), sender);
				return setHistogram(args[0], args[1], Double.parseDouble(args[2]), Double.parseDouble(args[3]), histogram_query.toString(), sender);
		
		
			case "setclock":
				if (args.length < 4) {
					sender.sendMessage("Must have 4 arguments");
					return false;
				}
				
				if (args[0].length() != 2 || 
					args[0].charAt(0) < 'E' || 
					args[0].charAt(0) > 'H' ||
					args[0].charAt(1) < '1' ||
					args[0].charAt(1) > '3') {
						sender.sendMessage("clockID must be a letter (E-H) and a number(1-3) in that order");
						return false;
				}
				
				try {
					if (!args[2].equalsIgnoreCase("auto"))
						Double.parseDouble(args[2]);
				}
				catch(Exception e) {
					sender.sendMessage("max value must be auto or a number!");
					return false;
				}
				
				StringBuilder query = new StringBuilder(args[3]);
				for (int arg = 4; arg < args.length; arg++)
					query.append(" ").append(args[arg]);
				if (args[2].equalsIgnoreCase("auto"))
					return setClock(args[0], args[1], query.toString(), sender);
				return setClock(args[0], args[1], Double.parseDouble(args[2]), query.toString(), sender);
				
			case "setgraph":
				if (args.length < 5) {
					sender.sendMessage("Must have 5 arguments");
					return false;
				}
				
				if (args[0].length() != 2 || 
					args[0].charAt(0) < 'I' || 
					args[0].charAt(0) > 'K' ||
					args[0].charAt(1) < '1' ||
					args[0].charAt(1) > '2') 
				{
						sender.sendMessage("graphID must be a letter (I-K) and a number(1-2) in that order");
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
					if (!args[3].equalsIgnoreCase("auto"))
						Double.parseDouble(args[3]);
				}
				catch(Exception e) {
					sender.sendMessage("max value must be auto or a number!");
					return false;
				}
				
				int step = Integer.parseInt(args[2]);
				StringBuilder graph_query = new StringBuilder(args[4]);
				for (int arg = 5; arg < args.length; arg++)
					graph_query.append(" ").append(args[arg]);
				if (args[3].equalsIgnoreCase("auto"))
					return setGraph(args[0], args[1], step, graph_query.toString(), sender);
				return setGraph(args[0], args[1], step, Double.parseDouble(args[3]), graph_query.toString(), sender);
				
				
			case "cleardashboard":
				clockDashboard.clearDashboard();
				graphDashboard.clearDashboard();
				histogramDashboard.clearDashboard();
				return true;
				
			case "import":
				if (args.length != 1) {
					sender.sendMessage("Must have 1 path argument");
					return false;
				}
				
				try {
					if (!importFile(args[0], sender)) {
						sender.sendMessage("No changes made");
						return false;
					}
				} 
				catch (FileNotFoundException e) {
					sender.sendMessage("No such file: " + args[0]);
					return false;
				}
				
				sender.sendMessage("Changes made!");
				return true;
				
			case "export":
				if (args.length != 1) {
					sender.sendMessage("Must have 1 path argument");
					return false;
				}
				try {
					exportFile(args[0]);
					sender.sendMessage("Successfuly exported!");
					return true;
				}
				catch (IOException e) {
					sender.sendMessage("Something went wrong! check server console!");
					plugin.getLogger().info(stackTraceToString(e));
					return false;
				}
				
			case "drawrect":
				if (args.length != 6) {
					sender.sendMessage("must have 6 args");
					return false;
				}
				drawRect(new Vector(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])), 
						 new Vector(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])), 
						 Material.BLACK_CONCRETE);
				return true;
		}
		return false;
	}
	
	/**
	 * This method gets a query, socket pair and a threshold, as well as row and column and creates a new panel in game
	 */
	public boolean setClock(String clockID,String socketPair, double maxValue, String query, CommandSender sender) {
		clockDashboard.setClock(clockID, socketPair, query, maxValue);
		try {
			plugin.updateAll(this);
		}
		catch (IOException e) {
			plugin.getLogger().info(stackTraceToString(e));
			plugin.getLogger().info("***HANDLED!***");
			sender.sendMessage("Bad query or Unknown Host " + socketPair);
			clockDashboard.removeClock(clockID);
			return false;
		}
		catch (Exception e) {
			plugin.getLogger().info(stackTraceToString(e));
			return false;
		}
		
		return true;
	}
	
	/**
	 * This method gets a query, socketpair and a clockID and creates a new panel with auto thresholding
	 */
	public boolean setClock(String clockID, String socketPair, String query, CommandSender sender) {
		clockDashboard.setClock(clockID, socketPair, query);
		
		try {
			plugin.updateAll(this);
		}
		catch (IOException e) {
			plugin.getLogger().info(stackTraceToString(e));
			plugin.getLogger().info("***HANDLED!***");
			sender.sendMessage("Bad query or Unknown Host " + socketPair);
			clockDashboard.removeClock(clockID);
			return false;
		}
		catch (Exception e) {
			plugin.getLogger().info(stackTraceToString(e));
			return false;
		}
		
		return true;
	}
	
	/**
	 * graph setter with static max value
	 */
	public boolean setGraph(String graphID, String socketPair, int step, double maxValue, String query, CommandSender sender) {
		graphDashboard.setGraph(graphID, socketPair, query, step, maxValue);
		
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
	
	/**
	 * graph setter with auto max value
	 */
	public boolean setGraph(String graphID, String socketPair, int step, String query, CommandSender sender) {
		graphDashboard.setGraph(graphID, socketPair, query, step);
		
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
	
	public boolean setHistogram(String histogramID, String socketPair, double minValue, double maxValue, String query, CommandSender sender) {
		histogramDashboard.setHistogram(histogramID, socketPair, query, minValue, maxValue);
		
		try {
			plugin.updateAll(this);
		}
		catch (IOException e) {
			plugin.getLogger().info(stackTraceToString(e));
			plugin.getLogger().info("***HANDLED***");
			sender.sendMessage("Unknown Host " + socketPair);
			histogramDashboard.removeHistogram(histogramID);
			return false;
		}
		catch (Exception e) {
			plugin.getLogger().info(stackTraceToString(e));
			return false;
		}
		return true;
	}
	
	/**
	 * import JSON config file, if at least 1 change was made return true
	 */
	public boolean importFile(String path, CommandSender sender) throws FileNotFoundException {
		boolean success;
		boolean partialSuccess = false;
		Scanner myReader = new Scanner(new File(path));
		String jsonString = myReader.useDelimiter("\\Z").next(); //parse JSON file into string
		myReader.close();
		JSONObject obj;
		
		try {
			obj = new JSONObject(jsonString);
		}
		catch (JSONException e) {
			sender.sendMessage("Bad JSON file");
			return false;
		}
		
		JSONObject temp;
		JSONArray clocks = obj.getJSONObject("dashboards").getJSONArray("clocks");
		JSONArray graphs = obj.getJSONObject("dashboards").getJSONArray("graphs");
		JSONArray histograms = obj.getJSONObject("dashboards").getJSONArray("histograms");
		
		for (int i = 0; i < clocks.length(); i++) {
			temp = clocks.getJSONObject(i);
			if (temp.get("maxValue").equals("auto"))
				success = setClock(temp.getString("clockID"), temp.getString("socketPair"), temp.getString("query"), sender);
			else
				success = setClock(temp.getString("clockID"), temp.getString("socketPair"), temp.getDouble("maxValue"), temp.getString("query"), sender);
			plugin.getLogger().info(temp.getString("clockID") + " " + String.valueOf(success));
			partialSuccess = partialSuccess || success;
		}
		
		for (int i = 0; i < graphs.length(); i++) {
			temp = graphs.getJSONObject(i);
			if (temp.get("maxValue").equals("auto"))
				success = setGraph(temp.getString("graphID"), temp.getString("socketPair"), temp.getInt("step"), temp.getString("query"), sender);
			else
				success = setGraph(temp.getString("graphID"), temp.getString("socketPair"), temp.getInt("step"), temp.getDouble("maxValue"), temp.getString("query"), sender);
			plugin.getLogger().info(temp.getString("graphID") + " " + String.valueOf(success));
			partialSuccess = partialSuccess || success;
		}
		
		for (int i = 0; i < histograms.length(); i++) {
			temp = histograms.getJSONObject(i);
			//if (temp.get("maxValue").equals("auto"))
				//success = setHistogram(temp.getString("histogramID"), temp.getString("socketPair"), temp.getString("query"), sender);
			//else
			success = setHistogram(temp.getString("histogramID"), temp.getString("socketPair"), temp.getDouble("minValue"), temp.getDouble("maxValue"), temp.getString("query"), sender);
			plugin.getLogger().info(temp.getString("histogramID") + " " + String.valueOf(success));
			partialSuccess = partialSuccess || success;
		}
		
		return partialSuccess;
	}
	
	/**
	 * export all current clocks and graphs to a JSON config file located in path
	 * JSON should look like:
	 * { 
	 *     "dashboards":
	 * 	   {
	 * 			"clocks": 
	 * 			[
	 * 				{
	 * 				"clockID": "XY",
	 * 				"socketPair": "ipOrAddress:port",
	 * 				"maxValue": [double] x,
	 * 				"query": "promQLQuery{}"
	 * 				},
	 * 				...
	 * 			],
	 * 			"graphs": 
	 * 			[
	 * 				{
	 * 				"graphID": "XY",
	 * 				"socketPair": "ipOrAddress:port",
	 * 				"step": [int] timeInSeconds,
	 * 				"maxValue": [double] x,
	 * 				"query": "promQLQuery{}"
	 * 				},
	 * 				...
	 * 			]
	 * 	   }
	 * }
	 */
	public void exportFile(String path) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>(); // The complete JSON document
		Map<String, Object> temp;
		ArrayList<Object> clocks = new ArrayList<Object>();
		ArrayList<Object> graphs = new ArrayList<Object>();
		ArrayList<Object> histograms = new ArrayList<Object>();
		
		for (String clockID : clockDashboard.clocks.keySet()) {
			temp = new HashMap<String, Object>();
			temp.put("clockID", clockID);
			temp.put("socketPair", clockDashboard.clocks.get(clockID).getSocketPair());
			temp.put("maxValue", clockDashboard.clocks.get(clockID).maxValue);
			temp.put("query", clockDashboard.clocks.get(clockID).getRawQuery());
			clocks.add(temp);
		}
		
		for (String graphID : graphDashboard.graphs.keySet()) {
			temp = new HashMap<String, Object>();
			temp.put("graphID", graphID);
			temp.put("socketPair", graphDashboard.graphs.get(graphID).getSocketPair());
			temp.put("step", graphDashboard.graphs.get(graphID).step);
			temp.put("maxValue", graphDashboard.graphs.get(graphID).maxValue);
			temp.put("query", graphDashboard.graphs.get(graphID).getRawQuery());
			graphs.add(temp);
		}
		
		for (String histogramID : histogramDashboard.histograms.keySet()) {
			temp = new HashMap<String, Object>();
			temp.put("histogramID", histogramID);
			temp.put("socketPair", histogramDashboard.histograms.get(histogramID).getSocketPair());
			temp.put("minValue", histogramDashboard.histograms.get(histogramID).minValue);
			temp.put("maxValue", histogramDashboard.histograms.get(histogramID).maxValue);
			temp.put("query", histogramDashboard.histograms.get(histogramID).getRawQuery());
			histograms.add(temp);
		}
		
		temp = new HashMap<String, Object>();
		temp.put("clocks", clocks);
		temp.put("graphs", graphs);
		temp.put("histograms", histograms);
		map.put("dashboards", temp);
		String json = (new JSONObject(map)).toString();
		FileWriter myWriter = new FileWriter(path);
		myWriter.write(json);
		myWriter.close();
	}
}
