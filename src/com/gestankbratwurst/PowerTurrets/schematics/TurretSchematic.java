package com.gestankbratwurst.PowerTurrets.schematics;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

public abstract class TurretSchematic {
	
	public TurretSchematic(Location baseLocation) {
		this.baseLocation = baseLocation;
	}
	
	protected final Location baseLocation;
	
	public void buildUp() {
		this.getStructure().forEach((loc,mat)->loc.getBlock().setType(mat));
	}
	
	public void tearDown() {
		this.getStructure().keySet().forEach(loc->loc.getBlock().setType(Material.AIR));
	}
	
	public abstract Map<Location, Material> getStructure();
}
