package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;

public class VehiclePublic extends MTVSubCommand {
   public VehiclePublic() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      if (!this.player.isInsideVehicle()) {
         Vehicle vehicle = this.getVehicle();
         if (vehicle == null) {
            return true;
         } else {
            vehicle.setPublic(true);
            vehicle.save();
            this.sendMessage(Message.ACTION_SUCCESSFUL);
            return true;
         }
      } else {
         return false;
      }
   }
}
