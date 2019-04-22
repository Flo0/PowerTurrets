package com.gestankbratwurst.PowerTurrets.data;

import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import com.gestankbratwurst.PowerTurrets.util.TurretType;

public class TurretHead {
	
	public TurretHead(ArmorStand host, TurretType type) {
		this.host = host;
		this.host.setHelmet(new ItemStack(type.getHeadMaterial()));
	}
	
	private final ArmorStand host;
	
	public void rotate(float pitch, float yaw, float roll) {
		host.setHeadPose(new EulerAngle(pitch,yaw,roll));
	}
	
}