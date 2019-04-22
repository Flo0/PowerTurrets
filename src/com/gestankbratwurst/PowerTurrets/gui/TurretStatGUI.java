package com.gestankbratwurst.PowerTurrets.gui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gestankbratwurst.PowerTurrets.data.TurretStats;
import com.gestankbratwurst.PowerTurrets.fileIO.Language;

import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;

public class TurretStatGUI implements InventoryProvider{

	@Override
	public void init(Player player, InventoryContents contents) {
		
		TurretStats stats = contents.property("stats");
		
		ItemStack statIcon = new ItemBuilder(Material.DISPENSER)
				.name("§6" + stats.getType().getName().toString() + " [§e" + stats.getLevel() + " §6 / §e" + stats.getMaxlevel() + "§6]")
				.lore("")
				.lore("§f" + Language.GENERAL_POINTS.toString() + ": " + stats.getPoints())
				.lore("§f" + Language.GENERAL_LEVEL_UP_COST.toString() + ": " + stats.getNextLevelCost() + " " + Language.GENERAL_EXP.toString())
				.build();
		
		ItemStack health = new ItemBuilder(Material.ROSE_RED )
				.name("§6" + Language.GENERAL_HEALTH.toString() + ": §e" + stats.getHp() + "§6 / §e" + stats.getMaxHp())
				.lore("")
				.lore("§fCost: " + stats.getHpLvlCost())
				.lore("§f[" + stats.getHp() + "]" + " -> " + "[" + (stats.getHp() + stats.getHpValueGain()) + "]")
				.build();
		ItemStack damage = new ItemBuilder(Material.DIAMOND_SWORD)
				.name("§6" + Language.GENERAL_DAMAGE.toString() + ": §e" + stats.getBaseDamage() + "§6 / §e" + stats.getMaxDamage())
				.lore("")
				.lore("§fCost: " + stats.getDamageLvlCost())
				.lore("§f[" + stats.getBaseDamage() + "]" + " -> " + "[" + (stats.getBaseDamage() + stats.getDamageValueGain()) + "]")
				.build();
		ItemStack accuracy = new ItemBuilder(Material.SLIME_BALL)
				.name("§6" + Language.GENERAL_ACCURACY.toString() + ": §e" + stats.getAccuracy() + "§6 / §e" + stats.getMaxAccuracy())
				.lore("")
				.lore("§fCost: " + stats.getAccuracyLvlCost())
				.lore("§f[" + stats.getAccuracy() + "]" + " -> " + "[" + (stats.getAccuracy() + stats.getAccuracyValueGain()) + "]")
				.build();
		ItemStack range = new ItemBuilder(Material.ARROW)
				.name("§6" + Language.GENERAL_RANGE.toString() + ": §e" + stats.getRange() + "§6 / §e" + stats.getMaxRange())
				.lore("")
				.lore("§fCost: " + stats.getRangeLvlCost())
				.lore("§f[" + stats.getRange() + "]" + " -> " + "[" + (stats.getRange() + stats.getRangeValueGain()) + "]")
				.build();
		ItemStack fireRate = new ItemBuilder(Material.REDSTONE_TORCH)
				.name("§6" + Language.GENERAL_FIRE_RATE.toString() + ": §e" + stats.getFireRate() + "§6 / §e" + stats.getMaxFireRate())
				.lore("")
				.lore("§fCost: " + stats.getFireRateLvlCost())
				.lore("§f[" + stats.getFireRate() + "]" + " -> " + "[" + stats.getFireRate().getNext() + "]")
				.build();
		ItemStack velocity = new ItemBuilder(Material.GUNPOWDER)
				.name("§6" + Language.GENERAL_VELOCITY.toString() + ": §e" + stats.getVelocity() + "§6 / §e" + stats.getMaxVelocity())
				.lore("")
				.lore("§fCost: " + stats.getVelocityLvlCost())
				.lore("§f[" + stats.getVelocity() + "]" + " -> " + "[" + stats.getVelocity().getNext() + "]")
				.build();
		
		contents.set(SlotPos.of(0, 4), ClickableItem.of(statIcon, e -> {
			
			if(player.getTotalExperience() > stats.getNextLevelCost() && stats.getMaxlevel() > stats.getLevel()) {
				player.setTotalExperience(player.getTotalExperience() - stats.getNextLevelCost());
				stats.levelUp();
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1.0F);
				TurretGUIs.TURRET_STAT_GUI.open(player, new String[] {"stats"}, new Object[] {stats});
			}else {
				player.sendMessage(Language.LEVEL_UP_FAILED_EXP.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 0.5F, 0.4F);
			}
			
		}));
		
		contents.set(SlotPos.of(2, 1), ClickableItem.of(health, e -> {
			
			if(stats.lvlUpHealth()) {
				player.sendMessage(Language.LEVEL_UP_HEALTH.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2.0F);
				TurretGUIs.TURRET_STAT_GUI.open(player, new String[] {"stats"}, new Object[] {stats});
			}else {
				player.sendMessage(Language.LEVEL_UP_FAILED.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 0.5F, 0.4F);
			}
			
		}));
		
		contents.set(SlotPos.of(2, 2), ClickableItem.of(damage, e -> {
			
			if(stats.lvlUpDamage()) {
				player.sendMessage(Language.LEVEL_UP_DAMAGE.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2.0F);
				TurretGUIs.TURRET_STAT_GUI.open(player, new String[] {"stats"}, new Object[] {stats});
			}else {
				player.sendMessage(Language.LEVEL_UP_FAILED.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 0.5F, 0.4F);
			}
			
		}));
		
		contents.set(SlotPos.of(2, 3), ClickableItem.of(accuracy, e -> {
			
			if(stats.lvlUpAccuracy()) {
				player.sendMessage(Language.LEVEL_UP_ACCURACY.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2.0F);
				TurretGUIs.TURRET_STAT_GUI.open(player, new String[] {"stats"}, new Object[] {stats});
			}else {
				player.sendMessage(Language.LEVEL_UP_FAILED.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 0.5F, 0.4F);
			}
			
		}));
		
		contents.set(SlotPos.of(2, 5), ClickableItem.of(range, e -> {
			
			if(stats.lvlUpRange()) {
				player.sendMessage(Language.LEVEL_UP_RANGE.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2.0F);
				TurretGUIs.TURRET_STAT_GUI.open(player, new String[] {"stats"}, new Object[] {stats});
			}else {
				player.sendMessage(Language.LEVEL_UP_FAILED.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 0.5F, 0.4F);
			}
			
		}));
		
		contents.set(SlotPos.of(2, 6), ClickableItem.of(fireRate, e -> {
			
			if(stats.lvlFireRate()) {
				player.sendMessage(Language.LEVEL_UP_FIRE_RATE.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2.0F);
				TurretGUIs.TURRET_STAT_GUI.open(player, new String[] {"stats"}, new Object[] {stats});
			}else {
				player.sendMessage(Language.LEVEL_UP_FAILED.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 0.5F, 0.4F);
			}
			
		}));
		
		contents.set(SlotPos.of(2, 7), ClickableItem.of(velocity, e -> {
			
			if(stats.lvlProjectileVelocity()) {
				player.sendMessage(Language.LEVEL_UP_PROJECTILE_VELOCITY.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2.0F);
				TurretGUIs.TURRET_STAT_GUI.open(player, new String[] {"stats"}, new Object[] {stats});
			}else {
				player.sendMessage(Language.LEVEL_UP_FAILED.toChatString());
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 0.5F, 0.4F);
			}
			
		}));
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		
		
	}

}
