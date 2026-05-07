package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.utils.LanguageUtils;

public class VehicleLanguage extends MTVSubCommand {
   public VehicleLanguage() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      if (!this.sender.hasPermission("mtvehicles.language") && !this.sender.hasPermission("mtvehicles.admin")) {
         this.sendMessage(Message.NO_PERMISSION);
      } else {
         LanguageUtils.openLanguageGUI(this.player);
      }

      return true;
   }
}
