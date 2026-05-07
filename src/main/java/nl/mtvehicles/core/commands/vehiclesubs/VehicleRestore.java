package nl.mtvehicles.core.commands.vehiclesubs;

import java.util.UUID;
import nl.mtvehicles.core.events.inventory.RestoreMenuOpenEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.utils.MenuUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class VehicleRestore extends MTVSubCommand {
   public VehicleRestore() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      if (!this.checkPermission("mtvehicles.restore")) {
         return true;
      } else {
         this.sendMessage(Message.MENU_OPEN);
         if (this.arguments.length != 2) {
            MenuUtils.restoreCMD(this.player, 1, (UUID)null);
            MenuUtils.restoreUUID.put(this.player, (Object)null);
            return true;
         } else {
            OfflinePlayer argPlayer = Bukkit.getPlayer(this.arguments[1]);
            if (argPlayer != null && argPlayer.hasPlayedBefore()) {
               RestoreMenuOpenEvent api = new RestoreMenuOpenEvent(this.player);
               api.call();
               if (api.isCancelled()) {
                  return true;
               } else {
                  MenuUtils.restoreCMD(this.player, 1, argPlayer.getUniqueId());
                  MenuUtils.restoreUUID.put(this.player, argPlayer.getUniqueId());
                  MenuUtils.restorePage.put(this.player, 1);
                  return true;
               }
            } else {
               this.sendMessage(Message.OFFLINE_PLAYER_NOT_FOUND);
               return true;
            }
         }
      }
   }
}
