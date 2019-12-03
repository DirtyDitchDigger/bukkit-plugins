package me.mintoyatsu.randomspawn;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.File;

public class RandomSpawnPlayerListener extends PlayerListener {
    public int xMin;
    public int xMax;
    public int zMin;
    public int zMax;
    public boolean avoidHazards;

    public static RandomSpawn plugin; public RandomSpawnPlayerListener(RandomSpawn instance) {
        plugin = instance;
    }

    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.isBedSpawn()) {
            return;
        }
        // no bed, randomize spawn
        World world = Bukkit.getServer().getWorlds().get(0);
        Location spawn = getRandomLocation(world);
        event.setRespawnLocation(spawn);
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = Bukkit.getServer().getWorlds().get(0);
        String name = player.getName();
        File file = new File(String.valueOf(world.getName()) + "/players/" + name + ".dat");
        if (file.exists()) {
            return;
        }
        // first join, randomize spawn
        Location spawn = getRandomLocation(world);
        player.teleport(spawn);
    }

    public boolean isSafeLocation(World world, Location location) {
        if (!avoidHazards) {
            return true;
        }
        Location locationBlock = new Location(world, location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
        return locationBlock.getBlock().getType() != Material.STATIONARY_WATER &&
               locationBlock.getBlock().getType() != Material.WATER &&
               locationBlock.getBlock().getType() != Material.STATIONARY_LAVA &&
               locationBlock.getBlock().getType() != Material.LAVA &&
               locationBlock.getBlock().getType() != Material.CACTUS;
    }

    public Location getRandomLocation(World world) {
        Location location = new Location(world, 0.0, 0.0, 0.0);
        do {
            int xRand = xMin + (int)(Math.random() * (xMax - xMin) + 0.5);
            int zRand = zMin + (int)(Math.random() * (zMax - zMin) + 0.5);
            location.setX(Double.parseDouble(Integer.toString(xRand)));
            location.setZ(Double.parseDouble(Integer.toString(zRand)));
            Chunk chunk = world.getChunkAt(location);
            if (!world.isChunkLoaded(chunk)) {
                world.loadChunk(chunk);
            }
            int yTop = world.getHighestBlockYAt(xRand, zRand);
            location.setY(Double.parseDouble(Integer.toString(yTop)));
            //plugin.out("Trying spawn at X " + location.getBlockX() + " Y " + location.getBlockY() + " Z " + location.getBlockZ());
        } while (!isSafeLocation(world, location));
        return location;
    }
}
