package com.gestankbratwurst.PowerTurrets.projectiles;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.gestankbratwurst.PowerTurrets.data.TurretStats;
import com.gestankbratwurst.PowerTurrets.manager.TraceManager;

public class SmallShot extends TurretProjectile{

	public SmallShot(TurretStats stats, Location start, Vector target, Set<UUID> friends) {
		super(stats, 1D, start, target, 1.0F, friends);
	}

	@Override
	protected ArrayList<Location> generatePath() {
		return TraceManager.getLinear(super.origin, super.targetDirection, super.stats.getRange(), super.getVelocity());
	}

	@Override
	public void runOut(Location loc) {
		
	}

	@Override
	protected void onBulletTick(Location loc) {
		loc.getWorld().spawnParticle(Particle.CRIT, loc, 1, 0D, 0D, 0D, 0D);
	}

	@Override
	public void entityImpact(LivingEntity target) {
		
		target.damage(super.stats.getBaseDamage());
		target.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 12, 0.8D, 0.8D, 0.8D);
		super.stats.getImpactMechanics().forEach(mechanic->mechanic.onImpactEntity(target));
		
	}

	@Override
	public void blockImpact(Location loc) {
		
		loc.getWorld().createExplosion(loc, 1.0F);
		loc.getWorld().spawnParticle(Particle.CRIT, loc, 12, 0.8D, 0.8D, 0.8D);
		
	}

}
