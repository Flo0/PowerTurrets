package com.gestankbratwurst.PowerTurrets.recipeLib;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.gestankbratwurst.PowerTurrets.recipeLib.shaped.ShapedCraftingMatrix;

import lombok.Getter;
import lombok.Setter;

public class CustomPrepareItemCraftEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	public CustomPrepareItemCraftEvent(Player player, ItemStack result, ShapedCraftingMatrix matrix) {
		this.result = result;
		this.matrix = matrix;
		this.player = player;
	}
	
	@Getter
	private Player player;
	@Getter @Setter
	private ItemStack result;
	@Getter
	private final ShapedCraftingMatrix matrix;

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
