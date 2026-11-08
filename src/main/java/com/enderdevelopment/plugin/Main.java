package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener, CommandExecutor {
    private NamespacedKey cannonKey;
    private NamespacedKey usesKey;

    @Override
    public void onEnable() {
        cannonKey = new NamespacedKey(this, "warden_cannon");
        usesKey = new NamespacedKey(this, "uses_remaining");
        Bukkit.getPluginManager().registerEvents(this, this);
        if (getCommand("wardencannon") != null) {
            getCommand("wardencannon").setExecutor(this);
        }
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }
        if (args.length != 1 || !args[0].equalsIgnoreCase("shot")) {
            player.sendMessage(Component.text("Usage: /wardencannon shot", NamedTextColor.YELLOW));
            return true;
        }

        player.getInventory().addItem(createCannonTrident());
        player.sendMessage(Component.text("Warden Cannon trident received. It has 4 shots.", NamedTextColor.DARK_AQUA));
        return true;
    }

    private ItemStack createCannonTrident() {
        ItemStack trident = new ItemStack(Material.TRIDENT);
        ItemMeta meta = trident.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("Warden Cannon", NamedTextColor.DARK_AQUA));
            meta.getPersistentDataContainer().set(cannonKey, PersistentDataType.BYTE, (byte) 1);
            meta.getPersistentDataContainer().set(usesKey, PersistentDataType.INTEGER, 4);
            trident.setItemMeta(meta);
        }
        return trident;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Trident trident)) {
            return;
        }
        ItemStack item = trident.getItemStack();
        ItemMeta meta = item.getItemMeta();
        if (meta == null || meta.getPersistentDataContainer().get(cannonKey, PersistentDataType.BYTE) == null) {
            return;
        }

        Integer uses = meta.getPersistentDataContainer().get(usesKey, PersistentDataType.INTEGER);
        if (uses == null || uses <= 0) {
            return;
        }
        meta.getPersistentDataContainer().set(usesKey, PersistentDataType.INTEGER, uses - 1);
        item.setItemMeta(meta);
        triggerImpact(trident.getLocation());
    }

    private void triggerImpact(Location impact) {
        World world = impact.getWorld();
        if (world == null) {
            return;
        }
        Location center = impact.getBlock().getLocation().add(0.5, 0.5, 0.5);
        world.createExplosion(center, 3.5F, true, true);
        spawnSonicBoomColumn(center);
    }

    private void spawnSonicBoomColumn(Location center) {
        World world = center.getWorld();
        if (world == null) {
            return;
        }
        int startY = Math.max(world.getMinHeight(), center.getBlockY());
        int highestY = world.getMaxHeight() - 1;
        int verticalRange = Math.max(0, highestY - startY);
        for (int index = 0; index < 5; index++) {
            int boomY = startY + (verticalRange * index / 4);
            long delay = index;
            Bukkit.getScheduler().runTaskLater(this, () -> {
                Location boomLocation = new Location(world, center.getX(), boomY, center.getZ());
                world.spawnParticle(org.bukkit.Particle.SONIC_BOOM, boomLocation, 1, 0, 0, 0, 0);
                world.playSound(boomLocation, org.bukkit.Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0F, 1.0F);
            }, delay);
        }
    }
}
