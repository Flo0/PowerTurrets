package com.gestankbratwurst.PowerTurrets.items;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import com.gestankbratwurst.PowerTurrets.Core;
import com.gestankbratwurst.PowerTurrets.fileIO.Language;
import com.gestankbratwurst.PowerTurrets.util.AmmoType;
import com.gestankbratwurst.PowerTurrets.util.Ammunition;
import com.gestankbratwurst.PowerTurrets.util.MathUtil;

import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.nbt.NBTItem;

public class BoxAmmoBuilder {
	
	public BoxAmmoBuilder() {
		this.config = Core.getPlugin().getFileManager().getMainConfig();
	}
	
	private final FileConfiguration config;
	
	public ItemStack getBoxAmmo(Ammunition ammo) throws IllegalArgumentException{
		if(!ammo.getAmmoType().equals(AmmoType.BOX)) throw new IllegalArgumentException("AmmoType must be BOX");
		return new ItemBuilder(Material.valueOf(config.getString(ammo.getConfigPath() + ".Material")))
				.name("§6" + ammo.getName())
				.lore("")
				.lore("§f" + Language.GENERAL_AMMO.toString() + ": " + "[§e" + ammo.getAmmoContent() + "§f/§e" + ammo.getAmmoContent() + "§f]")
				.lore(MathUtil.ProgressBar(ammo.getAmmoContent(), ammo.getAmmoContent(), 50, "|"))
				.addNBTString(Ammunition.getNbtKey(), ammo.toString())
				.addNBTString(AmmoType.getNbtKey(), ammo.getAmmoType().toString())
				.addNBTInt("bullets", ammo.getAmmoContent())
				.build();
	}
	
	public ItemStack removeBullets(ItemStack box, int bulletCount) {
		NBTItem nbt = new NBTItem(box);
		int bulletsLeft = nbt.getInteger("bullets");
		int newBulletCount = bulletsLeft - bulletCount;
		if(newBulletCount <= 0) return new ItemStack(Material.AIR);
		Ammunition ammo = Ammunition.valueOf(nbt.getString(Ammunition.getNbtKey()));
		return new ItemBuilder(box.getType())
				.name("§6" + ammo.getName())
				.lore("")
				.lore("§f" + Language.GENERAL_AMMO.toString() + ": " + "[§e" + newBulletCount + "§f/§e" + ammo.getAmmoContent() + "§f]")
				.lore(MathUtil.ProgressBar(newBulletCount, ammo.getAmmoContent(), 50, "|"))
				.addNBTString(Ammunition.getNbtKey(), ammo.toString())
				.addNBTString(AmmoType.getNbtKey(), ammo.getAmmoType().toString())
				.addNBTInt("bullets", newBulletCount)
				.build();
	}
	
}
