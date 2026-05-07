package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;

public class VehicleDespawn extends MTVSubCommand {
   public boolean execute() {
      if (!this.checkPermission("mtvehicles.despawn")) {
         return true;
      } else if (this.arguments.length != 2) {
         this.sendMessage(Message.USE_VEHICLE_DESPAWN);
         return true;
      } else {
         String licensePlate = this.arguments[1];
         int despawnVehicles = VehicleUtils.despawnVehicle(licensePlate);
         if (despawnVehicles == 0) {
            this.sendMessage(Message.NO_VEHICLE_TO_DESPAWN);
         } else {
            this.sendMessage(Message.VEHICLE_DESPAWNED);
         }

         return true;
      }
   }
}
