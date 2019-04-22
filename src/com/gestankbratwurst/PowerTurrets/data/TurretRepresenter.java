package com.gestankbratwurst.PowerTurrets.data;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import com.gestankbratwurst.PowerTurrets.util.TurretType;

import lombok.Getter;

public class TurretRepresenter {
	
	private static final Vector X_AXIS = new Vector(1,0,0);
	private static final Vector Y_AXIS = new Vector(0,1,0);
	
	public TurretRepresenter(Location loc, TurretType type) {
		this.armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		this.armorStand.setVisible(false);
		this.armorStand.setCollidable(false);
		this.armorStand.setGravity(false);
		this.armorStand.setInvulnerable(true);
		this.armorStand.setMarker(true);
		this.head = new TurretHead(this.armorStand, type);
		this.id = this.armorStand.getUniqueId();
		this.armorStand.teleport(loc);
	}
	
	@Getter
	private final UUID id;
	private final ArmorStand armorStand;
	private final TurretHead head;
	
	public void rotateHead(Vector direction) {
		float yaw;
		float pitch = direction.angle(Y_AXIS) - (float) (2.5F * Math.PI);;
		
		Vector vecX = new Vector(direction.getX(), 0, direction.getZ());
		
		if(direction.getZ() < 0) {
			yaw = (float) (1.5F * Math.PI) - vecX.angle(X_AXIS);
		}else {
			yaw = vecX.angle(X_AXIS) - (float) (2.5F * Math.PI);
		}
		this.head.rotate(pitch, yaw, 0F);
	}
	
	public void onDelete() {
		this.armorStand.remove();
	}
}
