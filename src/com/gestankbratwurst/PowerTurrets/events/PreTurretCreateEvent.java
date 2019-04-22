package com.gestankbratwurst.PowerTurrets.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import lombok.Getter;
import lombok.Setter;

public class PreTurretCreateEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	@Getter @Setter
	private boolean cancelled = false;
	
	@Getter @Setter
	private Location location;

	public PreTurretCreateEvent(Player who, Location location) {
		super(who);
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
