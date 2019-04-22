package com.gestankbratwurst.PowerTurrets;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.gestankbratwurst.PowerTurrets.Threads.TurretTickThread;
import com.gestankbratwurst.PowerTurrets.commands.DebugCommands;
import com.gestankbratwurst.PowerTurrets.fileIO.FileManager;
import com.gestankbratwurst.PowerTurrets.fileIO.Language;
import com.gestankbratwurst.PowerTurrets.listener.DebugListener;
import com.gestankbratwurst.PowerTurrets.listener.TurretInteractionListener;
import com.gestankbratwurst.PowerTurrets.manager.TurretManager;
import com.gestankbratwurst.PowerTurrets.recipeLib.RecipeLibCore;
import com.gestankbratwurst.PowerTurrets.util.TurretType;

import lombok.Getter;
import net.crytec.api.devin.commands.CommandRegistrar;
import net.crytec.api.devin.commands.ObjectParsing;
import net.crytec.shaded.org.apache.lang3.EnumUtils;

public class Core extends JavaPlugin{
	
	@Getter
	private static Core plugin;
	@Getter
	private TurretManager turretManager;
	@Getter
	private FileManager fileManager;
	
	@Override
	public void onLoad() {
		plugin = this;
		
		// Register TurretType Enum Parser
		ObjectParsing.registerParser(TurretType.class, (args) -> {
			String result = args.next();
			if (!EnumUtils.isValidEnum(TurretType.class, result)) {
				throw new IllegalArgumentException("There is no TurretType with the name \"" + result + "\"");
			} else {
				return TurretType.valueOf(result);
			}
		});
	}
	
	@Override
	public void onEnable() {
		this.loadLanguage();
		this.turretManager = new TurretManager();
		this.fileManager = new FileManager(this);
		CommandRegistrar cr = new CommandRegistrar(this);
		cr.registerCommands(new DebugCommands());
		
		RecipeLibCore.load(this);
		
		Bukkit.getPluginManager().registerEvents(new DebugListener(), this);
		Bukkit.getPluginManager().registerEvents(new TurretInteractionListener(this.turretManager), this);
		
		Bukkit.getScheduler().runTaskTimer(this, new TurretTickThread(), 20, 5);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void loadLanguage() {
		File lang = new File(this.getDataFolder(), "language.yml");
		if (!lang.exists()) {
			try {
				this.getDataFolder().mkdir();
				lang.createNewFile();
				if (lang != null) {
					YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(lang);
					defConfig.save(lang);
					Language.setFile(defConfig);
				}
			} catch (IOException e) {
				this.getLogger().severe("Could not create language file!");
				Bukkit.getPluginManager().disablePlugin(this);
			}
		}

		YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
		for (Language item : Language.values()) {
			if (conf.getString(item.getPath()) == null) {
				if (item.isArray()) {
					conf.set(item.getPath(), item.getDefArray());
				} else {
					conf.set(item.getPath(), item.getDefault());
				}

			}
		}
		Language.setFile(conf);
		try {
			conf.save(lang);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
