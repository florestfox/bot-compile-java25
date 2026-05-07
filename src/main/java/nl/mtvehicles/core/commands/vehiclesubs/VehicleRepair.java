package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.inventory.ItemStack;

public class VehicleRepair extends MTVSubCommand {
   public VehicleRepair() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      if (!this.checkPermission("mtvehicles.repair")) {
         return true;
      } else {
         ItemStack item = this.player.getInventory().getItemInMainHand();
         if (!this.isHoldingVehicle()) {
            return true;
         } else {
            String license = VehicleUtils.getLicensePlate(item);
            int damage = ConfigModule.vehicleDataConfig.getDamage(license);
            double maxHealth = VehicleUtils.getMaxHealthByDamage(damage);
            VehicleData.markVehicleAsRepaired(license);
            ConfigModule.vehicleDataConfig.setHealth(license, maxHealth);
            this.sendMessage(Message.REPAIR_SUCCESSFUL);
            return true;
         }
      }
   }
}
