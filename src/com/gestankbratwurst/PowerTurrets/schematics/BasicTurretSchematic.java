package com.gestankbratwurst.PowerTurrets.schematics;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

import com.google.common.collect.Maps;

public class BasicTurretSchematic extends TurretSchematic{

	public BasicTurretSchematic(Location baseLocation) {
		super(baseLocation);
		this.buildMap = Maps.newHashMap();
		this.fillBuildMap();
	}
	
	private final Map<Location, Material> buildMap;
	
	@Override
	public Map<Location, Material> getStructure() {
		return this.buildMap;
	}
	
	private void fillBuildMap() {
		this.buildMap.put(super.baseLocation.clone(), Material.POLISHED_ANDESITE);
		this.buildMap.put(super.baseLocation.clone().add(0, 1, 0), Material.SPRUCE_FENCE);
	}
	
}
