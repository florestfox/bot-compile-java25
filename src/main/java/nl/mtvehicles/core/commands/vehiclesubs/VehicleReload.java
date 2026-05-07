package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;

public class VehicleReload extends MTVSubCommand {
   public VehicleReload() {
      this.setPlayerCommand(false);
   }

   public boolean execute() {
      if (!this.checkPermission("mtvehicles.reload")) {
         return true;
      } else {
         Bukkit.getLogger().info("Reload config files..");
         ConfigModule.reloadConfigs();
         Bukkit.getLogger().info("Files loaded!");
         this.sendMessage(Message.RELOAD_SUCCESSFUL);
         return true;
      }
   }
}
