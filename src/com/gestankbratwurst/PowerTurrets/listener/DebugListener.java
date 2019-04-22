package com.gestankbratwurst.PowerTurrets.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.gestankbratwurst.PowerTurrets.data.TurretStats;
import com.gestankbratwurst.PowerTurrets.projectiles.SmallShot;
import com.gestankbratwurst.PowerTurrets.util.ProjectileVelocity;
import com.gestankbratwurst.PowerTurrets.util.TurretType;
import com.google.common.collect.Sets;

public class DebugListener implements Listener{
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) || !event.getHand().equals(EquipmentSlot.HAND)) return;
		Player player = event.getPlayer();
		TurretStats stats = new TurretStats(TurretType.BASIC);
		RayTraceResult result = player.rayTraceBlocks(128D);
		if(result == null) {
			player.sendMessage("No Target found.");
			return;
		}
		Vector position = result.getHitPosition();
		
		stats.setVelocity(ProjectileVelocity.SUDDEN_IMPACT);
		stats.setAccuracy(0.5F);
		Location shootLoc = player.getLocation().clone().add(0,2,0).add(position.clone().subtract(player.getLocation().clone().toVector()).normalize().multiply(3));
		new SmallShot(stats, shootLoc, position, Sets.newHashSet());
	}
	
	
}