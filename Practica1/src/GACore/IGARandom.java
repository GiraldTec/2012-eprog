package GACore;

import java.util.Random;

public class IGARandom {	
	private static Random rand = new Random();
	
	public static void setSeed(long seed) {
		rand = new Random(seed);
	}
	
	public static boolean getRBoolean() { return rand.nextBoolean();}
	public static int getRInt() { return rand.nextInt();}
	public static int getRInt(int max) { return rand.nextInt(max);}
	public static double getRDouble() { return rand.nextDouble();}
	
}
