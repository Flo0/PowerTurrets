package com.gestankbratwurst.PowerTurrets.recipeLib.furnace;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import com.gestankbratwurst.PowerTurrets.Core;
import com.google.common.collect.Maps;

public class CustomFurnaceRecipe extends FurnaceRecipe{
	
	public static Map<ItemStack, ItemStack> RecipeList = Maps.newHashMap();
	
	public static boolean isRecipeInput(ItemStack item) {
		ItemStack oneItem = item.clone();
		oneItem.setAmount(1);
		return RecipeList.containsKey(oneItem);
	}
	
	public CustomFurnaceRecipe(String name, ItemStack input, ItemStack output, float experience, int cookingTime) {
		super(new NamespacedKey(Core.getPlugin(), name), output.clone(), input.getType(), experience, cookingTime);
		input.setAmount(1);
		this.input = input;
		this.output = output.clone();
	}
	
	private final ItemStack input;
	private final ItemStack output;
	
	public void register() {
		Bukkit.addRecipe(this);
		RecipeList.put(this.input, this.output);
	}
}
