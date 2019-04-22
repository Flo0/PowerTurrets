package com.gestankbratwurst.PowerTurrets.util;

import java.util.Comparator;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public final class LivingEntityDistanceComparator implements Comparator<LivingEntity>{

	public LivingEntityDistanceComparator(Location base) {
		this.base = base;
	}

	private final Location base;

	@Override
	public int compare(LivingEntity ent1, LivingEntity ent2) {
		double dist1 = ent1.getLocation().distanceSquared(this.base);
		double dist2 = ent2.getLocation().distanceSquared(this.base);
		if(dist1 > dist2) {
			return 1;
		}else if(dist1 < dist2) {
			return -1;
		}
		return 0;
	}
}
