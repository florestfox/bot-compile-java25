package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.inventory.ItemStack;

public class VehicleRefill extends MTVSubCommand {
   public VehicleRefill() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      if (!this.checkPermission("mtvehicles.refill")) {
         return true;
      } else {
         ItemStack item = this.player.getInventory().getItemInMainHand();
         if (!this.isHoldingVehicle()) {
            return true;
         } else {
            String licensePlate = VehicleUtils.getLicensePlate(item);
            Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
            vehicle.setFuel((double)100.0F);
            vehicle.save();
            VehicleData.fuel.put(licensePlate, (double)100.0F);
            if (VehicleData.fallDamage.get(licensePlate) != null) {
               VehicleData.fallDamage.remove(licensePlate);
            }

            this.sendMessage(Message.REFILL_SUCCESSFUL);
            return true;
         }
      }
   }
}
