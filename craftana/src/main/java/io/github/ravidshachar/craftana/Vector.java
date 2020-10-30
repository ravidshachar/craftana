package io.github.ravidshachar.craftana;

public class Vector {
	/**
	 * A simple 3d vector class for craftana
	 */
	  private final int x;
	  private final int y;
	  private final int z;

	  public Vector(int x, int y, int z) {
	    this.x = x;
	    this.y = y;
	    this.z = z;
	  }
	  
	  public Vector() {
		  this.x = 0;
		  this.y = 0;
		  this.z = 0;
	  }

	  public int getX() {
		  return this.x;
	  }
	  
	  public int getY() {
		  return this.y;
	  }
	  
	  public int getZ() {
		  return this.z;
	  }

	  public Vector add(Vector addend) {
	    return new Vector(x + addend.x, y + addend.y, z + addend.z);
	  }
	  
	  public Vector add(int addedx, int addedy, int addedz) {
		    return new Vector(x + addedx, y + addedy, z + addedz);
	  }
	  
	  public Vector sub(Vector subbed) {
		  return new Vector(x - subbed.x, y - subbed.y, z - subbed.z);
	  }
	  
	  public Vector sub(int subbedx, int subbedy, int subbedz) {
		  return new Vector(x - subbedx, y - subbedy, z - subbedz);
	  }
	  
	  @Override
	  public String toString() {
		  return String.format("(" + x + ", " + y + ", " + z + ")");
	  }
}