package me.mintoyatsu.hardmode;

import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class HardModeDamageListener extends EntityListener {
    public static HardMode plugin; public HardModeDamageListener(HardMode instance) {
        plugin = instance;
    }

    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            // we don't care about non-player entities being damaged
            return;
        }
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent)event;
            Entity damager = subEvent.getDamager();
            if (subEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                // find attacking Skeleton/Player entity
                damager = ((Projectile)damager).getShooter();
            }
            if (damager == null) {
                // Dispenser / unknown
                return;
            }
            switch (event.getCause()) {
                case PROJECTILE:
                    // fallthrough
                case ENTITY_ATTACK: {
                    if (damager instanceof Zombie ||
                            damager instanceof Skeleton ||
                            damager instanceof Spider ||
                            damager instanceof PigZombie ||
                            damager instanceof Slime ||
                            damager instanceof Wolf) {
                        event.setDamage(event.getDamage() * 3);
                    }
                    break;
                }
                case ENTITY_EXPLOSION: {
                    if (damager instanceof TNTPrimed) {
                        // normal damage for TNT
                        break;
                    }
                    // Creepers and Ghasts
                    event.setDamage(event.getDamage() * 2);
                    break;
                }
                default: {
                    // normal damage for all other damage types
                    break;
                }
            }
        } else {
            // we don't care about block damage
            return;
        }
    }
}
