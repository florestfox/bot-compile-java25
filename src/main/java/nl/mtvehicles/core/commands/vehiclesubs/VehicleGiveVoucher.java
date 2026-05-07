package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/** @deprecated */
@Deprecated
public class VehicleGiveVoucher extends MTVSubCommand {
   public VehicleGiveVoucher() {
      this.setPlayerCommand(false);
   }

   public boolean execute() {
      if (!this.checkPermission("mtvehicles.givevoucher")) {
         return true;
      } else if (this.arguments.length != 3) {
         this.sendMessage(Message.USE_GIVE_VOUCHER);
         return true;
      } else {
         Player argPlayer = Bukkit.getPlayer(this.arguments[1]);
         String carUuid = this.arguments[2];
         if (argPlayer == null) {
            this.sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
         } else {
            ItemStack car = VehicleUtils.createAndGetItemByUUID(argPlayer, carUuid);
            if (car == null) {
               this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_CAR_NOT_FOUND));
               return true;
            } else {
               ItemStack voucher = ItemUtils.createVoucher(carUuid);
               argPlayer.getInventory().addItem(new ItemStack[]{voucher});
               this.sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_VOUCHER_SUCCESS).replace("%p%", argPlayer.getName()));
               return true;
            }
         }
      }
   }
}
