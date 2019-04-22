package com.gestankbratwurst.PowerTurrets.listener;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import com.gestankbratwurst.PowerTurrets.data.Turret;
import com.gestankbratwurst.PowerTurrets.data.TurretMagazine;
import com.gestankbratwurst.PowerTurrets.fileIO.Language;
import com.gestankbratwurst.PowerTurrets.gui.TurretGUIs;
import com.gestankbratwurst.PowerTurrets.manager.TurretManager;

public class TurretInteractionListener implements Listener{
	
	public TurretInteractionListener(TurretManager manager) {
		this.manager = manager;
	}
	
	private final TurretManager manager;
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Optional<Turret> opTurret = this.manager.getTurretOfLocation(event.getBlock().getLocation());
		if(!opTurret.isPresent()) return;
	}
	
	@EventHandler
	public void onPlayerClicks(PlayerInteractEvent event) {
		if(!event.getHand().equals(EquipmentSlot.HAND)) return;
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		Optional<Turret> opTurret = this.manager.getTurretOfLocation(event.getClickedBlock().getLocation());
		if(!opTurret.isPresent()) return;
		Turret turret = opTurret.get();
		if(event.getPlayer().getUniqueId() != turret.getOwner()) {
			event.getPlayer().sendMessage(Language.MESSAGES_NOT_OWNER.toChatString());
			return;
		}
		TurretGUIs.MAIN_TURRET_GUI.open(event.getPlayer(), new String[] {"turret"}, new Object[] {turret});
	}
	
	@EventHandler
	public void onMagazineClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		if(!TurretMagazine.isMagazine(inv)) return;
		TurretMagazine.of(inv).onClose(event);
	}
	
}
