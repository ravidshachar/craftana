package io.github.ravidshachar.craftana;

import org.bukkit.plugin.Plugin;

import static io.github.ravidshachar.craftana.Constants.*;

public class GraphDashboard extends Dashboard<Graph> {
	static int diffHorizontal = graphWidth + 2;
	static int diffVertical = -1 * (graphHeight + 1);
	
	public GraphDashboard(Plugin plugin, Vector firstCoords, Boolean isX) {
		super(plugin, diffHorizontal, diffVertical, isX, firstCoords);
	}
	
	/**
	 * graph setter with static max value
	 */
	public void setGraph(String graphID, String socketPair, String query, int step, double maxValue) {
		if (panels.get(graphID) == null) {
			MCExporter.graphAmount.inc();
		}
		Vector leftCoords = firstCoords.add(getLeftCoords(graphID));
		panels.put(graphID, new Graph(plugin, leftCoords, socketPair, query, step, maxValue, isX));
		panels.get(graphID).clearPanel();
	}
	
	/**
	 * graph setter with auto max value
	 */
	public void setGraph(String graphID, String socketPair, String query, int step) {
		if (panels.get(graphID) == null) {
			MCExporter.graphAmount.inc();
		}
		Vector leftCoords = firstCoords.add(getLeftCoords(graphID));
		panels.put(graphID, new Graph(plugin, leftCoords, socketPair, query, step, isX));
		panels.get(graphID).clearPanel();
	}
	
	/*public void removePanel(String graphID) {
		if (graphs.get(graphID) == null)
			return;
		Graph tempGraph = graphs.remove(graphID);
		tempGraph.clearPanel();
	}*/
	
	protected Vector getLeftCoords(String graphID) {
		char letter = graphID.charAt(0);
		char number = graphID.charAt(1);
		return new Vector(isX ? (letter - 'I') * diffHorizontal : 0, (number - '1') * diffVertical, isX ? 0 : (letter - 'I') * diffHorizontal);
	}
	
	/*public void updateDashboard() throws NumberFormatException, JSONException, IOException {
		for (Graph graph : graphs.values()) {
			graph.displayQuery();
		}
	}*/
	
	/**
	 * This function clears current graphs as long as they're registered
	 */
	public void clearDashboard() {
		String graphID;
		for (char i='I'; i <= 'K';i++) {
			for (char j='1'; j <= '2';j++) {
				graphID = new StringBuilder().append(i).append(j).toString();
				removePanel(graphID);
			}
		}
		MCExporter.graphAmount.set(0);
	}
}
