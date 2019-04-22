package com.gestankbratwurst.PowerTurrets.util;

import java.util.Comparator;

import org.bukkit.util.Vector;

public final class VectorDistanceComparator implements Comparator<Vector>{
	
	public VectorDistanceComparator(Vector base) {
		this.base = base;
	}
	
	private final Vector base;

	@Override
	public int compare(Vector vec1, Vector vec2) {
		double dist1 = vec1.distanceSquared(this.base);
		double dist2 = vec2.distanceSquared(this.base);
		if(dist1 > dist2) {
			return 1;
		}else if(dist1 < dist2) {
			return -1;
		}
		return 0;
	}
}
