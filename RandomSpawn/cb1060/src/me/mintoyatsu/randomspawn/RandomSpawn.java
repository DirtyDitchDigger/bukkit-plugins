package me.mintoyatsu.randomspawn;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class RandomSpawn extends JavaPlugin {
    private Logger log;
    public final RandomSpawnPlayerListener playerListener;

    public RandomSpawn() {
        this.log = Logger.getLogger("Minecraft");
        this.playerListener = new RandomSpawnPlayerListener(this);
    }

    public void onEnable() {
        this.loadConfig();
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Event.Priority.Lowest, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Lowest, this);
        this.out("Enabled");
    }

    public void onDisable() {
        this.out("Disabled");
    }

    public void out(final String str) {
        final PluginDescriptionFile description = this.getDescription();
        this.log.info("[" + description.getName() + " " + description.getVersion() + "] " + str);
    }

    public void crap(final String str) {
        final PluginDescriptionFile description = this.getDescription();
        this.log.severe("[" + description.getName() + " " + description.getVersion() + "] " + str);
    }

    public void loadConfig() {
        final File file = new File(this.getDataFolder(), "config.yml");
        final File dataFolder = this.getDataFolder();
        if (!file.exists()) {
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            FileWriter writer = null;
            try {
                file.createNewFile();
                writer = new FileWriter(file);
                writer.write("# Tries to avoid spawning above water, lava and cactus.\r\n");
                writer.write("# This can cause more chunk loading if enabled.\r\n");
                writer.write("avoidHazards: true\r\n");
                writer.write("# Minimum X coordinate for spawn rectangle\r\n");
                writer.write("xMin: -1000\r\n");
                writer.write("# Maximum X coordinate for spawn rectangle\r\n");
                writer.write("xMax: 1000\r\n");
                writer.write("# Minimum Z coordinate for spawn rectangle\r\n");
                writer.write("zMin: -1000\r\n");
                writer.write("# Maximum Z coordinate for spawn rectangle\r\n");
                writer.write("zMax: 1000\r\n");
            }
            catch (IOException ex) {
                ex.printStackTrace();
                this.crap("Error creating config file: " + ex.getMessage());
            }
            finally {
                if (writer != null) {
                    try {
                        writer.close();
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                        this.crap("Error closing config file: " + ex.getMessage());
                    }
                }
            }
        }
        final Configuration config = new Configuration(file);
        config.load();
        playerListener.xMin = config.getInt("xMin", -1000);
        playerListener.xMax = config.getInt("xMax", 1000);
        playerListener.zMin = config.getInt("zMin", -1000);
        playerListener.zMax = config.getInt("zMax", 1000);
        playerListener.avoidHazards = config.getBoolean("avoidHazards", true);
    }
}
