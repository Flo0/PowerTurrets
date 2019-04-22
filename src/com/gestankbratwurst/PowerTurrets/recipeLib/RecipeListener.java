package com.gestankbratwurst.PowerTurrets.recipeLib;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gestankbratwurst.PowerTurrets.recipeLib.furnace.CustomFurnaceRecipe;
import com.gestankbratwurst.PowerTurrets.recipeLib.shaped.CustomShapedRecipe;
import com.gestankbratwurst.PowerTurrets.recipeLib.shaped.ShapedCraftingMatrix;
import com.gestankbratwurst.PowerTurrets.recipeLib.shapeless.CustomShapelessRecipe;
import com.gestankbratwurst.PowerTurrets.recipeLib.shapeless.ShapelessCraftingMatrix;
import com.google.common.collect.Lists;

import net.crytec.api.itemstack.ItemBuilder;


public class RecipeListener implements Listener{
	
	@EventHandler
	public void onFurnaceSmelt(FurnaceSmeltEvent event) {
		ItemStack input = event.getSource().clone();
		ItemStack preOut = event.getResult();
		input.setAmount(1);
		if(!preOut.hasItemMeta()) return;
		if(!preOut.getItemMeta().hasDisplayName()) return;
		if(!CustomFurnaceRecipe.RecipeList.containsKey(input)) return;
		System.out.println("Key is here");
		event.setResult(CustomFurnaceRecipe.RecipeList.get(input));
	}
	
	@EventHandler
	public void onFurnaceClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		if(inv == null) return;
		if(event.getRawSlot() < 0) return;
		if(!inv.getType().equals(InventoryType.FURNACE)) return;
		InventoryAction action = event.getAction();
		
		switch(action) {
		case MOVE_TO_OTHER_INVENTORY:
			if(Lists.newArrayList(0,1,2).contains(event.getSlot())) return;
			if(!CustomFurnaceRecipe.isRecipeInput(event.getCurrentItem())) {
				event.setResult(Result.DENY);
				return;
			}
			if(inv.getItem(0) == null) {
				inv.setItem(0, event.getCurrentItem());
				event.setCurrentItem(new ItemStack(Material.AIR));
			}
		case PLACE_ALL:
			if(event.getSlot() != 0) return;
			event.setCancelled(true);
			if(!CustomFurnaceRecipe.isRecipeInput(event.getCursor())) {
				event.setResult(Result.DENY);
				return;
			}
			ItemStack slot = inv.getItem(0);
			ItemStack cursor = event.getCursor();
			if(slot == null) {
				inv.setItem(0, cursor);
				event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
			}else if(cursor.isSimilar(slot)) {
				int maxSize = slot.getMaxStackSize();
				int cursorSize = cursor.getAmount();
				int slotSize = slot.getAmount();
				int sum = cursorSize + slotSize;
				if(sum > maxSize) {
					slot.setAmount(maxSize);
					event.getWhoClicked().getItemOnCursor().setAmount(sum - maxSize);
				}else {
					slot.setAmount(sum);
					event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
				}
			}
			
		case PLACE_ONE:
			if(event.getSlot() != 0) return;
			event.setCancelled(true);
			if(!CustomFurnaceRecipe.isRecipeInput(event.getCursor())) {
				event.setResult(Result.DENY);
				return;
			}
			
			ItemStack item = event.getCursor().clone();
			item.setAmount(1);
			
			if(event.getCursor().getAmount() == 1) {
				event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
			}else {
				event.getCursor().setAmount(event.getCursor().getAmount() - 1);
			}
			
			if(inv.getItem(0) == null) {
				inv.setItem(0, item);
			}else if(item.isSimilar(inv.getItem(0))) {
				inv.getItem(0).setAmount(inv.getItem(0).getAmount() + 1);
			}
		case HOTBAR_MOVE_AND_READD:
			if(Lists.newArrayList(0,1,2).contains(event.getSlot())) return;
			if(!CustomFurnaceRecipe.isRecipeInput(event.getCurrentItem())) {
				event.setResult(Result.DENY);
				return;
			}
			if(inv.getItem(0) == null) {
				inv.setItem(0, event.getCurrentItem());
				event.setCurrentItem(new ItemStack(Material.AIR));
			}
		case HOTBAR_SWAP:
			if(Lists.newArrayList(0,1,2).contains(event.getSlot())) return;
			if(!CustomFurnaceRecipe.isRecipeInput(event.getCurrentItem())) {
				event.setResult(Result.DENY);
				return;
			}
			if(inv.getItem(0) == null) {
				inv.setItem(0, event.getCurrentItem());
				event.setCurrentItem(new ItemStack(Material.AIR));
			}
		case NOTHING:
			if(Lists.newArrayList(0,1,2).contains(event.getSlot())) return;
			if(!CustomFurnaceRecipe.isRecipeInput(event.getCurrentItem())) {
				event.setResult(Result.DENY);
				return;
			}
			if(inv.getItem(0) == null) {
				inv.setItem(0, event.getCurrentItem());
				event.setCurrentItem(new ItemStack(Material.AIR));
			}
		default: return;
		}
		
	}
	
	@EventHandler
	public void onMoveFurnace(InventoryMoveItemEvent event) {
		Inventory destination = event.getDestination();
		if(!destination.getType().equals(InventoryType.FURNACE)) return;
		if(CustomFurnaceRecipe.RecipeList.containsKey(event.getItem())) return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPrepareCraft(PrepareItemCraftEvent event) {
		CraftingInventory inv = event.getInventory();
		
		if(inv.getType().equals(InventoryType.WORKBENCH)) {
			
			ShapedCraftingMatrix matrix = new ShapedCraftingMatrix(inv);
			
			ItemStack shapedResult = CustomShapedRecipe.RecipeList.get(matrix);
			
			if(shapedResult != null && !shapedResult.getType().equals(Material.AIR)) {
				inv.setResult(shapedResult);
			}
			
			ShapelessCraftingMatrix ingredients = new ShapelessCraftingMatrix(inv);
			
			ItemStack shapelessResult = CustomShapelessRecipe.RecipeList.get(ingredients);
			
			if(shapelessResult != null && !shapelessResult.getType().equals(Material.AIR)) {
				inv.setResult(shapelessResult);
			}
			
		}else if(inv.getType().equals(InventoryType.CRAFTING)) {
			
			ShapelessCraftingMatrix ingredients = new ShapelessCraftingMatrix(inv);
			
			ItemStack shapelessResult = CustomShapelessRecipe.RecipeList.get(ingredients);
			
			if(shapelessResult != null && !shapelessResult.getType().equals(Material.AIR)) {
				inv.setResult(shapelessResult);
			}
		}
		
		
	}
	
	@EventHandler
	public void onWorkbenchClick(InventoryClickEvent event) {
		
		if(event.getRawSlot() < 0) return;
		
		Inventory inv = event.getClickedInventory();
		
		if(inv==null || !(inv instanceof CraftingInventory)) return;
		
		if(!inv.getType().equals(InventoryType.WORKBENCH)) return;
		
		CraftingInventory craft = (CraftingInventory) inv;
		
		if(event.getSlot() != 0) return;
		
		ItemStack result = event.getCurrentItem();
		
		if(CustomShapedRecipe.ResultList.containsKey(result)) {
			if(!event.getClick().equals(ClickType.SHIFT_LEFT)) {
				new ShapedCraftingMatrix(craft).singleRemoveFrom(craft);
				event.getWhoClicked().setItemOnCursor(result);
			}else {
				Player player = (Player) event.getWhoClicked();
				
				int amount = new ShapedCraftingMatrix(craft).fullRemoveFrom(craft) * result.getAmount();
				
				int fullStacks = amount / result.getMaxStackSize();
				int leftover = amount % result.getMaxStackSize();

				ItemStack fullItem = result.clone();
				fullItem.setAmount(fullItem.getMaxStackSize());
				while(fullStacks > 0) {
					player.getInventory().addItem(fullItem);
					fullStacks--;
				}
				fullItem.setAmount(leftover);
				player.getInventory().addItem(fullItem);
			}
			event.setCancelled(true);
		}else if(CustomShapelessRecipe.ResultList.containsKey(result)) {
			if(!event.getClick().equals(ClickType.SHIFT_LEFT)) {
				new ShapelessCraftingMatrix(craft).singleRemoveFrom(craft);
				event.getWhoClicked().setItemOnCursor(result);
			}else {
				Player player = (Player) event.getWhoClicked();
				
				int amount = new ShapelessCraftingMatrix(craft).fullRemoveFrom(craft) * result.getAmount();
				
				int fullStacks = amount / result.getMaxStackSize();
				int leftover = amount % result.getMaxStackSize();

				ItemStack fullItem = result.clone();
				fullItem.setAmount(fullItem.getMaxStackSize());
				while(fullStacks > 0) {
					player.getInventory().addItem(fullItem);
					fullStacks--;
				}
				fullItem.setAmount(leftover);
				player.getInventory().addItem(fullItem);
			}
			event.setCancelled(true);
		}
		
		
	}
	
	//TODO remove
	@EventHandler
	public void playerClose(InventoryCloseEvent event) {
		
		if(!event.getInventory().getType().equals(InventoryType.DISPENSER)) return;
		
		if(!event.getInventory().getName().contains("NEW RECIPE")) return;
		
		ItemStack input = event.getInventory().getItem(4);
		ItemStack ouput = event.getPlayer().getInventory().getItemInMainHand();
		new CustomFurnaceRecipe("RECIPE" + ThreadLocalRandom.current().nextInt(), input, ouput, 0F, 60).register();
//		CustomShapelessRecipe recipe = new CustomShapelessRecipe(event.getPlayer().getInventory().getItemInMainHand());
//		recipe.addMatrix(event.getInventory().getContents());
//		recipe.register();
		event.getPlayer().sendMessage("New Recipe registered.");
	}
	
	@EventHandler
	public void onFurnaceClick(PlayerJoinEvent event) {
		event.getPlayer().getInventory().addItem(new ItemBuilder(Material.ACACIA_STAIRS).name("MEGA TREPPE").amount(64).build());
	}
	
}