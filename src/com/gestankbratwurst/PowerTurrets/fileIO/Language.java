package com.gestankbratwurst.PowerTurrets.fileIO;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
 
/**
* An enum for requesting strings from the language file.
*/

public enum Language {
	ERROR_BREAK_NOT_OWNER("error.notOwnerBreaking", "&cThis Tower can only be broken by the owner."),
	ERROR_MOBSPAWN_DISABLED("error.mobspawnDisabled", "&cTurrets can only be placed if mobs can spawn in this world."),
	
	PROJECTILE_VELOCITY_SLOW("ProjectileVelocity.SLOW", "Slow"),
	PROJECTILE_VELOCITY_NORMAL("ProjectileVelocity.NORMAL", "Normal"),
	PROJECTILE_VELOCITY_FAST("ProjectileVelocity.FAST", "Fast"),
	PROJECTILE_VELOCITY_VERY_FAST("ProjectileVelocity.VERY_FAST", "Very Fast"),
	PROJECTILE_VELOCITY_SUDDEN_IMPACT("ProjectileVelocity.SUDDEN_IMPACT", "Sudden Impact"),
	
	FIRE_RATE_LETHARGIC("FireRate.LETHARGIC", "Lethargic"),
	FIRE_RATE_VERY_SLOW("FireRate.VERY_SLOW", "Very Slow"),
	FIRE_RATE_SLOW("FireRate.SLOW", "Slow"),
	FIRE_RATE_SLOWER("FireRate.SLOWER", "Slower"),
	FIRE_RATE_MODERATED("FireRate.MODERATED", "Moderated"),
	FIRE_RATE_NORMAL("FireRate.NORMAL", "Normal"),
	FIRE_RATE_FAST("FireRate.FAST", "Fast"),
	FIRE_RATE_VERY_FAST("FireRate.VERY_FAST", "Very Fast"),
	FIRE_RATE_RAPID("FireRate.RAPID", "Rapid"),
	
	GENERAL_EXP("General.EXP", "EXP"),
	GENERAL_COST("General.Cost", "Cost"),
	GENERAL_POINTS("General.Points", "Points"),
	GENERAL_LEVEL_UP_COST("General.LevelUpCost", "LevelUpCost"),
	GENERAL_HEALTH("General.Health", "Health"),
	GENERAL_DAMAGE("General.Damage", "Damage"),
	GENERAL_ACCURACY("General.Accuracy", "Accuracy"),
	GENERAL_RANGE("General.Range", "Range"),
	GENERAL_FIRE_RATE("General.FireRate", "Fire Rate"),
	GENERAL_VELOCITY("General.Velocity", "Velocity"),
	GENERAL_STATS("General.Stats", "Stats"),
	GENERAL_DELETE_TURRET("General.Delete_Turret", "Delete Turret"),
	GENERAL_INVENTORY("General.Inventory", "Inventory"),
	GENERAL_DESTROY_CONFIRM("General.ConfirmDeletion", "Confirm deletion"),
	GENERAL_DESTROY_ABORT("General.AbortDeletion", "Abort deletion"),
	GENERAL_MAGAZINE("General.Magazine", "Magazine"),
	GENERAL_TURRET("General.Turret", "Turret"),
	GENERAL_AMMO("General.Ammo", "Ammunition"),
	
	AMMUNITION_TYPE_SHELL("Ammunition.Type.Shell", "Shell"),
	AMMUNITION_TYPE_BOX("Ammunition.Type.Box", "Box"),
	
	MESSAGES_NOT_OWNER("Messages.NotOwnerOfTurret","This Turret does not belong to you."),
	
	LEVEL_UP_FAILED_DESC("Stats.LevelUpDesc","Click to levelUp"),
	LEVEL_UP_FAILED_EXP("Stats.LevelUpFailedExp","Not enought Exp or max level reached."),
	LEVEL_UP_FAILED("Stats.LevelUpFailed","Not enought Points or max value reached."),
	LEVEL_UP_TURRET("Stats.LevelUpTurret", "Leveled up turret."),
	LEVEL_UP_HEALTH("Stats.LevelUpHealth", "Leveled up health."),
	LEVEL_UP_DAMAGE("Stats.LevelUpDamage", "Leveled up damage."),
	LEVEL_UP_ACCURACY("Stats.LevelUpAccuracy", "Leveled up accuracy."),
	LEVEL_UP_RANGE("Stats.LevelUpRange", "Leveled up range."),
	LEVEL_UP_FIRE_RATE("Stats.LevelUpFireRate", "Leveled up fire rate."),
	LEVEL_UP_PROJECTILE_VELOCITY("Stats.LevelUpPojectileVelocity", "Leveled up muzzle velocity."),
	
	TURRET_BASIC_TURRET("Turrets.BASIC_TURRET", "Turret"),
	
	TITLE("title-name", "&2[&fPowerTurrets&2]");
	
	
    private String path;
    private String def;
    private boolean isArray = false;
    
    private List<String> defArray;
    private static YamlConfiguration LANG;
 
    /**
    * Lang enum constructor.
    * @param path The string path.
    * @param start The default string.
    */
    private Language(String path, String start) {
        this.path = path;
        this.def = start;
    }
    
    private Language(String path, List<String> start) {
        this.path = path;
        this.defArray = start;
        this.isArray = true;
    }
    
    /**
    * Set the {@code YamlConfiguration} to use.
    * @param config The config to set.
    */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }
    
    public static YamlConfiguration getFile() {
    	return LANG;
    }
    
    @Override
    public String toString() {
        if (this == TITLE) return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def)) + " ";
        return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
    }
    
    /**
     * Get the String with the TITLE
     * @return
     */
    public String toChatString() {
    	return TITLE.toString() + ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
    }
    
    public List<String> getDescriptionArray() {
    	return LANG.getStringList(this.path).stream().map(x -> ChatColor.translateAlternateColorCodes('&', x)).collect(Collectors.toList());
    }
    
    public boolean isArray() {
    	return this.isArray;
    }
    
    public List<String> getDefArray() {
    	return this.defArray;
    }
     
    /**
    * Get the default value of the path.
    * @return The default value of the path.
    */
    public String getDefault() {
        return this.def;
    }
 
    /**
    * Get the path to the string.
    * @return The path to the string.
    */
    public String getPath() {
        return this.path;
    }
}
