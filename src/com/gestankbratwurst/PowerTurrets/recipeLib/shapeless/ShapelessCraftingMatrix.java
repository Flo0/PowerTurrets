package com.gestankbratwurst.PowerTurrets.recipeLib.shapeless;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

public class ShapelessCraftingMatrix {
	
	public ShapelessCraftingMatrix(CraftingInventory inv) {
		this.ingredients = Lists.newArrayList();
		ItemStack[] itemMatrix = inv.getMatrix();
		for(int slot = 0; slot < itemMatrix.length; slot++) {
			ItemStack item = itemMatrix[slot];
			if(item==null) {
				continue;
			}
			ItemStack oneItem = item.clone();
			oneItem.setAmount(1);
			this.ingredients.add(oneItem);
		}
		this.isLocked = false;
	}
	
	public ShapelessCraftingMatrix() {
		this.ingredients = Lists.newArrayList();
		this.isLocked = false;
	}
	
	private final List<ItemStack> ingredients;
	private boolean isLocked;
	
	public void singleRemoveFrom(CraftingInventory inv) {
		ItemStack[] invMatrix = inv.getMatrix();
		for(ItemStack item : this.ingredients) {
			ItemStack current;
			for(int slot = 0; slot < invMatrix.length; slot++) {
				current = invMatrix[slot];
				if(current==null) continue;
				if(current.hashCode() == item.hashCode()) {
					if(current.getAmount() == 1) {
						inv.setItem(slot+1, new ItemStack(Material.AIR));
					}else {
						current.setAmount(current.getAmount() - 1);
					}
				}
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
			int left = present - amount;
			
			if(left == 0) {
				inv.setItem(slot+1, new ItemStack(Material.AIR));
			}else {
				item.setAmount(left);
			}
		}
		
		return amount;
	}
	
	public List<ItemStack> getIngredients() throws IllegalStateException{
		if(this.isLocked) throw new IllegalStateException("This object has been locked and can not be edited.");
		return this.ingredients;
	}
	
	public void addItem(ItemStack item) throws IllegalStateException {
		if(this.isLocked) throw new IllegalStateException("This object has been locked and can not be edited.");
		if(this.ingredients.size() > 9) throw new IllegalStateException("Cant have more than 9 ingredients in a ShapelessCraftingMatrix.");
		if(item==null) return;
		ItemStack itemOne = item.clone();
		itemOne.setAmount(1);
		this.ingredients.add(itemOne);
	}
	
	public void lock() {
		this.isLocked = true;
	}
	
	@Override
	public int hashCode() {
		return this.ingredients.stream().mapToInt(entry -> this.itemHashOf(entry, this.ingredients.size())).sum();
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if(otherObject == this) return true;
		ShapelessCraftingMatrix other = (ShapelessCraftingMatrix) otherObject;
		if(other == this) return true;
		if(other.hashCode()==this.hashCode()) return true;
		return false;
	}
	
	private int itemHashOf(ItemStack item, int index) {
		
		int hash = 332211 + index * 17;
		
		if(item==null || item.getType().equals(Material.AIR)) {
			hash += 111111;
		}else {
			hash += item.hashCode();
		}
		
		return hash;
	}
}
