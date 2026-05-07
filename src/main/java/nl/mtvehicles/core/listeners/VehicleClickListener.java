package nl.mtvehicles.core.listeners;

import java.util.HashMap;
import java.util.Map;
import nl.mtvehicles.core.events.VehicleEnterEvent;
import nl.mtvehicles.core.events.VehiclePickUpEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class VehicleClickListener extends MTVListener {
   private final Map<String, Long> lastUsage = new HashMap();
   private Entity entity;
   private String license;

   @EventHandler(priority = EventPriority.MONITOR)
   public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      this.entity = event.getRightClicked();
      if (VehicleUtils.isVehicle(this.entity)) {
         event.setCancelled(true);
         if (!this.entity.getCustomName().startsWith("VEHICLE")) {
            String playerName = this.player.getName();
            long currentTime = System.currentTimeMillis();
            long lastUsed = (Long)this.lastUsage.getOrDefault(playerName, 0L);
            if (currentTime - lastUsed >= 500L) {
               this.lastUsage.put(playerName, currentTime);
               this.license = VehicleUtils.getLicensePlate(this.entity);
               if (this.player.isSneaking()) {
                  this.pickup();
               } else {
                  this.enter();
               }
            }
         }
      }
   }

   private void pickup() {
      this.setAPI(new VehiclePickUpEvent());
      VehiclePickUpEvent api = (VehiclePickUpEvent)this.getAPI();
      api.setLicensePlate(this.license);
      this.callAPI();
      if (!this.isCancelled()) {
         this.license = api.getLicensePlate();
         Vehicle vehicle = VehicleUtils.getVehicle(this.license);
         if (vehicle != null) {
            boolean disablePickupFromWater = (Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.DISABLE_PICKUP_FROM_WATER);
            if (!this.player.hasPermission("mtvehicles.anwb") && disablePickupFromWater && this.entity.getLocation().clone().add((double)0.0F, (double)1.0F, (double)0.0F).getBlock().isLiquid()) {
               ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.VEHICLE_IN_WATER);
            } else if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.PICKUP, vehicle.getVehicleType(), this.entity.getLocation(), this.player)) {
               ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.CANNOT_DO_THAT_HERE);
            } else {
               VehicleUtils.pickupVehicle(this.license, this.player);
            }
         }
      }
   }

   private void enter() {
      Vehicle vehicle = VehicleUtils.getVehicle(this.license);
      if (vehicle != null) {
         this.setAPI(new VehicleEnterEvent(this.license, this.entity.getLocation()));
         this.callAPI();
         if (!this.isCancelled()) {
            String customName = this.entity.getCustomName();
            if (customName.contains("MTVEHICLES_SEAT")) {
               if (!vehicle.isPublic() && !vehicle.isOwner(this.player) && !vehicle.canSit(this.player) && !this.player.hasPermission("mtvehicles.ride")) {
                  this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_ENTER).replace("%p%", vehicle.getOwnerName())));
               } else {
                  // [PERBAIKAN BSNL] Memperbaiki cara mengecek apakah kursi kosong di MC terbaru
                  if (this.entity.getPassengers().isEmpty()) {
                     this.entity.addPassenger(this.player);
                     this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ENTER_MEMBER).replace("%p%", vehicle.getOwnerName())));
                  }
               }
            } else {
               VehicleUtils.enterVehicle(this.license, this.player);
            }
         }
      }
   }
}