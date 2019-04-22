package com.gestankbratwurst.PowerTurrets.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.gestankbratwurst.PowerTurrets.data.Turret;
import com.gestankbratwurst.PowerTurrets.fileIO.Language;

import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;

public class TurretMainGUI implements InventoryProvider{
	
	@Override
	public void init(Player player, InventoryContents contents) {
		
		Turret turret = contents.property("turret");
		
		contents.set(SlotPos.of(1, 2), ClickableItem.of(new ItemBuilder(Material.BOOK)
				.name("§6" + Language.GENERAL_STATS.toString())
				.build(), event->{
			TurretGUIs.TURRET_STAT_GUI.open(player, new String[] {"stats"}, new Object[] {turret.getStats()});
		}));
		
		contents.set(SlotPos.of(1, 4), ClickableItem.of(new ItemBuilder(Material.RED_CONCRETE)
				.name("§6" + Language.GENERAL_DELETE_TURRET.toString())
				.build(), event->{
			TurretGUIs.CONFIRM_DELETION_GUI.open(player, new String[] {"turret"}, new Object[] {turret});
		}));
		
		contents.set(SlotPos.of(1, 6), ClickableItem.of(new ItemBuilder(Material.CHEST)
				.name("§6" + Language.GENERAL_INVENTORY.toString())
				.build(), event->{
			player.openInventory(turret.getMagazine().getInventory());
		}));
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		
		
		
	}

}
