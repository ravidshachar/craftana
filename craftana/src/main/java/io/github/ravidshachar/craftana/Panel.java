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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Panel {
	private String socketPair; //the prometheus socket pair
	private String query;
	
	public Panel(String socketPair, String query) {
		this.socketPair = String.format("http://%s/api/v1", socketPair);
		this.socketPair = socketPair;
		this.query = query;
	}
	
	public String getSocketPair() {
		return socketPair;
	}
	
	/**
	 * This function receives a query string as a parameter and return the first value found in the 
	 * RESTAPI response
	 */
	public String Query() throws IOException, JSONException {
		Bukkit.getLogger().info(formatURL().toString());
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
			return new URL("http", 
						   socketPair.substring(0, socketPair.indexOf(":")), 
						   Integer.parseInt(socketPair.substring(socketPair.indexOf(":") + 1, socketPair.length())), 
						   "/api/v1/query?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
						  );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
