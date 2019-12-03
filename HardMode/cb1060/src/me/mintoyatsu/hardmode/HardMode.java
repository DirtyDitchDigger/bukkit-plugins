package me.mintoyatsu.hardmode;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HardMode extends JavaPlugin {
    private Logger log;
    private final HardModeDamageListener damageListener;

    public HardMode() {
        this.log = Logger.getLogger("Minecraft");
        this.damageListener = new HardModeDamageListener(this);
    }

    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, damageListener, Event.Priority.Lowest, this);
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
}
