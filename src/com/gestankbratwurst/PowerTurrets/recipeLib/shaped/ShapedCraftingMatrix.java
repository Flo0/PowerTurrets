package com.gestankbratwurst.PowerTurrets.recipeLib.shaped;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

public class ShapedCraftingMatrix {
	
	public ShapedCraftingMatrix(CraftingInventory inv) {
		this.matrix = Maps.newHashMapWithExpectedSize(9);
		ItemStack[] itemMatrix = inv.getMatrix();
		for(int slot = 0; slot < 9; slot++) {
			ItemStack item = itemMatrix[slot];
			if(item==null) {
				this.matrix.put(slot, null);
				continue;
			}
			ItemStack oneItem = item.clone();
			oneItem.setAmount(1);
			this.matrix.put(slot, oneItem);
		}
		this.isLocked = false;
	}
	
	public ShapedCraftingMatrix() {
		this.matrix = Maps.newHashMapWithExpectedSize(9);
		for(int slot = 0; slot < 9; slot++) {
			this.matrix.put(slot, null);
		}
		this.isLocked = false;
	}
	
	private final Map<Integer, ItemStack> matrix;
	private boolean isLocked;
	
	public void singleRemoveFrom(CraftingInventory inv) {
		ItemStack[] invMatrix = inv.getMatrix();
		for(int slot = 0; slot < 9; slot++) {
			
			ItemStack item = invMatrix[slot];
			if(item == null || item.getType().equals(Material.AIR)) continue;
			int present = item.getAmount();
			int toRemove = this.matrix.get(slot).getAmount();
			int left = present - toRemove;
			
			if(left == 0) {
				inv.setItem(slot+1, new ItemStack(Material.AIR));
			}else {
				item.setAmount(left);
			}
		}
	}
	
	public int fullRemoveFrom(CraftingInventory inv) {
		ItemStack[] invMatrix = inv.getMatrix();
		int amount = 64;
		int current;
		for(int slot = 0; slot < 9; slot++) {
			
			ItemStack item = invMatrix[slot];
			if(item==null) continue;
			current = item.getAmount();
			if(current < amount) amount = current;
			
		}
		
		for(int slot = 0; slot < 9; slot++) {
			
			ItemStack item = invMatrix[slot];
			if(item == null || item.getType().equals(Material.AIR)) continue;
			int present = item.getAmount();
			int toRemove = amount;
			int left = present - toRemove;
			
			if(left == 0) {
				inv.setItem(slot+1, new ItemStack(Material.AIR));
			}else {
				item.setAmount(left);
			}
		}
		
		return amount;
	}
	
	public Map<Integer, ItemStack> getMatrix() throws IllegalStateException{
		if(this.isLocked) throw new IllegalStateException("This object has been locked and can not be edited.");
		return this.matrix;
	}
	
	public void setItem(int slot, ItemStack item) throws IllegalArgumentException, IllegalStateException {
		if(this.isLocked) throw new IllegalStateException("This object has been locked and can not be edited.");
		if(slot < 0 || slot > 8) throw new IllegalArgumentException("Slot must be (inculding both) between 0 and 8");
		if(item==null) {
			matrix.put(slot, null);
			return;
		}
		ItemStack itemOne = item.clone();
		itemOne.setAmount(1);
		matrix.put(slot, itemOne);
	}
	
	public void lock() {
		this.isLocked = true;
	}
	
	@Override
	public int hashCode() {
		return this.matrix.entrySet().stream().mapToInt(entry -> this.itemHashOf(entry.getValue(), entry.getKey())).sum();
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if(otherObject == this) return true;
		ShapedCraftingMatrix other = (ShapedCraftingMatrix) otherObject;
		if(other == this) return true;
		if(other.hashCode()==this.hashCode()) return true;
		return false;
	}
	
	private int itemHashOf(ItemStack item, int slot) throws IllegalArgumentException{
		
		if(slot < 0 || slot > 8) throw new IllegalArgumentException("Slot must be (inculding both) between 0 and 8");
		
		int hash = slot * 33;
		
		if(item==null || item.getType().equals(Material.AIR)) {
			hash += 111111;
		}else {
			hash += item.hashCode();
		}
		
		return hash;
	}
}
