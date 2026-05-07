package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/** @deprecated */
@Deprecated
public class VehicleGiveCar extends MTVSubCommand {
   public VehicleGiveCar() {
      this.setPlayerCommand(false);
   }

   public boolean execute() {
      if (!this.checkPermission("mtvehicles.givecar")) {
         return true;
      } else if (this.arguments.length != 3) {
         this.sendMessage(Message.USE_GIVE_CAR);
         return true;
      } else {
         Player argPlayer = Bukkit.getPlayer(this.arguments[1]);
         String carUuid = this.arguments[2].replace("-", "");
         if (argPlayer == null) {
            this.sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
         } else {
            ItemStack car = VehicleUtils.createAndGetItemByUUID(argPlayer, carUuid);
            if (car == null) {
               this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
               return true;
            } else {
               argPlayer.getInventory().addItem(new ItemStack[]{car});
               this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_SUCCESS).replace("%p%", argPlayer.getName()));
               return true;
            }
         }
      }
   }
}
