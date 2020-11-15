package io.github.ravidshachar.craftana;

import org.bukkit.Material;

public final class Constants {
	public static final int radius = 15; //clock radius
	public static final int diffX = 38;
	public static final int diffY = 26;
	public static final int maxText = 34;
	public static final int steps = 20;
	public static final int graphWidth = (steps - 1) * 2;
	public static final int graphHeight = 40;
	public static final Vector firstArrowCoords = new Vector(159, 56, -75); //coords for the left arrow startpoint
	public static final Vector firstTextCoords = new Vector(159, 80, -75); //coords for the left corner of the first text canvas
	public static final Vector firstGraphCoords = new Vector(332, 3, -68);  //coords for the bottom left corner of the first graph
	public static Material textMat = Material.LIGHT_GRAY_CONCRETE;
	public static Material graphMat = Material.LIGHT_GRAY_CONCRETE;
	public static Material specialMat = Material.PURPLE_CONCRETE;
}
