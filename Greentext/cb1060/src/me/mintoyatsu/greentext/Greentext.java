package me.mintoyatsu.greentext;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Greentext extends JavaPlugin {
    private Logger log;
    private final GreentextPlayerListener playerListener;
    
    public Greentext() {
        this.log = Logger.getLogger("Minecraft");
        this.playerListener = new GreentextPlayerListener(this);
    }
    
    public void onEnable() {
        this.loadConfig();
        
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Event.Priority.Lowest, this);
        
        this.out("Enabled!");
    }
    
    public void onDisable() {
        this.out("Disabled!");
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
        final File file = new File(this.getDataFolder(), "settings.yml");
        final File dataFolder = this.getDataFolder();
        final Configuration config = new Configuration(file);
        
        config.load();
        
        playerListener.bGreentext = config.getBoolean("enableGreentext", true);
        playerListener.bRedtext = config.getBoolean("enableRedtext", false);
        playerListener.bYellowtext = config.getBoolean("enableYellowtext", false);
        playerListener.bOrangetext = config.getBoolean("enableOrangetext", false);
        
        if (!file.exists()) {
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            try {
                file.createNewFile();
            }
            catch (IOException ex) {
                ex.printStackTrace();
                this.crap("IOError while creating config file: " + ex.getMessage());
            }
        }
        
        config.save();
    }
}
