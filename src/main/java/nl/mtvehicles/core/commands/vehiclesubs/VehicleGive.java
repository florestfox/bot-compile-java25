package nl.mtvehicles.core.commands.vehiclesubs;

import java.util.HashMap;
import nl.mtvehicles.core.commands.VehicleTabCompleterManager;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleGive extends MTVSubCommand {
   public VehicleGive() {
      this.setPlayerCommand(false);
   }

   public boolean execute() {
      if (!this.sender.hasPermission("mtvehicles.givecar") && !this.sender.hasPermission("mtvehicles.givevoucher")) {
         ConfigModule.messagesConfig.sendMessage(this.sender, Message.NO_PERMISSION);
         return true;
      } else if (this.arguments.length != 3 && this.arguments.length != 4) {
         this.sendMessage(Message.USE_NEW_VEHICLE_GIVE);
         return true;
      } else {
         Player argPlayer = Bukkit.getPlayer(this.arguments[1]);
         if (argPlayer == null) {
            this.sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
         } else {
            HashMap<String, String> vehicleList = VehicleTabCompleterManager.getVehicleList();
            if (!vehicleList.containsKey(this.arguments[2])) {
               this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
               return true;
            } else {
               String carUuid = (String)vehicleList.get(this.arguments[2]);
               boolean useVoucher;
               if (this.arguments.length < 4) {
                  useVoucher = false;
               } else {
                  useVoucher = this.arguments[3].equals("--voucher:true");
               }

               ItemStack itemToGive;
               if (useVoucher) {
                  if (!this.checkPermission("mtvehicles.givevoucher")) {
                     return true;
                  }

                  if (VehicleUtils.getItem(carUuid) == null) {
                     this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
                     return true;
                  }

                  itemToGive = ItemUtils.createVoucher(carUuid);
               } else {
                  if (!this.checkPermission("mtvehicles.givecar")) {
                     return true;
                  }

                  ItemStack car = VehicleUtils.createAndGetItemByUUID(argPlayer, carUuid);
                  if (car == null) {
                     this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
                     return true;
                  }

                  itemToGive = car;
               }

               HashMap<Integer, ItemStack> failedItems = argPlayer.getInventory().addItem(new ItemStack[]{itemToGive});
               if (!failedItems.isEmpty()) {
                  this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.NO_INVENTORY_SPACE));
                  return true;
               } else {
                  if (useVoucher) {
                     this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_VOUCHER_SUCCESS).replace("%p%", argPlayer.getName()));
                  } else {
                     this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_SUCCESS).replace("%p%", argPlayer.getName()));
                  }

                  return true;
               }
            }
         }
      }
   }
}
