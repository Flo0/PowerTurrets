package com.gestankbratwurst.PowerTurrets.projectiles;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.gestankbratwurst.PowerTurrets.Threads.ProjectilePathfinder;
import com.gestankbratwurst.PowerTurrets.data.TurretStats;
import com.gestankbratwurst.PowerTurrets.util.LivingEntityDistanceComparator;
import com.gestankbratwurst.PowerTurrets.util.ProjectileVelocity;

import lombok.Getter;

public abstract class TurretProjectile {
	
	public TurretProjectile(TurretStats stats, double projectileRadius, Location origin, Vector targetPosition, float baseAccuracy, Set<UUID> friends) {
		this.stats = stats;
		this.targetPosition = targetPosition;
		this.originTargetDirectionVector = targetPosition.clone().subtract(origin.toVector());
		this.projectileRadius = projectileRadius;
		this.origin = origin;
		this.targetDirectionOrigin = this.originTargetDirectionVector.clone().normalize();
		this.state = 0;
		this.currentLoc = this.origin;
		this.baseAccuracy = baseAccuracy;
		this.targetDirection = this.applyAccuracy();
		this.path = this.generatePath();
		this.friends = friends;
		
		new ProjectilePathfinder(this);
	}
	
	private final Set<UUID> friends;
	private final float baseAccuracy;
	protected final double projectileRadius;
	@Getter
	private final Vector targetPosition;
	protected final TurretStats stats;
	private final Vector originTargetDirectionVector;
	private final ArrayList<Location> path;
	protected final Location origin;
	@Getter
	private final Vector targetDirectionOrigin;
	protected final Vector targetDirection;
	@Getter
	private Location currentLoc;
	@Getter
	private int state = 0;
	
	public boolean tickBullet() {
		
		Optional<LivingEntity> nearest = this.getNearest(this.currentLoc);
		
		this.onBulletTick(this.currentLoc);
		
		if(nearest.isPresent()) {
			this.entityImpact(nearest.get());
			return true;
		}
		if(!this.getCurrentLoc().getBlock().getType().equals(Material.AIR)) {
			this.blockImpact(this.currentLoc);
			return true;
		}
		
		return false;
	}
	
	public ProjectileVelocity getVelocity() {
		return this.stats.getVelocity();
	}
	
	public boolean updateState() {
		this.state++;
		if(this.state >= this.path.size()) {
			this.runOut(this.currentLoc);
			return false;
		}
		this.currentLoc = this.path.get(this.state);
		return true;
	}
	
	protected Set<LivingEntity> getNearProjectile(Location loc){
		return loc.getWorld().getNearbyEntities(loc, projectileRadius, projectileRadius, projectileRadius)
				.stream()
				.filter(current-> (current instanceof LivingEntity))
				.map(LivingEntity.class::cast)
				.collect(Collectors.toSet());
	}
	
	protected Optional<LivingEntity> getNearest(Location loc) {
		return loc.getWorld().getNearbyEntities(loc, this.projectileRadius, this.projectileRadius, this.projectileRadius)
				.stream()
				.filter(entity -> entity instanceof LivingEntity)
				.filter(entity -> !this.friends.contains(entity.getUniqueId()))
				.map(LivingEntity.class::cast)
				.min(new LivingEntityDistanceComparator(loc));
	}
	
	private Vector applyAccuracy() throws IllegalArgumentException{
		
		float accuracy = this.stats.getAccuracy() * this.baseAccuracy;
		
		if(accuracy <= 0F || accuracy > 1.0F) {
			throw new IllegalArgumentException("Accuracy must be greater than 0 and smaller than or equal to 1.0");
		}
		
		if(accuracy == 1) {
			return this.targetDirectionOrigin;
		}
		
		double inaccuracy = 1D - accuracy;
		double distance = this.originTargetDirectionVector.length() * 0.25D;
		
		double x = distance * (ThreadLocalRandom.current().nextDouble(2D * inaccuracy) - inaccuracy);
		double y = distance * (ThreadLocalRandom.current().nextDouble(2D * inaccuracy) - inaccuracy);
		double z = distance * (ThreadLocalRandom.current().nextDouble(2D * inaccuracy) - inaccuracy);
		
		return this.targetPosition.clone().add(new Vector(x,y,z)).subtract(this.origin.toVector()).normalize();
	}
	
	protected abstract ArrayList<Location> generatePath();
	public abstract void entityImpact(LivingEntity target);
	public abstract void blockImpact(Location loc);
	public abstract void runOut(Location loc);
	protected abstract void onBulletTick(Location loc);
	
}
