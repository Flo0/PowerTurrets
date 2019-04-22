package com.gestankbratwurst.PowerTurrets.fileIO;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gestankbratwurst.PowerTurrets.Core;

import lombok.Getter;

public class FileManager {
	
	public FileManager(Core plugin) {
		this.mainFile = new File(plugin.getDataFolder(), "config.yml");
		this.createFiles(plugin);
		this.loadConfigurations();
	}
	
	private final File mainFile;
	@Getter
	private FileConfiguration mainConfig;
	
	private void createFiles(Core plugin) {
		if(plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}
		if(!this.mainFile.exists()) {
			plugin.saveResource("config.yml", false);
		}
	}
	
	private void loadConfigurations() {
		this.mainConfig = YamlConfiguration.loadConfiguration(this.mainFile);
	}
}
