package com.gestankbratwurst.PowerTurrets.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.gestankbratwurst.PowerTurrets.data.Turret;

import lombok.Getter;

public class TurretCreateEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	@Getter
	private final Turret turret;
	@Getter
	private final Location location;
	
	public TurretCreateEvent(Player who, Location location, Turret turret) {
		super(who);
		this.turret = turret;
		this.location = location;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
