package nl.mtvehicles.core.commands.vehiclesubs;

import java.util.HashMap;
import nl.mtvehicles.core.commands.VehicleTabCompleterManager;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.inventory.ItemStack;

public class VehicleBuy extends MTVSubCommand {
   public VehicleBuy() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      if (!this.sender.hasPermission("mtvehicles.buycar") && !this.sender.hasPermission("mtvehicles.buyvoucher")) {
         ConfigModule.messagesConfig.sendMessage(this.sender, Message.NO_PERMISSION);
         return true;
      } else if (this.arguments.length != 2 && this.arguments.length != 3) {
         this.sendMessage(Message.USE_NEW_VEHICLE_BUY);
         return true;
      } else if (!DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
         this.sendMessage(Message.ECONOMY_NOT_SET_UP);
         return true;
      } else if (!DependencyModule.vault.isEconomySetUp()) {
         this.sendMessage(Message.ECONOMY_NOT_SET_UP);
         return true;
      } else if (this.player.getInventory().firstEmpty() == -1) {
         this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.NO_INVENTORY_SPACE));
         return true;
      } else {
         HashMap<String, String> vehicleList = VehicleTabCompleterManager.getVehicleList();
         if (!vehicleList.containsKey(this.arguments[1])) {
            this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
            return true;
         } else {
            String carUuid = (String)vehicleList.get(this.arguments[1]);
            Double price = VehicleUtils.getPrice(carUuid);
            if (price == null) {
               this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
               return true;
            } else {
               boolean useVoucher;
               if (this.arguments.length < 3) {
                  useVoucher = false;
               } else {
                  useVoucher = this.arguments[2].equals("--voucher:true");
               }

               if (useVoucher) {
                  if (!this.checkPermission("mtvehicles.buyvoucher")) {
                     return true;
                  }

                  if (!DependencyModule.vault.withdrawMoneyPlayer(this.player, price)) {
                     return true;
                  }

                  this.player.getInventory().addItem(new ItemStack[]{ItemUtils.createVoucher(carUuid)});
               } else {
                  if (!this.checkPermission("mtvehicles.buycar")) {
                     return true;
                  }

                  if (!DependencyModule.vault.withdrawMoneyPlayer(this.player, price)) {
                     return true;
                  }

                  this.player.getInventory().addItem(new ItemStack[]{VehicleUtils.createAndGetItemByUUID(this.player, carUuid)});
               }

               return true;
            }
         }
      }
   }
}
