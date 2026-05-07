package nl.mtvehicles.core.listeners;

import java.util.HashMap;
import javax.annotation.Nullable;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.events.VehicleDamageEvent;
import nl.mtvehicles.core.events.VehicleFuelEvent;
import nl.mtvehicles.core.events.VehicleOpenTrunkEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.BossBarUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class VehicleEntityListener extends MTVListener {
   public static HashMap<String, Double> speed = new HashMap();

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onPlayerInteractAtEntity(EntityDamageByEntityEvent event) {
      this.event = event;
      Entity victim = event.getEntity();
      if (VehicleUtils.isVehicle(victim)) {
         String license = VehicleUtils.getLicensePlate(victim);
         if (license != null) {
            Entity damager = event.getDamager();
            if (!(damager instanceof Player)) {
               this.handleVehicleDamage(damager, license);
            } else {
               this.player = (Player)damager;
               if (this.player.isSneaking() && !this.player.isInsideVehicle()) {
                  this.handleOpenTrunk(license);
                  event.setCancelled(true);
               } else {
                  ItemStack heldItem = this.player.getInventory().getItemInMainHand();
                  if (!heldItem.hasItemMeta()) {
                     this.handleVehicleDamage(this.player, license);
                  } else {
                     NBTItem nbt = new NBTItem(heldItem);
                     if (!nbt.hasKey("mtvehicles.benzineval")) {
                        this.handleVehicleDamage(this.player, license);
                     } else {
                        this.handleFueling(license, this.player, nbt);
                     }
                  }
               }
            }
         }
      }
   }

   private void handleVehicleDamage(Entity damager, String license) {
      this.setupDamageAPI(damager, license);
      this.callAPI((Player)null);
      if (!this.isCancelled()) {
         String newLicense = ((VehicleDamageEvent)this.getAPI()).getLicensePlate();
         double damage = ((VehicleDamageEvent)this.getAPI()).getDamage();
         damage(newLicense, damage);
      }
   }

   private void handleOpenTrunk(String license) {
      this.setAPI(new VehicleOpenTrunkEvent());
      VehicleOpenTrunkEvent api = (VehicleOpenTrunkEvent)this.getAPI();
      api.setLicensePlate(license);
      this.callAPI();
      if (!this.isCancelled()) {
         if (!VehicleUtils.isTrunkInventoryOpen(this.player, license)) {
            VehicleUtils.openTrunk(this.player, api.getLicensePlate());
         }

      }
   }

   private void handleFueling(String license, Player player, NBTItem nbt) {
      double vehicleFuel = Math.max((Double)VehicleData.fuel.getOrDefault(license, (double)0.0F), (Double)ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.FUEL));
      String jerryCanFuelStr = nbt.getString("mtvehicles.benzineval");
      String jerryCanSizeStr = nbt.getString("mtvehicles.benzinesize");
      if (jerryCanFuelStr != null && jerryCanSizeStr != null) {
         int jerryCanFuel = Integer.parseInt(jerryCanFuelStr);
         int jerryCanSize = Integer.parseInt(jerryCanSizeStr);
         this.setAPI(new VehicleFuelEvent(vehicleFuel, jerryCanFuel, jerryCanSize));
         VehicleFuelEvent api = (VehicleFuelEvent)this.getAPI();
         api.setLicensePlate(license);
         this.callAPI();
         if (!this.isCancelled()) {
            license = api.getLicensePlate();
            if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED) && (Boolean)ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.FUEL_ENABLED)) {
               if (!ConfigModule.defaultConfig.canUseJerryCan(player)) {
                  ConfigModule.messagesConfig.sendMessage(player, (Message)Message.NOT_IN_A_GAS_STATION);
               } else if (jerryCanFuel < 1) {
                  ConfigModule.messagesConfig.sendMessage(player, (Message)Message.NO_FUEL);
               } else if (vehicleFuel >= (double)100.0F) {
                  ConfigModule.messagesConfig.sendMessage(player, (Message)Message.VEHICLE_FULL);
               } else {
                  if (VehicleData.fallDamage.get(license) != null && vehicleFuel > (double)2.0F) {
                     VehicleData.fallDamage.remove(license);
                  }

                  int fuelToAdd = Math.min(5, jerryCanFuel);
                  vehicleFuel = Math.min(vehicleFuel + (double)fuelToAdd, (double)100.0F);
                  if (player.isInsideVehicle()) {
                     VehicleData.fuel.put(license, vehicleFuel);
                  } else {
                     ConfigModule.vehicleDataConfig.set(license, VehicleDataConfig.Option.FUEL, vehicleFuel);
                     ConfigModule.vehicleDataConfig.save();
                  }

                  BossBarUtils.setBossBarValue(vehicleFuel / (double)100.0F, license);
                  player.getInventory().setItemInMainHand(VehicleFuel.jerrycanItem(jerryCanSize, jerryCanFuel - fuelToAdd));
               }
            }
         }
      }
   }

   public static void damage(String license, double damage) {
      if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.DAMAGE_ENABLED)) {
         if (VehicleUtils.getVehicle(license) != null) {
            double damageMultiplier = (Double)ConfigModule.defaultConfig.get(DefaultConfig.Option.DAMAGE_MULTIPLIER);
            damageMultiplier = Math.max(0.1, Math.min(damageMultiplier, (double)5.0F));
            ConfigModule.vehicleDataConfig.damageVehicle(license, damage * damageMultiplier);
         }
      }
   }

   private void setupDamageAPI(@Nullable Entity damager, String license) {
      this.setAPI(new VehicleDamageEvent());
      VehicleDamageEvent api = (VehicleDamageEvent)this.getAPI();
      api.setDamager(damager);
      api.setDamageCause(DamageCause.ENTITY_ATTACK);
      api.setLicensePlate(license);
      api.setDamage(((EntityDamageByEntityEvent)this.event).getDamage());
   }
}
