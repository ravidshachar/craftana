package io.github.ravidshachar.craftana;

import org.bukkit.Material;

public final class Constants {
	public static final int radius = 15; // clock radius
	public static final int diffX = 38; // clock diffX
	public static final int diffY = 26; // clock diffY
	public static final int maxText = 34; // max text width
	public static final int steps = 20; // graph steps
	public static final int graphWidth = (steps - 1) * 2;
	public static final int graphHeight = 40;
	public static final int buckets = 10; // amount of histogram buckets
	public static final int histogramWidth = 28;
	public static final int histogramHeight = 40;
	public static final int histogramDir = -1; // direction for the histogram
	public static final Vector firstArrowCoords = new Vector(197, 56, -75); //coords for the left arrow startpoint
	public static final Vector firstTextCoords = new Vector(197, 80, -75); //coords for the left corner of the first text canvas
	public static final Vector firstGraphCoords = new Vector(332, 44, -68);  //coords for the bottom left corner of the first graph
	public static final Vector firstHistogramCoords = new Vector(177, 44, 50);
	public static Material textMat = Material.LIGHT_GRAY_CONCRETE;
	public static Material specialMat = Material.PURPLE_CONCRETE;
	public static Material clockMat = Material.WHITE_STAINED_GLASS;
	public static Material graphMat = Material.WHITE_STAINED_GLASS;
	public static Material histogramMat = Material.WHITE_STAINED_GLASS;
}
