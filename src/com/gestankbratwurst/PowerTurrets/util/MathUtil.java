package com.gestankbratwurst.PowerTurrets.util;

public class MathUtil {
	
	public static String ProgressBar(double current, double max, int length, String fragment) {
		String bar = "";
		
		int fullfrags = (int) ((length / max) * current);
		int currentLenght = fullfrags;
		
		bar += "§a";
		while(fullfrags > 0) {
			bar += fragment;
			fullfrags--;
		}
		
		bar += "§c";
		while(currentLenght < length) {
			bar += fragment;
			currentLenght++;
		}

		return bar;
	}
	
}
