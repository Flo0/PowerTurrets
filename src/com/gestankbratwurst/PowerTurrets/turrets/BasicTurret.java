package com.gestankbratwurst.PowerTurrets.turrets;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Sound;

import com.gestankbratwurst.PowerTurrets.data.Turret;
import com.gestankbratwurst.PowerTurrets.util.TurretType;

public class BasicTurret extends Turret{

	public BasicTurret(Location loc, UUID owner, TurretType type) {
		super(loc, owner, type);
	}

	@Override
	public void onDelete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreate() {
		
		
	}

	@Override
	public void onFire() {
		
		super.getBaseLoc().getWorld().playSound(super.getTurretHeadLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0.6F, 1.5F);
		
	}

	@Override
	public boolean onBreakAttempt() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	
}
