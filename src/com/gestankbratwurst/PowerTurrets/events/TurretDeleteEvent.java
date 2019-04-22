package com.gestankbratwurst.PowerTurrets.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gestankbratwurst.PowerTurrets.data.Turret;

import lombok.Getter;
import lombok.Setter;

public class TurretDeleteEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	@Getter @Setter
	private boolean cancelled = false;
	
	@Getter
	private final Turret turret;

	public TurretDeleteEvent(Turret turret) {
		this.turret = turret;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
