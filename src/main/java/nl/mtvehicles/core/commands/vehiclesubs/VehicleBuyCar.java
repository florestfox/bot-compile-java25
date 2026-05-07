package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.inventory.ItemStack;

/** @deprecated */
@Deprecated
public class VehicleBuyCar extends MTVSubCommand {
   public VehicleBuyCar() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      if (!this.player.hasPermission("mtvehicles.buycar")) {
         this.sendMessage(Message.NO_PERMISSION);
         return true;
      } else if (this.arguments.length != 2) {
         this.sendMessage(Message.USE_BUY_CAR);
         return true;
      } else if (!DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
         this.sendMessage(Message.ECONOMY_NOT_SET_UP);
         return true;
      } else if (!DependencyModule.vault.isEconomySetUp()) {
         this.sendMessage(Message.ECONOMY_NOT_SET_UP);
         return true;
      } else {
         String carUuid = this.arguments[1];
         Double price = VehicleUtils.getPrice(carUuid);
         if (price == null) {
            this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            return true;
         } else {
            if (DependencyModule.vault.withdrawMoneyPlayer(this.player, price)) {
               ItemStack car = VehicleUtils.createAndGetItemByUUID(this.player, carUuid);
               this.player.getInventory().addItem(new ItemStack[]{car});
            }

            return true;
         }
      }
   }
}
