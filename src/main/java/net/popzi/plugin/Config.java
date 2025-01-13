package net.popzi.plugin;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    private final Main main;
    private final File file;
    private FileConfiguration data;

    /**
     * Constructor
     * @param main the main plugin
     */
    public Config(Main main) {
        this.main = main;
        this.file = new File(this.main.getDataFolder(), "config.yml");
        this.SaveDefault(false);
        this.Load();
    }

    /**
     * Method that loads the config from the plugin data folder.
     * Note, that anything in memory will get overwritten.
     */
    public void Load() {
        this.data = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Method to retrieve the currently loaded config file
     * @return the config file as a FileConfiguration
     */
    public FileConfiguration getData() {
        if (this.data == null)
            this.Load();
        return this.data;
    }

    /**
     * Method to save the default `config.yml` file from resources.
     * @param force to force and overwrite any existing data.
     */
    public void SaveDefault(boolean force) {
        if (force)
            this.main.saveResource("config.yml", true);

        if (!this.file.exists()) // Save if the file does not exist
            this.main.saveResource("config.yml", false);
    }
}
