package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.PluginUpdater;

public class VehicleUpdate extends MTVSubCommand {
   public boolean execute() {
      if (!this.checkPermission("mtvehicles.update")) {
         return true;
      } else if (!(Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.AUTO_UPDATE)) {
         this.sendMessage(Message.UPDATE_DISABLED);
         return false;
      } else {
         PluginUpdater.updatePlugin(this.sender);
         return true;
      }
   }
}
