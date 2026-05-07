package nl.mtvehicles.core.commands.vehiclesubs;

import java.util.ArrayList;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleSetOwner extends MTVSubCommand {
   public VehicleSetOwner() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      ItemStack item = this.player.getInventory().getItemInMainHand();
      boolean playerSetOwner = (Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.PUT_ONESELF_AS_OWNER);
      if (!playerSetOwner && !this.checkPermission("mtvehicles.setowner")) {
         return true;
      } else if (!this.isHoldingVehicle()) {
         return true;
      } else if (this.arguments.length != 2) {
         this.sendMessage(Message.USE_SET_OWNER);
         return true;
      } else {
         String licensePlate = VehicleUtils.getLicensePlate(item);
         if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
            this.sendMessage(Message.VEHICLE_NOT_FOUND);
            return true;
         } else {
            Player argPlayer = Bukkit.getPlayer(this.arguments[1]);
            if (argPlayer == null) {
               this.sendMessage(Message.PLAYER_NOT_FOUND);
               return true;
            } else {
               Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);

               assert vehicle != null;

               if ((playerSetOwner || !this.player.hasPermission("mtvehicles.setowner")) && !vehicle.isOwner(this.player)) {
                  this.sendMessage(Message.NOT_YOUR_CAR);
                  return true;
               } else {
                  vehicle.setRiders(new ArrayList());
                  vehicle.setMembers(new ArrayList());
                  vehicle.setOwner(argPlayer.getUniqueId());
                  vehicle.save();
                  this.sendMessage(Message.MEMBER_CHANGE);
                  return true;
               }
            }
         }
      }
   }
}
