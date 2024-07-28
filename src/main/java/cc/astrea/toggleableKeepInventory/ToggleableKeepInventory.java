package cc.astrea.toggleableKeepInventory;

import org.bukkit.*;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToggleableKeepInventory extends JavaPlugin implements Listener {
    NamespacedKey key;

    @Override
    public void onEnable() {
        key = new NamespacedKey(this, "astrea-tss-keepinv");
        getCommand("keepinv").setExecutor(new KeepInvCommand(key));
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public static int getExperienceValue(int n) {
        // taken from minecraft source code. values above 100 were removed
        if (n >= 73) {
            return 73;
        }
        if (n >= 37) {
            return 37;
        }
        if (n >= 17) {
            return 17;
        }
        if (n >= 7) {
            return 7;
        }
        if (n >= 3) {
            return 3;
        }
        return 1;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Boolean keepInvValue = event.getPlayer().getPersistentDataContainer().get(key, BooleanType.BOOLEAN);
        if (keepInvValue == null) {
            return;
        }

        if (keepInvValue && !event.getKeepInventory()) {
            event.setKeepInventory(true);
            event.setKeepLevel(true);
            event.setShouldDropExperience(false);
            event.getDrops().clear();
        } else if (!keepInvValue && event.getKeepInventory()) {
            World world = event.getPlayer().getWorld();
            Location location = event.getPlayer().getLocation();

            // drop items, since paper won't do that
            for (ItemStack item : event.getPlayer().getInventory()) {
                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }
                world.dropItemNaturally(location, item);
            }

            event.setKeepInventory(false);
            event.setKeepLevel(false);
            event.setShouldDropExperience(true);

            // experience needs to be "dropped" since the above toggle only means we don't get xp back on respawn
            // minecraft uses something similar to this system internally, based on decompiles
            int experienceToDrop = event.getPlayer().getLevel() * 7;
            if (experienceToDrop > 100) {
                experienceToDrop = 100;
            }

            while (experienceToDrop > 0) {
                int orbExperience = getExperienceValue(experienceToDrop);
                experienceToDrop -= orbExperience;
                ExperienceOrb orb = world.spawn(location, ExperienceOrb.class);
                orb.setExperience(orb.getExperience() + orbExperience); // for stacking xp orbs
            }
        }
    }
}
