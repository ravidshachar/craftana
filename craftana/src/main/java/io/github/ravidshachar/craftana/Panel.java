package io.github.ravidshachar.craftana;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Panel {
	protected String socketPair; //the prometheus socket pair
	protected String query;
	Plugin plugin;
	Vector leftCoords;
	double maxValue;
	Boolean isX;
	Boolean autoBoundaries;
	
	public Panel(Plugin plugin, Vector leftCoords, String socketPair, String query, double maxValue, Boolean isX) {
		//this.socketPair = String.format("http://%s/api/v1", socketPair);
		this.plugin = plugin;
		this.leftCoords = leftCoords;
		this.socketPair = socketPair;
		this.query = query;
		this.maxValue = maxValue;
		this.isX = isX;
		this.autoBoundaries = false;
	}
	
	public Panel(Plugin plugin, Vector leftCoords, String socketPair, String query, Boolean isX) {
		//this.socketPair = String.format("http://%s/api/v1", socketPair);
		this.plugin = plugin;
		this.leftCoords = leftCoords;
		this.socketPair = socketPair;
		this.query = query;
		this.maxValue = -1;
		this.isX = isX;
		this.autoBoundaries = false;
	}
	
	public String getSocketPair() {
		return socketPair;
	}
	
	public String getRawQuery() {
		return query;
	}
	
	public String lastQueryTime() throws IOException, JSONException {
		JSONObject json = readJsonFromUrl(formatURL());
		if (!json.getString("status").equals("success")) {
			return "status: " + json.getString("status") + "\n" + Boolean.toString(json.getString("status").equals("success"));
		}
		JSONArray result = json.getJSONObject("data").getJSONArray("result");
		if (result == null || result.length() == 0) {
			return "result: " + result.toString();
		}
		return result.getJSONObject(0).getJSONArray("value").getBigDecimal(0).toPlainString();
	}
	
	/**
	 * This function uses the query string attribute and return the first value found in the 
	 * RESTAPI response
	 */
	public String Query() throws IOException, JSONException {
		JSONObject json = readJsonFromUrl(formatURL());
		if (!json.getString("status").equals("success")) {
			return "status: " + json.getString("status") + "\n" + Boolean.toString(json.getString("status").equals("success"));
		}
		JSONArray result = json.getJSONObject("data").getJSONArray("result");
		if (result == null || result.length() == 0) {
			return "result: " + result.toString();
		}
		return result.getJSONObject(0).getJSONArray("value").getString(1);
	}
	
	
	/**
	 * This function receives step and returns
	 * the query range from steps * step time ago until now
	 */
	public String[] RangeQuery(int step, int numOfSteps) throws IOException, JSONException {
		return RangeQuery(Long.toString(System.currentTimeMillis() / 1000L - numOfSteps * step), Long.toString(System.currentTimeMillis() / 1000L), step + "s");
	}
	
	/**
	 * This function receives step and end timestamp and returns
	 * the query range from steps * step time ago until end timestamp
	 */
	public String[] RangeQuery(Long end, int step, int numOfSteps) throws IOException, JSONException {
		return RangeQuery(Long.toString(end - (numOfSteps + 1) * step), Long.toString(end), step + "s");
	}
	
	/**
	 * This function receives start and end timestamps and
	 * returns the query range as an array
	 */
	public String[] RangeQuery(String start, String end, String step) throws IOException, JSONException {
		JSONObject json = readJsonFromUrl(formatURL(start, end, step));
		if (!json.getString("status").equals("success")) {
			Bukkit.getLogger().info("status: " + json.getString("status") + "\n" + Boolean.toString(json.getString("status").equals("success")));
			return null;
		}
		JSONArray result = json.getJSONObject("data").getJSONArray("result");
		if (result == null || result.length() == 0) {
			Bukkit.getLogger().info("result: " + result.toString());
			return null;
		}
		JSONArray values = result.getJSONObject(0).getJSONArray("values");
		String[] values_strings = new String[values.length()];
		for (int i = 0; i < values.length(); i++) {
			values_strings[i] = values.getJSONArray(i).getString(1);
		}
		return values_strings;
	}
	
	public abstract void displayQuery() throws IOException, JSONException, NumberFormatException;
	
	public abstract void clearPanel();
	
	/**
	 * this method returns the minimum value over the last 10 minutes
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public double getMinValue() throws JSONException, IOException {
		String[] values = RangeQuery(Long.toString(System.currentTimeMillis() / 1000L - 600L), Long.toString(System.currentTimeMillis() / 1000L), "5s");
		double min = Double.parseDouble(values[0]);
		for (int i = 1; i < values.length; i++) {
			min = Math.min(min, Double.parseDouble(values[i]));
		}
		return min;
	}
	
	/**
	 * this method returns the maximum value over the last 10 minutes
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public double getMaxValue() throws JSONException, IOException {
		String[] values = RangeQuery(Long.toString(System.currentTimeMillis() / 1000L - 600L), Long.toString(System.currentTimeMillis() / 1000L), "5s");
		double max = 0;
		for (int i = 0; i < values.length; i++) {
			max = Math.max(max, Double.parseDouble(values[i]));
		}
		return max;
	}
	
	/**
	 * Helper method to readJsonFromUrl(), basically builds a string from the BufferedReader used in
	 * readJsonFromUrl()
	 */
	private String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	/**
	 * This mehod gets a URL object as an input and sends an HTTP GET request, assuming it's a
	 * RESTAPI app (which obviously prometheus is) it returns the JSON response as a JSONObject
	 */
	private JSONObject readJsonFromUrl(URL url) throws IOException, JSONException {
		InputStream is = url.openStream();
    	try {
    		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	    	String jsonText = readAll(rd);
	    	JSONObject json = new JSONObject(jsonText);
	    	return json;
    	}
    	finally {
	    	is.close();
		}
	}
	/**
	* This method formats the URL with the prometheus' socket pair and the query,
	* also encodes the query correctly as standard UTF_8 characters
	*/
	private URL formatURL() {
		try {
			//return new URL(String.format("http://%s/api/v1/query?query=", socketPair) + URLEncoder.encode(query, StandardCharsets.UTF_8.toString()));
			if (socketPair.indexOf(":") > 0) {
				return new URL("http", 
						   	   socketPair.substring(0, socketPair.indexOf(":")), 
						   	   Integer.parseInt(socketPair.substring(socketPair.indexOf(":") + 1, socketPair.length())), 
						   	   "/api/v1/query?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
						  	  );
			}
			else {
				return new URL("http", 
					   	   socketPair, 
					   	   9090, 
					   	   "/api/v1/query?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
					  	  );
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * same as format URL but for range_queries with time in format <YYYY>-<MM>-<DD>T<HH>:<mm>:<ss>Z or as timestamp
	 */
	private URL formatURL(String start, String end, String step) {
		try {
			//return new URL(String.format("http://%s/api/v1/query?query=", socketPair) + URLEncoder.encode(query, StandardCharsets.UTF_8.toString()));
			if (socketPair.indexOf(":") > 0) {
				return new URL("http", 
						   	   socketPair.substring(0, socketPair.indexOf(":")), 
						       Integer.parseInt(socketPair.substring(socketPair.indexOf(":") + 1, socketPair.length())), 
						       "/api/v1/query_range?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8.toString()) + 
						       "&start=" + start +
						       "&end=" + end +
						       "&step=" + URLEncoder.encode(step, StandardCharsets.UTF_8.toString())
						      );
			}
			else {
				return new URL("http",
						socketPair,
						9090,
						"/api/v1/query_range?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8.toString()) + 
						"&start=" + start +
						"&end=" + end +
						"&step=" + URLEncoder.encode(step, StandardCharsets.UTF_8.toString())
						);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
