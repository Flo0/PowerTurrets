package com.gestankbratwurst.PowerTurrets.data;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gestankbratwurst.PowerTurrets.Core;
import com.gestankbratwurst.PowerTurrets.fileIO.Language;
import com.gestankbratwurst.PowerTurrets.items.BoxAmmoBuilder;
import com.gestankbratwurst.PowerTurrets.projectiles.TurretProjectile;
import com.gestankbratwurst.PowerTurrets.util.AmmoType;
import com.gestankbratwurst.PowerTurrets.util.Ammunition;
import com.google.common.collect.Maps;

import lombok.Getter;
import net.crytec.api.nbt.NBTItem;

public class TurretMagazine {
	
	private static Map<Inventory, TurretMagazine> magazineMap = Maps.newHashMap();
	
	public static TurretMagazine of(Inventory inv) {
		return magazineMap.get(inv);
	}
	
	public static boolean isMagazine(Inventory inv) {
		return magazineMap.containsKey(inv);
	}
	
	public TurretMagazine(Turret turret) {
		this.inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, Language.GENERAL_MAGAZINE.toString());
		FileConfiguration config = Core.getPlugin().getFileManager().getMainConfig();
		String path = turret.getType().getConfigPath();
		this.allowedAmmoType = AmmoType.valueOf(config.getString(path + ".AllowedAmmoType"));
		this.allowedAmmunition = config.getStringList(path + ".AllowedAmmunition").stream().map(str->Ammunition.valueOf(str)).collect(Collectors.toSet());
		this.boxBuilder = new BoxAmmoBuilder();
		
		magazineMap.put(this.inventory, this);
	}
	
	@Getter
	private final Inventory inventory;
	private final AmmoType allowedAmmoType;
	private final Set<Ammunition> allowedAmmunition;
	private final BoxAmmoBuilder boxBuilder;
	
	public Optional<Class<? extends TurretProjectile>> loadNextProjectile(boolean doesConsume) {
		Class<? extends TurretProjectile> projectileClass = null;
		for(int slot = 0; slot < 9; slot++) {
			ItemStack item = this.inventory.getItem(slot);
			if(item!=null) {
				NBTItem nbt = new NBTItem(item);
				if(!this.allowedAmmunition.contains(Ammunition.valueOf(nbt.getString(Ammunition.getNbtKey())))) continue;
				projectileClass = Ammunition.valueOf(nbt.getString(Ammunition.getNbtKey())).getProjectileClass();
				if(doesConsume) this.reduceAmmo(slot);
				break;
			}
		}
		return Optional.ofNullable(projectileClass);
	}
	
	private void reduceAmmo(int slot) {
		ItemStack item = this.inventory.getItem(slot);
		switch(AmmoType.valueOf(new NBTItem(item).getString(AmmoType.getNbtKey()))) {
		case SHELL: 
			if(item.getAmount() > 1) {
				item.setAmount(item.getAmount() - 1);
			}else {
				this.inventory.setItem(slot, new ItemStack(Material.AIR));
			}
		case BOX: 
			this.inventory.setItem(slot, this.boxBuilder.removeBullets(item, 1));
		}
	}
	
	public void onClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		for(int slot = 0; slot <= 8; slot++) {
			ItemStack item = inv.getItem(slot);
			if(item == null) continue;
			NBTItem nbt = new NBTItem(item);
			if(!nbt.hasKey(AmmoType.getNbtKey())) {
				if(!nbt.hasKey(Ammunition.getNbtKey())){
					event.getPlayer().getInventory().addItem(item);
					inv.setItem(slot, new ItemStack(Material.AIR));
					return;
				}
			}
			AmmoType type = AmmoType.valueOf(nbt.getString(AmmoType.getNbtKey()));
			Ammunition ammo = Ammunition.valueOf(nbt.getString(Ammunition.getNbtKey()));
			if(!this.allowedAmmoType.equals(type) || !this.allowedAmmunition.contains(ammo)) {
				event.getPlayer().getInventory().addItem(item);
				inv.setItem(slot, new ItemStack(Material.AIR));
				return;
			}
		}
	}
}
