package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.inventory.ItemStack;

public class VehicleDelete extends MTVSubCommand {
   public VehicleDelete() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      if (!this.checkPermission("mtvehicles.delete")) {
         return true;
      } else {
         ItemStack item = this.player.getInventory().getItemInMainHand();
         if (!this.isHoldingVehicle()) {
            return true;
         } else {
            try {
               String licensePlate = VehicleUtils.getLicensePlate(item);
               VehicleUtils.getVehicle(licensePlate).delete();
               this.sendMessage(Message.VEHICLE_DELETED);
            } catch (Exception var3) {
               this.sendMessage(Message.VEHICLE_ALREADY_DELETED);
            }

            this.player.getInventory().getItemInMainHand().setAmount(0);
            return true;
         }
      }
   }
}
