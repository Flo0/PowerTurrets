package com.gestankbratwurst.PowerTurrets.recipeLib.shaped;

import java.util.HashSet;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.Getter;

public class CustomShapedRecipe {
	
	public static Map<ShapedCraftingMatrix, ItemStack> RecipeList = Maps.newHashMap();
	public static Map<ItemStack, HashSet<ShapedCraftingMatrix>> ResultList = Maps.newHashMap();
	
	public CustomShapedRecipe(ItemStack result) {
		this.result = result;
		this.matrix = new ShapedCraftingMatrix();
	}
	
	@Getter
	private final ItemStack result;
	private final ShapedCraftingMatrix matrix;
	
	public CustomShapedRecipe setItem(int slot, ItemStack item) throws IllegalArgumentException {
		if(slot < 0 || slot > 8) throw new IllegalArgumentException("Slot must be (inculding both) between 0 and 8");
		this.matrix.setItem(slot, item);
		return this;
	}
	
	public CustomShapedRecipe setMatrix(Map<Integer, ItemStack> matrix) throws IllegalArgumentException {
		if(matrix.size() != 9) throw new IllegalArgumentException("Matrix must have 9 entrys.");
		matrix.forEach((slot,item) -> this.setItem(slot, item));
		return this;
	}
	
	public CustomShapedRecipe setMatrix(ItemStack[] items) throws IllegalArgumentException {
		if(items.length != 9) throw new IllegalArgumentException("Matrix must have 9 entrys.");
		for(int slot = 0; slot < 9; slot++) {
			this.setItem(slot, items[slot]);
		}
		return this;
	}
	
	public void register() {
		
		this.matrix.lock();
		
		RecipeList.put(matrix, result);
		if(ResultList.containsKey(result)) {
			ResultList.get(result).add(matrix);
		}else {
			ResultList.put(result, Sets.newHashSet(matrix));
		}
	}
	
	public void delete() {
		RecipeList.remove(this.matrix);
		ResultList.get(this.result).remove(this.matrix);
		if(ResultList.get(result).isEmpty()) {
			ResultList.remove(result);
		}
	}
	
}