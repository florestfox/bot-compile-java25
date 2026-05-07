package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.events.VehiclePlaceEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class VehiclePlaceListener extends MTVListener {
   public VehiclePlaceListener() {
      super(new VehiclePlaceEvent());
   }

   @EventHandler
   public void onVehiclePlace(PlayerInteractEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      Action action = event.getAction();
      ItemStack item = event.getItem();
      Block clickedBlock = event.getClickedBlock();
      if (action == Action.RIGHT_CLICK_BLOCK && item != null && item.getType() != Material.AIR && item.getAmount() != 0 && item.hasItemMeta() && clickedBlock != null) {
         NBTItem nbtItem = new NBTItem(item);
         if (nbtItem.hasTag("mtvehicles.kenteken")) {
            String license = VehicleUtils.getLicensePlate(item);
            if (license != null && VehicleUtils.existsByLicensePlate(license)) {
               VehiclePlaceEvent api = (VehiclePlaceEvent)this.getAPI();
               Location spawnLoc = clickedBlock.getLocation();
               if (ConfigModule.vehicleDataConfig.getType(license).isBoat()) {
                  while(spawnLoc.getBlock().getType().toString().contains("WATER")) {
                     spawnLoc.add((double)0.0F, (double)1.0F, (double)0.0F);
                     if (spawnLoc.getY() >= clickedBlock.getLocation().getY() + (double)512.0F) {
                        break;
                     }
                  }
               }

               api.setLocation(spawnLoc);
               api.setLicensePlate(license);
               this.callAPI();
               if (!this.isCancelled()) {
                  Location loc = api.getLocation();
                  Vehicle vehicle = VehicleUtils.getVehicle(license);
                  if (vehicle != null) {
                     if (event.getHand() != EquipmentSlot.HAND) {
                        event.setCancelled(true);
                        this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND)));
                     } else if (ConfigModule.defaultConfig.isBlockWhitelistEnabled() && !ConfigModule.defaultConfig.blockWhiteList().contains(clickedBlock.getType())) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.BLOCK_NOT_IN_WHITELIST);
                     } else if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.PLACE, vehicle.getVehicleType(), loc, this.player)) {
                        ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.CANNOT_DO_THAT_HERE);
                     } else {
                        Location location = loc.clone().add((double)0.0F, (double)1.0F, (double)0.0F);
                        VehicleUtils.spawnVehicle(license, location);
                        
                        // [PERBAIKAN BSNL] Cara yang benar untuk mengurangi item di tangan pemain untuk MC versi baru
                        ItemStack mainHand = this.player.getInventory().getItemInMainHand();
                        mainHand.setAmount(mainHand.getAmount() - 1);
                        
                        this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_PLACE).replace("%p%", vehicle.getOwnerName())));
                        event.setCancelled(true);
                     }
                  }
               }
            } else {
               ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.VEHICLE_NOT_FOUND);
               event.setCancelled(true);
            }
         }
      }
   }
}