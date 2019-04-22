package com.gestankbratwurst.PowerTurrets.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Location;
import org.bukkit.Material;

import com.gestankbratwurst.PowerTurrets.data.Turret;
import com.gestankbratwurst.PowerTurrets.fileIO.Language;
import com.gestankbratwurst.PowerTurrets.schematics.BasicTurretSchematic;
import com.gestankbratwurst.PowerTurrets.schematics.TurretSchematic;
import com.gestankbratwurst.PowerTurrets.turrets.BasicTurret;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TurretType {
	
	BASIC(1D, 2, 1, Material.DISPENSER, "BasicTurret", Language.TURRET_BASIC_TURRET, BasicTurret.class, BasicTurretSchematic.class);
	
	@Getter
	private final double headOffset;
	@Getter
	private final int height;
	@Getter
	private final int width;
	@Getter
	private final Material headMaterial;
	@Getter
	private final String configPath;
	@Getter
	private final Language name;
	@Getter
	private final Class<? extends Turret> castClass;
	private final Class<? extends TurretSchematic> structureClass;
	
	public final TurretSchematic getSchematicFor(Location loc) {
		try {
		Constructor<? extends TurretSchematic> constructor = this.structureClass.getConstructor(Location.class);
		Object[] parameter = new Object[] { loc };
		
		return constructor.newInstance(parameter);
		
		} catch ( IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
