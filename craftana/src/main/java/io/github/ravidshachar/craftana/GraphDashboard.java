package io.github.ravidshachar.craftana;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.plugin.Plugin;
import org.json.JSONException;

import static io.github.ravidshachar.craftana.Constants.*;

public class GraphDashboard {
	Plugin plugin;
	HashMap<String, Graph> graphs; // String graphID : Graph graphObject
	int diffHorizontal;
	int diffVertical;
	Boolean isX;
	Vector firstCoords;
	
	public GraphDashboard(Plugin plugin, Vector firstCoords, Boolean isX) {
		this.plugin = plugin;
		graphs = new HashMap<String, Graph>();
		this.diffHorizontal = graphWidth + 2;
		this.diffVertical = -1 * (graphHeight + 1);
		this.firstCoords = firstCoords;
		this.isX = isX;
	}
	
	/**
	 * graph setter with static threshold
	 */
	public void setGraph(String graphID, String socketPair, String query, int step, double threshold) {
		Vector leftCoords = firstCoords.add(getLeftCoords(graphID));
		graphs.put(graphID, new Graph(plugin, leftCoords, socketPair, query, step, threshold, isX));
		graphs.get(graphID).clearGraph();
	}
	
	/**
	 * graph setter with auto threshold
	 */
	public void setGraph(String graphID, String socketPair, String query, int step) {
		Vector leftCoords = firstCoords.add(getLeftCoords(graphID));
		graphs.put(graphID, new Graph(plugin, leftCoords, socketPair, query, step, isX));
		graphs.get(graphID).clearGraph();
	}
	
	public void removeGraph(String graphID) {
		if (graphs.get(graphID) == null)
			return;
		Graph tempGraph = graphs.remove(graphID);
		tempGraph.clearGraph();
	}
	
	private Vector getLeftCoords(String graphID) {
		char letter = graphID.charAt(0);
		char number = graphID.charAt(1);
		return new Vector(isX ? (letter - 'F') * diffHorizontal : 0, (number - '1') * diffVertical, isX ? 0 : (letter - 'F') * diffHorizontal);
	}
	
	public void updateDashboard() throws NumberFormatException, JSONException, IOException {
		for (Graph graph : graphs.values()) {
			graph.displayQuery();
		}
	}
	
	/**
	 * This function clears current graphs as long as they're registered
	 */
	public void clearDashboard() {
		String graphID;
		for (char i='F'; i <= 'H';i++) {
			for (char j='1'; j <= '2';j++) {
				graphID = new StringBuilder().append(i).append(j).toString();
				removeGraph(graphID);
			}
		}
	}
}
