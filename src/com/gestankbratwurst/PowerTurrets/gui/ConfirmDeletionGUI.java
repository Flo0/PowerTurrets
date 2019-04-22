package com.gestankbratwurst.PowerTurrets.gui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.gestankbratwurst.PowerTurrets.Core;
import com.gestankbratwurst.PowerTurrets.data.Turret;
import com.gestankbratwurst.PowerTurrets.fileIO.Language;

import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;

public class ConfirmDeletionGUI implements InventoryProvider{

	@Override
	public void init(Player player, InventoryContents contents) {
		
		Turret turret = contents.property("turret");
		
		contents.set(SlotPos.of(1, 3), ClickableItem.of(new ItemBuilder(Material.RED_CONCRETE).name(Language.GENERAL_DESTROY_ABORT.toChatString()).build(), event->{
			player.closeInventory();
		}));
		
		contents.set(SlotPos.of(1, 5), ClickableItem.of(new ItemBuilder(Material.GREEN_CONCRETE).name(Language.GENERAL_DESTROY_CONFIRM.toChatString()).build(), event->{
			Core.getPlugin().getTurretManager().deleteTurret(turret);
			player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.5F, 0.8F);
			player.closeInventory();
		}));
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {
	}

}
