package com.gestankbratwurst.PowerTurrets.recipeLib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.gestankbratwurst.PowerTurrets.recipeLib.furnace.CustomFurnaceRecipe;

import lombok.Getter;
import net.crytec.api.itemstack.ItemBuilder;

public class RecipeLibCore {
	
	private static RecipeLibCore instance;
	
	public static RecipeLibCore load(JavaPlugin plugin) {
		if(instance == null) {
			instance = new RecipeLibCore(plugin);
		}
		return instance;
	}
	
	private RecipeLibCore(JavaPlugin plugin) {
		this.plugin = plugin;
		this.listener = new RecipeListener();
		
		this.registerListener();
		
		ExactChoice stair = new RecipeChoice.ExactChoice(new ItemBuilder(Material.ACACIA_STAIRS).name("MEGA TREPPE").build());
		
		ShapedRecipe recipeEC = new ShapedRecipe(new NamespacedKey(plugin, "test-rezept"), new ItemBuilder(Material.DIAMOND_SWORD).name("MEGA SWORD").amount(64).build());
		
		recipeEC.shape(" S "," S "," S ");
		
		recipeEC.setIngredient('S', stair);
		
		Bukkit.addRecipe(recipeEC);
		
		Bukkit.recipeIterator().forEachRemaining(recipe ->{
			if(recipe instanceof FurnaceRecipe) {
				ItemStack in = ((FurnaceRecipe) recipe).getInput();
				ItemStack out = ((FurnaceRecipe) recipe).getResult();
				CustomFurnaceRecipe.RecipeList.put(in, out);
			}
			
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.ACACIA_LOG), new ItemStack(Material.CHARCOAL));
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.BIRCH_LOG), new ItemStack(Material.CHARCOAL));
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.DARK_OAK_LOG), new ItemStack(Material.CHARCOAL));
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.JUNGLE_LOG), new ItemStack(Material.CHARCOAL));
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.OAK_LOG), new ItemStack(Material.CHARCOAL));
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.SPRUCE_LOG), new ItemStack(Material.CHARCOAL));
			
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.STRIPPED_ACACIA_LOG), new ItemStack(Material.CHARCOAL));
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.STRIPPED_BIRCH_LOG), new ItemStack(Material.CHARCOAL));
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.STRIPPED_DARK_OAK_LOG), new ItemStack(Material.CHARCOAL));
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.STRIPPED_JUNGLE_LOG), new ItemStack(Material.CHARCOAL));
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.STRIPPED_OAK_LOG), new ItemStack(Material.CHARCOAL));
			CustomFurnaceRecipe.RecipeList.put(new ItemStack(Material.STRIPPED_SPRUCE_LOG), new ItemStack(Material.CHARCOAL));

		});
	}
	
	@Getter
	private final JavaPlugin plugin;
	private final RecipeListener listener;
	
	private void registerListener() {
		Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
	}
}