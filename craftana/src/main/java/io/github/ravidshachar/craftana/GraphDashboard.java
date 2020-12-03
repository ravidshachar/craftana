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
	 * graph setter with static max value
	 */
	public void setGraph(String graphID, String socketPair, String query, int step, double maxValue) {
		if (graphs.get(graphID) == null) {
			MCExporter.graphAmount.inc();
		}
		Vector leftCoords = firstCoords.add(getLeftCoords(graphID));
		graphs.put(graphID, new Graph(plugin, leftCoords, socketPair, query, step, maxValue, isX));
		graphs.get(graphID).clearGraph();
	}
	
	/**
	 * graph setter with auto max value
	 */
	public void setGraph(String graphID, String socketPair, String query, int step) {
		if (graphs.get(graphID) == null) {
			MCExporter.graphAmount.inc();
		}
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
		return new Vector(isX ? (letter - 'I') * diffHorizontal : 0, (number - '1') * diffVertical, isX ? 0 : (letter - 'I') * diffHorizontal);
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
		for (char i='I'; i <= 'K';i++) {
			for (char j='1'; j <= '2';j++) {
				graphID = new StringBuilder().append(i).append(j).toString();
				removeGraph(graphID);
			}
		}
		MCExporter.graphAmount.set(0);
	}
}
