package nl.mtvehicles.core.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TNTSpawnListener implements Listener {
   private final Map<UUID, Long> cooldownMap = new HashMap();

   @EventHandler
   public void onPlayerClick(PlayerInteractEvent event) {
      if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
         this.trySpawnTNT(event.getPlayer());
      }
   }

   @EventHandler
   public void onPlayerClickAtEntity(PlayerInteractAtEntityEvent event) {
      this.trySpawnTNT(event.getPlayer());
   }

   private void trySpawnTNT(Player player) {
      if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.AIRPLANE_TNT)) {
         if (player.getInventory().getItemInMainHand().getType() == Material.TNT) {
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof ArmorStand) {
               String customName = vehicle.getCustomName();
               if (customName != null && customName.startsWith("MTVEHICLES_MAINSEAT_")) {
                  String license = customName.replace("MTVEHICLES_MAINSEAT_", "");
                  if (!license.isEmpty() && ((VehicleType)VehicleData.type.get(license)).isAirplane()) {
                     UUID playerId = player.getUniqueId();
                     long currentTime = System.currentTimeMillis();
                     long lastTime = (Long)this.cooldownMap.getOrDefault(playerId, 0L);
                     int cooldown = (Integer)ConfigModule.defaultConfig.get(DefaultConfig.Option.AIRPLANE_COOLDOWN) * 1000;
                     if (currentTime - lastTime >= (long)cooldown) {
                        this.spawnFallingTNT(player);
                        this.cooldownMap.put(playerId, currentTime);
                     }

                  }
               }
            }
         }
      }
   }

   private void spawnFallingTNT(Player player) {
      Location playerLocation = player.getLocation().clone().add((double)0.0F, (double)-1.0F, (double)0.0F);
      player.getWorld().spawn(playerLocation, TNTPrimed.class);
      ItemStack mainHandItem = player.getInventory().getItemInMainHand();
      mainHandItem.setAmount(mainHandItem.getAmount() - 1);
   }
}
