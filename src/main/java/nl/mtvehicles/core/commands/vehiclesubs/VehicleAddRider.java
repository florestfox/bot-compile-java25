package nl.mtvehicles.core.commands.vehiclesubs;

import java.util.List;
import nl.mtvehicles.core.events.VehicleAddRiderEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VehicleAddRider extends MTVSubCommand {
   public VehicleAddRider() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      Vehicle vehicle = this.getVehicle();
      if (vehicle == null) {
         return true;
      } else if (this.arguments.length != 2) {
         this.sendMessage(Message.USE_ADD_RIDER);
         return true;
      } else {
         Player argPlayer = Bukkit.getPlayer(this.arguments[1]);
         VehicleAddRiderEvent api = new VehicleAddRiderEvent();
         api.setPlayer(this.player);
         api.setAdded(argPlayer);
         api.setLicensePlate(vehicle.getLicensePlate());
         api.call();
         if (api.isCancelled()) {
            return true;
         } else {
            vehicle = api.getVehicle();
            argPlayer = api.getAdded();
            if (vehicle == null) {
               this.sendMessage(Message.VEHICLE_NOT_FOUND);
               return true;
            } else if (argPlayer == null) {
               this.sendMessage(Message.PLAYER_NOT_FOUND);
               return true;
            } else {
               List<String> riders = vehicle.getRiders();
               String playerUUID = argPlayer.getUniqueId().toString();
               if (riders.contains(playerUUID)) {
                  this.sendMessage(Message.ALREADY_RIDER);
                  return true;
               } else {
                  riders.add(argPlayer.getUniqueId().toString());
                  vehicle.setRiders(riders);
                  vehicle.save();
                  this.sendMessage(Message.MEMBER_CHANGE);
                  return true;
               }
            }
         }
      }
   }
}
