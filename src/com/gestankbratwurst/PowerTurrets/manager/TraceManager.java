package com.gestankbratwurst.PowerTurrets.manager;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import com.gestankbratwurst.PowerTurrets.util.ProjectileVelocity;
import com.google.common.collect.Lists;

public class TraceManager {
	
	public static ArrayList<Location> getLinear(Location from, Vector direction, double lenght, ProjectileVelocity velocity){
		
		ArrayList<Location> locations = Lists.newArrayList();
		
		World world = from.getWorld();
		Vector start = from.toVector();
		if(direction==null) {
			System.out.println("Vec is null");
		}
		if(velocity==null) {
			System.out.println("Vel is null");
		}
		Vector dir = direction.clone().multiply(velocity.getStepMultiplier());
		Vector normal = dir.clone();
		
		while(dir.length() < lenght) {
			
			locations.add(dir.clone().add(start).toLocation(world));
			dir.add(normal);
			
		}
		
		return locations;
	}
	
}
