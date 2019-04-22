package com.gestankbratwurst.PowerTurrets.recipeLib.shapeless;

import java.util.HashSet;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.Getter;

public class CustomShapelessRecipe {
	
	public static Map<ShapelessCraftingMatrix, ItemStack> RecipeList = Maps.newHashMap();
	public static Map<ItemStack, HashSet<ShapelessCraftingMatrix>> ResultList = Maps.newHashMap();
	
	public CustomShapelessRecipe(ItemStack result) {
		this.result = result;
		this.matrix = new ShapelessCraftingMatrix();
	}
	
	@Getter
	private final ItemStack result;
	private final ShapelessCraftingMatrix matrix;
	
	public CustomShapelessRecipe addItem(ItemStack item) {
		this.matrix.addItem(item);
		return this;
	}
	
	public CustomShapelessRecipe addMatrix(ItemStack[] items) throws IllegalArgumentException {
		if(items.length != 9) throw new IllegalArgumentException("Matrix must have 9 entrys.");
		for(int slot = 0; slot < 9; slot++) {
			this.addItem(items[slot]);
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