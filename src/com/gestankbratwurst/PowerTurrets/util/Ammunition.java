package com.gestankbratwurst.PowerTurrets.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.gestankbratwurst.PowerTurrets.projectiles.*;

@AllArgsConstructor
public enum Ammunition {
	//TODO Language
	FLAME_SHELL(AmmoType.SHELL, "Ammunition.Shells.FLAME_SHELL", "Flame Shell", 1, SmallShot.class),
	EXPLOSIVE_SHELL(AmmoType.SHELL, "Ammunition.Shells.EXPLOSIVE_SHELL", "Flame Shell", 1, SmallShot.class),
	EXPLOSIVE_FLAME_SHELL(AmmoType.SHELL, "Ammunition.Shells.EXPLOSIVE_FLAME_SHELL", "Flame Shell", 1, SmallShot.class),
	SMALL_SHELL(AmmoType.SHELL, "Ammunition.Shells.SMALL_SHELL", "Flame Shell", 1, SmallShot.class),
	SHELL(AmmoType.SHELL, "Ammunition.Shells.SHELL", "Shell", 1, SmallShot.class),
	MORTAR_SHELL(AmmoType.SHELL, "Ammunition.Shells.MORTAR_SHELL", "Flame Shell", 1, SmallShot.class),
	
	ARMOR_PIERCING(AmmoType.BOX, "Ammunition.Boxes.ARMOR_PIERCING", "Flame Shell", 32, SmallShot.class),
	BULLET_CRATE(AmmoType.BOX, "Ammunition.Boxes.BULLET_CRATE", "Bullet Crate", 64, SmallShot.class);
	
	@Getter
	private final AmmoType ammoType;
	@Getter
	private static final String nbtKey = "ammunition";
	@Getter
	private final String configPath;
	@Getter
	private final String name;
	@Getter
	private final int ammoContent;
	@Getter
	private Class<? extends TurretProjectile> projectileClass;
	
}
