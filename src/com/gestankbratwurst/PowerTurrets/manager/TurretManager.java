package com.gestankbratwurst.PowerTurrets.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.gestankbratwurst.PowerTurrets.data.Turret;
import com.gestankbratwurst.PowerTurrets.events.PreTurretCreateEvent;
import com.gestankbratwurst.PowerTurrets.events.TurretCreateEvent;
import com.gestankbratwurst.PowerTurrets.events.TurretDeleteEvent;
import com.gestankbratwurst.PowerTurrets.fileIO.Language;
import com.gestankbratwurst.PowerTurrets.util.TurretType;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.Getter;

public class TurretManager {
	
	public TurretManager() {
		this.playerTurrets = Maps.newHashMap();
		this.turretLocations = Maps.newHashMap();
		this.allTurrets = Sets.newHashSet();
	}
	
	@Getter
	private Set<Turret> allTurrets;
	
	private Map<UUID, Set<Turret>> playerTurrets;
	
	public Set<Turret> getTurretsFor(UUID player) {
		return this.playerTurrets.computeIfAbsent(player, (turretSet) -> Sets.newHashSet());
	}
	
	public Set<Turret> getLoadedTurrets(){
		return this.getAllTurrets().stream().filter(turret->turret.isLoaded()).collect(Collectors.toSet());
	}
	
	public int getTurretCount(UUID player) {
		return this.getTurretsFor(player).size();
	}
	
	public Optional<Turret> getTurretOfLocation(Location loc){
		return Optional.ofNullable(this.turretLocations.getOrDefault(loc, null));
	}
	
	private final Map<Location, Turret> turretLocations;
	
	public void deleteTurret(Turret turret) {
		
		TurretDeleteEvent event = new TurretDeleteEvent(turret);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		
		turret.onDelete();
		this.turretLocations.remove(turret.getBaseLoc());
		turret.getBlockBound().getBlocks().forEach(block->{
			this.turretLocations.remove(block.getLocation());
		});
		turret.destroyStructure();
		turret.getRepresenter().onDelete();
	}
	
	public Turret createTurret(Player player, TurretType type, Location location) {
		World world = location.getWorld();
		if(world.getDifficulty().equals(Difficulty.PEACEFUL) || !world.getGameRuleValue(GameRule.DO_MOB_SPAWNING) || !world.getAllowMonsters()) {
			player.sendMessage(Language.ERROR_MOBSPAWN_DISABLED.toChatString());
			return null;
		}
		
		PreTurretCreateEvent event = new PreTurretCreateEvent(player, location);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return null;
		
		try {
		Constructor<? extends Turret> constructor = type.getCastClass().getConstructor(Location.class, UUID.class, TurretType.class);
		Object[] parameter = new Object[] { event.getLocation(), player.getUniqueId(), type };
		
		Turret turret = constructor.newInstance(parameter);
		turret.buildStructure();
		turret.onCreate();
		
		turret.getBlockBound().getBlocks().stream().map(Block::getLocation).forEach(loc -> this.turretLocations.put(loc, turret));
		this.getTurretsFor(player.getUniqueId()).add(turret);
		this.allTurrets.add(turret);
		
		TurretCreateEvent createEvent = new TurretCreateEvent(player, location, turret);
		Bukkit.getPluginManager().callEvent(createEvent);
		
		return turret;
		} catch ( IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			ex.printStackTrace();
		}
		
		return null;
		
	}
	
	public boolean isTurret(Location loc) {
		return this.turretLocations.containsKey(loc);
	}
}