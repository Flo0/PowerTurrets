package com.gestankbratwurst.PowerTurrets.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.gestankbratwurst.PowerTurrets.Threads.TurretWorkload;
import com.gestankbratwurst.PowerTurrets.fileIO.Language;
import com.gestankbratwurst.PowerTurrets.projectiles.TurretProjectile;
import com.gestankbratwurst.PowerTurrets.schematics.TurretSchematic;
import com.gestankbratwurst.PowerTurrets.util.TurretType;
import com.gestankbratwurst.PowerTurrets.util.VectorDistanceComparator;
import com.google.common.collect.Sets;

import lombok.Getter;
import lombok.Setter;
import net.crytec.api.util.Cuboid;
import net.crytec.api.util.Cuboid.CuboidDirection;

public abstract class Turret {
	
	public Turret(Location loc, UUID owner, TurretType type) {
		this.friends = Sets.newHashSet();
		this.baseLoc = loc.clone().add(0.5, 0, 0.5);
		this.owner = owner;
		this.type = type;
		this.blockBound = new Cuboid(loc.clone());
		this.blockBound.expand(CuboidDirection.Up, type.getHeight() - 1);
		this.blockBound.expand(CuboidDirection.East, type.getWidth() - 1);
		this.blockBound.expand(CuboidDirection.West, type.getWidth() - 1);
		this.blockBound.expand(CuboidDirection.North, type.getWidth() - 1);
		this.blockBound.expand(CuboidDirection.South, type.getWidth() - 1);
		this.stats = new TurretStats(type);
		this.tickLive = 0;
		this.workload = new TurretWorkload(this);
		this.turretHeadLocation = this.baseLoc.clone().add(0, type.getHeight() + 0.5D, 0);
		this.structure = type.getSchematicFor(this.baseLoc.clone());
		this.representer = new TurretRepresenter(this.turretHeadLocation.clone().subtract(0, 1.75D, 0), type);
		//TODO add owner back in
		//this.friends.add(owner);
		this.friends.add(this.representer.getId());
		this.magazine = new TurretMagazine(this);
	}
	
	@Getter @Setter
	private boolean isAdmin = false;
	
	@Getter
	private final TurretMagazine magazine;
	@Getter
	private final TurretRepresenter representer;
	@Getter
	private final TurretSchematic structure;
	@Getter
	private final TurretWorkload workload;
	@Getter
	private final TurretType type;
	@Getter
	private final Location baseLoc;
	@Getter
	private final Location turretHeadLocation;
	@Getter
	private final UUID owner;
	@Getter
	private final Cuboid blockBound;
	@Getter
	private final TurretStats stats;
	@Getter
	private long tickLive;
	@Getter
	private final Set<UUID> friends;
	
	protected Optional<Vector> currentTarget;
	
	public void onTick() {
		this.findTarget();
		
		this.tickLive += 5;
		
		if(this.currentTarget.isPresent()) {
			this.representer.rotateHead(this.currentTarget.get().clone().subtract(this.turretHeadLocation.toVector()).normalize());
			if(this.tickLive % this.stats.getFireRate().getPeriod() == 0) {
				this.fireProjectile();
			}
		}
	}
	
	private void findTarget() {
		double range = this.stats.getRange();
		Optional<Vector> target = this.baseLoc.getWorld().getNearbyEntities(this.baseLoc, range, range, range)
				.stream()
				.filter(entity -> entity instanceof LivingEntity)
				.filter(entity -> !friends.contains(entity.getUniqueId()))
				.map(LivingEntity.class::cast)
				.map(entity -> entity.getLocation().toVector())
				.min(new VectorDistanceComparator(this.baseLoc.toVector()));
		if(target.isPresent()) {
			
			Vector vec = target.get().add(new Vector(0, 1, 0));
			RayTraceResult result = this.getBaseLoc().getWorld().rayTrace(this.turretHeadLocation, vec.clone().subtract(this.turretHeadLocation.toVector()).normalize(), (this.stats.getRange() + 1D), FluidCollisionMode.NEVER, true, 1D, entity -> !friends.contains(entity.getUniqueId()));
			if(result == null || result.getHitBlock() != null) target = Optional.ofNullable(null);
			
		}
		
		this.currentTarget = target;
	}
	
	private void fireProjectile() {
		if(!this.currentTarget.isPresent()) return;
		Optional<Class<? extends TurretProjectile>> optPro = this.magazine.loadNextProjectile(!this.isAdmin);
		if(!optPro.isPresent()) return;
		
		Class<? extends TurretProjectile> projClass = optPro.get();
		Object[] parameter = new Object[] {this.stats, this.turretHeadLocation, this.currentTarget.get(), this.friends};
		
		try {
			Constructor<? extends TurretProjectile> cons = projClass.getConstructor(TurretStats.class, Location.class, Vector.class, Set.class);
			try {
				cons.newInstance(parameter);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		this.onFire();
		
	}
	
	public boolean isLoaded() {
		return baseLoc.getWorld().isChunkLoaded(baseLoc.getBlockX()/16, baseLoc.getBlockZ()/16);
	}
	
	public void onBlockBreak(BlockBreakEvent event) {
		if(!this.onBreakAttempt()) return;
		if(event.getPlayer().getUniqueId() != this.owner) {
			event.getPlayer().sendMessage(Language.ERROR_BREAK_NOT_OWNER.toChatString());
			event.setCancelled(true);
			return;
		}
	}
	
	public void buildStructure() {
		this.structure.buildUp();
	}
	
	public void destroyStructure() {
		this.structure.tearDown();
	}
	
	public abstract void onDelete();
	public abstract void onDestroy();
	public abstract void onCreate();
	public abstract void onFire();
	public abstract boolean onBreakAttempt();
	
}