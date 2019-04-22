package com.gestankbratwurst.PowerTurrets.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.gestankbratwurst.PowerTurrets.Core;
import com.gestankbratwurst.PowerTurrets.items.BoxAmmoBuilder;
import com.gestankbratwurst.PowerTurrets.util.AmmoType;
import com.gestankbratwurst.PowerTurrets.util.Ammunition;
import com.gestankbratwurst.PowerTurrets.util.TurretType;

import net.crytec.api.devin.commands.Command;
import net.crytec.api.devin.commands.CommandResult;
import net.crytec.api.devin.commands.Commandable;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.nbt.NBTItem;

public class DebugCommands implements Commandable{
	
	@Command(struct = "turret debug create", desc = "Creates turret on your location.", perms = {"turrets.admin"})
	public CommandResult turretCreate(Player sender, TurretType type) {
		
		Core.getPlugin().getTurretManager().createTurret(sender, type, sender.getLocation().getBlock().getLocation()).setAdmin(false);
		
		return CommandResult.success();
	}
	
	@Command(struct = "turret debug getshell", desc = "Gives you ammo of a type.", perms = {"turrets.admin"})
	public CommandResult ammoShell(Player sender, Ammunition type) {
		
		ItemStack item = new ItemBuilder(Material.FLINT).name("§6Admin Ammo - " + type.toString()).build();
		NBTItem nbt = new NBTItem(item);
		nbt.setString(AmmoType.getNbtKey(), type.getAmmoType().toString());
		nbt.setString(Ammunition.getNbtKey(), type.toString());
		
		sender.getInventory().addItem(nbt.getItem());
		
		return CommandResult.success();
	}
	
	@Command(struct = "turret debug getbox", desc = "Gives you ammo of a type.", perms = {"turrets.admin"})
	public CommandResult ammoBox(Player sender, Ammunition type) {
		
		sender.getInventory().addItem(new BoxAmmoBuilder().getBoxAmmo(type));
		
		return CommandResult.success();
	}
	
	@Command(struct = "recipe create", desc = "Creates new recipe.", perms = {"turrets.admin"})
	public CommandResult recipe(Player sender) {
		
		sender.openInventory(Bukkit.createInventory(sender, InventoryType.DISPENSER, "§f§f§eNEW RECIPE"));
		
		return CommandResult.success();
	}
	
}
