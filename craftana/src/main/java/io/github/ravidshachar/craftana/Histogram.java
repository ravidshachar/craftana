package io.github.ravidshachar.craftana;

import static io.github.ravidshachar.craftana.Constants.*;
import static io.github.ravidshachar.craftana.Methods.*;

import java.io.IOException;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;

public class Histogram extends Panel {
	Plugin plugin;
	Vector leftCoords;
	int bucketAmounts[];
	double minValue;
	double maxValue;
	Boolean isX;
	Boolean autoMinMax;
	Long timestamp = -1L;
	
	/**
	 * Constructor with static max
	 */
	public Histogram(Plugin plugin, Vector leftCoords, String socketPair, String query, double minValue, double maxValue, Boolean isX) {
		super(socketPair, query);
		this.plugin = plugin;
		this.leftCoords = leftCoords;
		this.bucketAmounts = new int[buckets];
		Arrays.fill(bucketAmounts, 0);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.isX = isX;
		this.autoMinMax = false;
	}
	
	/**
	 * Constructor with auto max
	 */
	public Histogram(Plugin plugin, Vector leftCoords, String socketPair, String query, Boolean isX) {
		super(socketPair, query);
		this.plugin = plugin;
		this.leftCoords = leftCoords;
		this.minValue = -1;
		this.maxValue = -1;
		this.isX = isX;
		this.autoMinMax = true;
	}
	
	/**
	 * clears histogram
	 */
	public void clearHistogram() {
		drawRect(leftCoords, leftCoords.add(isX ? histogramDir * histogramWidth : 0, histogramHeight, isX ? 0 : histogramDir * histogramWidth), Material.AIR);
	}
	
	/**
	 * receive an int array with a length of <steps> and display it
	 */
	public void drawHistogram() {
		Vector currCoords = leftCoords;
		int height; // temp var for the visualised height
		int maxBucket = arrayMax(bucketAmounts);
		int sumOfBuckets = arraySum(bucketAmounts);
		clearHistogram();
		for (int i = 0; i < buckets; i++) {
			// After many formulation attempts, I found this formula to work quite nicely
			height = (int) (histogramHeight * (bucketAmounts[i] * maxBucket) / (Math.pow(maxBucket, 2) + sumOfBuckets));
			drawRect(currCoords, currCoords.add(isX ? histogramDir : 0, height, isX ? 0 : histogramDir), histogramMat);
			currCoords = currCoords.add(isX ? 3 * histogramDir : 0, 0, isX ? 0 : 3 * histogramDir);
		}
	}
	
	/**
	 * displays the query starting with the left coords
	 * @TODO FIX AUTO MINMAX
	 */
	public void displayQuery() throws JSONException, IOException {
		Long lastQueryTime = (long) Double.parseDouble(this.lastQueryTime());
		Double lastQueryVal = Double.parseDouble(this.Query());
		if (lastQueryTime == timestamp)
			return;
		timestamp = lastQueryTime;
		// if our query value is greater than the max value, increment the last bucket and update
		// maxValue as well as redo bucket amounts
		if (lastQueryVal >= maxValue) {
			bucketAmounts[buckets - 1]++;
			if (autoMinMax) {
				maxValue = lastQueryVal;
				// ***RedoBucketAmounts()***
			}
			return;
		}
		// the range that matters is the range (minValue, maxValue), this calculation finds
		// what bucket our query belongs to
		bucketAmounts[(int) (buckets * (lastQueryVal - minValue) / (maxValue - minValue))]++;
		// ***cleanHistogram()***
		drawHistogram();
	}
}
