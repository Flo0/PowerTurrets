package com.gestankbratwurst.PowerTurrets.util;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface BulletImpact {
	
	public void onImpactEntity(LivingEntity target);
	public void onImpactBlock(Location loc);
	
}
