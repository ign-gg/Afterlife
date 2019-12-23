package com.steve.nukkit.AfterLife.events;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Position;
import com.steve.nukkit.AfterLife.AfterLife;
import com.steve.nukkit.AfterLife.Main;

public class CustomEvent implements Listener {

    private Main plugin;

    public CustomEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onEvent(EntityDamageEvent event) {
        Player victim = (Player) event.getEntity();
        Position level = this.plugin.getServer().getDefaultLevel().getSafeSpawn();
        if (victim != null) {
            if (event.getFinalDamage() >= victim.getHealth()){
                event.setCancelled(true);
                victim.setHealth(victim.getMaxHealth());
                victim.teleport(level);

                AfterLife.deaths.add(victim.getUniqueId().toString());
                if (plugin.getConfig().getBoolean("use-levels")) {
                    AfterLife.experience.remove(victim.getUniqueId().toString(), plugin.getConfig().getInt("loose-xp-amount"));
                }

                switch (event.getCause()) {
                    case CONTACT:
                        plugin.getServer().broadcastMessage(victim.getName()+" Was killed by contact with another block!");
                        break;
                    case ENTITY_ATTACK:
                        plugin.getServer().broadcastMessage(victim.getName()+" was killed by another entity!");
                        break;
                    case PROJECTILE:
                        plugin.getServer().broadcastMessage(victim.getName()+" was killed by a projectile attack!");
                        break;
                    case SUFFOCATION:
                        plugin.getServer().broadcastMessage(victim.getName()+" Suffocated in a wall!");
                        break;
                    case FALL:
                        plugin.getServer().broadcastMessage(victim.getName()+" Fell from a high place!");
                        break;
                    case FIRE:
                        plugin.getServer().broadcastMessage(victim.getName()+" Went up in flames!");
                        break;
                    case FIRE_TICK:
                        plugin.getServer().broadcastMessage(victim.getName()+" Burned to death!");
                        break;
                    case LAVA:
                        plugin.getServer().broadcastMessage(victim.getName()+" Tried to swim in lava!");
                        break;
                    case DROWNING:
                        plugin.getServer().broadcastMessage(victim.getName()+" Drown!");
                        break;
                    case BLOCK_EXPLOSION:
                    case ENTITY_EXPLOSION:
                        plugin.getServer().broadcastMessage(victim.getName()+" Blew up!");
                        break;
                    case VOID:
                        plugin.getServer().broadcastMessage(victim.getName()+" Fell into the void!");
                        break;
                    case SUICIDE:
                        plugin.getServer().broadcastMessage(victim.getName()+" Committed suicide!");
                        break;
                    case MAGIC:
                        plugin.getServer().broadcastMessage(victim.getName()+" Was killed by magic!");
                        break;
                    case LIGHTNING:
                        plugin.getServer().broadcastMessage(victim.getName()+" Was struck by lightning!");
                        break;
                    case HUNGER:
                        plugin.getServer().broadcastMessage(victim.getName()+" Starved to death!");
                        break;
                    default:
                        plugin.getServer().broadcastMessage(victim.getName()+" Died!");
                }

            }

            if (event instanceof EntityDamageByEntityEvent) {
                Player killer = (Player) ((EntityDamageByEntityEvent) event).getDamager();
                if (killer != null) {
                    AfterLife.deaths.add(victim.getUniqueId().toString());
                    AfterLife.kills.add(killer.getUniqueId().toString());
                    plugin.getServer().broadcastMessage(victim.getName()+" was killed by "+killer.getName());
                    if (plugin.getConfig().getBoolean("use-levels")) {
                        AfterLife.experience.remove(victim.getUniqueId().toString(), plugin.getConfig().getInt("loose-xp-amount"));
                        AfterLife.experience.add(killer.getUniqueId().toString(), plugin.getConfig().getInt("add-xp-amount"));
                    }
                }
            }
        }
    }
}
