package io.github.ravidshachar.craftana;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import static io.github.ravidshachar.craftana.Constants.*;
import static io.github.ravidshachar.craftana.Methods.*;

import java.text.DecimalFormat;

/**
 * Physical representaion of a string in Minecraft
 */
public class BlockString {
	String str; //the string to be displayed
	public Vector leftCorner; //bottom left corner of the blockString canvas
	Vector start; //the bottom left corner of the first blockString character
	int diffV; //distance between two vertical panels
	Boolean isX; //if true the blockString should be drawn to the X axis, otherwise to the Z axis
	
	public BlockString(String str, Vector leftCoords, int diffV, Boolean isX) {
		this.str = shorten(str);
		this.diffV = diffV;
		this.leftCorner = leftCoords.add(isX ? (maxText - 1) / -2 - 1 : 0, diffV - 7, isX ? 0 : (maxText - 1) / -2 - 1);
		this.start = leftCorner.add(isX ? (maxText - length(this.str)) / 2 : 0, 0, isX ? 0 : (maxText - length(this.str)) / 2);
		this.isX = isX;
	}
	
	public int length() {
		int length = str.length() - 1;
		for (char c : str.toCharArray()) {
			length += charLength(c);
		}
		return length;
	}
	
	public int length(String s) {
		int length = s.length() - 1;
		for (char c : s.toCharArray()) {
			length += charLength(c);
		}
		return length;
	}
	
	private static String repeat(int count, String with) {
	    return new String(new char[count]).replace("\0", with);
	}
	
	public String shorten(String s) {
		double num = Double.parseDouble(s);
		int factor = (String.valueOf((int) num)).length();
		DecimalFormat df;
		if (num < 1000) {
			df = new DecimalFormat(repeat(factor, "0") + "." + repeat(4 - factor, "#"));
			return df.format(num);
		}
		if (num < 1000000) 
			return shorten(String.valueOf(num / 1000)) + "K";
		if (num < 1000000000)
			return shorten(String.valueOf(num / 1000000)) + "M";
		if (num / 1000 < 1000000000)
			return shorten(String.valueOf(num / 1000000000)) + "B";
		df = new DecimalFormat("0.###E0");
		return df.format(num);
	}

	/**
	 * @param c some character
	 * @return c's length
	 */
	public int charLength(char c) {
		int charLength = -1;
		if (c == '1')
			charLength = 1;
		else if (c == '.')
			charLength = 1;
		else if (c >= '2' && c <= '9')
			charLength = 3;
		else if (c == '0')
			charLength = 3;
		else if (c == 'A' || c == 'C' || c == 'E' || c == 'e')
			charLength = 3;
		else if (c == 'B' || c == 'D' || c == 'K')
			charLength = 4;
		else if (c == 'M')
			charLength = 5;
		return charLength;
	}
	
	public void clearString() {
		drawRect(this.leftCorner,this.leftCorner.add(isX ? diffV : 0, 4, isX ? 0 : diffV),Material.AIR);
	}
	
	public void drawString() {
		if (length() > maxText) {
			if (length("ERR0R") <= maxText)
				drawString("ERR0R");
			else if (length("ERR") <= maxText)
				drawString("ERR");
			else
				Bukkit.getServer().broadcastMessage("ERROR DRAWING STRING");
			return;
			
		}
		for (char c : str.toCharArray()) {
			drawChar(c);
			start = start.add(isX ? charLength(c) + 1 : 0, 0, isX ? 0 : charLength(c) + 1);
		}
	}
	
	public void drawString(String s) {
		if (length() > maxText) {
			if (length("ERR0R") <= maxText)
				drawString("ERR0R");
			else if (length("ERR") <= maxText)
				drawString("ERR");
			else
				Bukkit.getServer().broadcastMessage("ERROR DRAWING STRING");
			return;
			
		}
		for (char c : s.toCharArray()) {
			drawChar(c);
			start = start.add(isX ? charLength(c) + 1 : 0, 0, isX ? 0 : charLength(c) + 1);
		}
	}
	
	public void drawChar(char c) {
		if ((c >= '0' && c <= '9') || c == '.')
			drawChar(c, textMat);
		else
			drawChar(c, specialMat);
	}
	
	/**
	 * 
	 * @param start in-game coords of the leftmost bottom block of the character
	 */
	public void drawChar(char c, Material mat) {
		int charLength = charLength(c);
		Block b = null;
		if (charLength == -1)
			return;
		switch (c) {
			case '.':
				b = (new Location(Bukkit.getServer().getWorld("world"), start.getX(), start.getY(), start.getZ())).getBlock();
				b.setType(mat);
				break;
			case '1':
				drawLine(start, start.add(0, 4, 0), mat);
				break;
			case '2':
				for (int i = 0; i < 5; i+=2) {
					drawLine(start.add(0, i, 0), start.add(isX ? 2 : 0, i, isX ? 0 : 2), mat);
				}
				b = (new Location(Bukkit.getServer().getWorld("world"), start.getX(), start.getY() + 1, start.getZ())).getBlock();
				b.setType(mat);
				b = (new Location(Bukkit.getServer().getWorld("world"), start.getX() + 2, start.getY() + 3, start.getZ())).getBlock();
				b.setType(mat);
				break;
			case '3':
				for (int i = 0; i < 5; i+=2) {
					drawLine(start.add(0, i, 0), start.add(isX ? 2 : 0, i, isX ? 0 : 2), mat);
				}
				b = (new Location(Bukkit.getServer().getWorld("world"), start.getX() + (isX ? 2 : 0), start.getY() + 1, start.getZ() +  + (isX ? 0 : 2))).getBlock();
				b.setType(mat);
				b = (new Location(Bukkit.getServer().getWorld("world"), start.getX() + (isX ? 2 : 0), start.getY() + 3, start.getZ() +  + (isX ? 0 : 2))).getBlock();
				b.setType(mat);
				break;
			case '4':
				drawLine(start.add(0, 2, 0), start.add(0, 4, 0), mat);
				drawLine(start.add(isX ? 2 : 0, 0, isX ? 0 : 2), start.add(isX ? 2 : 0, 4, isX ? 0 : 2), mat);
				b = (new Location(Bukkit.getServer().getWorld("world"), start.getX() + (isX ? 1 : 0), start.getY() + 2, start.getZ() +  + (isX ? 0 : 1))).getBlock();
				b.setType(mat);
				break;
			case '5':
				for (int i = 0; i < 5; i+=2) {
					drawLine(start.add(0, i, 0), start.add(isX ? 2 : 0, i, isX ? 0 : 2), mat);
				}
				b = (new Location(Bukkit.getServer().getWorld("world"), start.getX(), start.getY() + 3, start.getZ())).getBlock();
				b.setType(mat);
				b = (new Location(Bukkit.getServer().getWorld("world"), start.getX() + (isX ? 2 : 0), start.getY() + 1, start.getZ() + (isX ? 0 : 2))).getBlock();
				b.setType(mat);
				break;
			case '6':
				drawChar('5', mat);
				drawChar('1', mat);
				break;
			case '7':
				drawLine(start.add(0, 4, 0), start.add(isX ? 2 : 0, 4, isX ? 0 : 2), mat);
				drawLine(start.add(isX ? 2 : 0, 0, isX ? 0 : 2), start.add(isX ? 2 : 0, 4, isX ? 0 : 2), mat);
				break;
			case '8':
				drawChar('1', mat);
				drawChar('3', mat);
				break;
			case '9':
				drawChar('4', mat);
				b = (new Location(Bukkit.getServer().getWorld("world"), start.getX() + (isX ? 1 : 0), start.getY() + 4, start.getZ() + (isX ? 0 : 1))).getBlock();
				b.setType(mat);
				break;
			case '0':
				drawChar('7', mat);
				drawChar('C', mat);
				break;
			case 'A':
				drawChar('1', mat);
				drawChar('9', mat);
				break;
			case 'B':
				drawChar('E', mat);
				drawLine(start.add(isX ? 3 : 0, 1, isX ? 0 : 3), start.add(isX ? 3 : 0, 3, isX ? 0 : 3), mat);
				b = (new Location(Bukkit.getServer().getWorld("world"), start.getX() + (isX ? 3 : 0), start.getY() + 2, start.getZ() + (isX ? 0 : 3))).getBlock();
				b.setType(Material.AIR);
				break;
			case 'C':
				drawLine(start, start.add(0, 4, 0), mat);
				drawLine(start, start.add(isX ? 2 : 0, 0, isX ? 0 : 2), mat);
				drawLine(start.add(0, 4, 0), start.add(isX ? 2 : 0, 4, isX ? 0 : 2), mat);
				break;
			case 'D':
				drawChar('C', mat);
				drawLine(start.add(isX ? 3 : 0, 1, isX ? 0 : 3), start.add(isX ? 3 : 0, 3, isX ? 0 : 3), mat);
				break;
			case 'E':
				drawChar('C', mat);
				drawLine(start.add(0, 2, 0), start.add(isX ? 2 : 0, 2, isX ? 0 : 2), mat);
				break;
			case 'K':
				drawChar('1', mat);
				(new Location(Bukkit.getServer().getWorld("world"), 
							  start.getX() + (isX ? 1 : 0), 
							  start.getY() + 2, 
							  start.getZ() + (isX ? 0 : 1))
				).getBlock().setType(mat);
				(new Location(Bukkit.getServer().getWorld("world"), 
						  start.getX() + (isX ? 2 : 0), 
						  start.getY() + 1, 
						  start.getZ() + (isX ? 0 : 2))
				).getBlock().setType(mat);
				(new Location(Bukkit.getServer().getWorld("world"), 
						  start.getX() + (isX ? 2 : 0), 
						  start.getY() + 3, 
						  start.getZ() + (isX ? 0 : 2))
				).getBlock().setType(mat);
				(new Location(Bukkit.getServer().getWorld("world"), 
						  start.getX() + (isX ? 3 : 0), 
						  start.getY(), 
						  start.getZ() + (isX ? 0 : 3))
				).getBlock().setType(mat);
				(new Location(Bukkit.getServer().getWorld("world"), 
						  start.getX() + (isX ? 3 : 0), 
						  start.getY() + 4, 
						  start.getZ() + (isX ? 0 : 3))
				).getBlock().setType(mat);
				break;
			case 'M':
				drawLine(start, start.add(0, 4, 0), mat);
				(new Location(Bukkit.getServer().getWorld("world"), 
						  start.getX() + (isX ? 1 : 0), 
						  start.getY() + 3, 
						  start.getZ() + (isX ? 0 : 1))
				).getBlock().setType(mat);
				(new Location(Bukkit.getServer().getWorld("world"), 
						  start.getX() + (isX ? 2 : 0), 
						  start.getY() + 2, 
						  start.getZ() + (isX ? 0 : 2))
				).getBlock().setType(mat);
				(new Location(Bukkit.getServer().getWorld("world"), 
						  start.getX() + (isX ? 3 : 0), 
						  start.getY() + 3, 
						  start.getZ() + (isX ? 0 : 3))
				).getBlock().setType(mat);
				drawLine(start.add(isX ? 4 : 0, 0, isX ? 0 : 4), start.add(isX ? 4 : 0, 4, isX ? 0 : 4), mat);
				break;
		}
	}
}
