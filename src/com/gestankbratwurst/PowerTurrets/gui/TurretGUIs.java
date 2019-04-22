package com.gestankbratwurst.PowerTurrets.gui;

import com.gestankbratwurst.PowerTurrets.fileIO.Language;

import net.crytec.api.smartInv.SmartInventory;

public class TurretGUIs {
	
	public static final SmartInventory MAIN_TURRET_GUI = SmartInventory.builder().size(3, 9).provider(new TurretMainGUI()).title(Language.GENERAL_TURRET.toString()).build();
	public static final SmartInventory TURRET_STAT_GUI = SmartInventory.builder().size(4, 9).provider(new TurretStatGUI()).title(Language.GENERAL_STATS.toString()).build();
	public static final SmartInventory CONFIRM_DELETION_GUI = SmartInventory.builder().size(3, 9).provider(new ConfirmDeletionGUI()).title(Language.GENERAL_DELETE_TURRET.toString()).build();
	
}
