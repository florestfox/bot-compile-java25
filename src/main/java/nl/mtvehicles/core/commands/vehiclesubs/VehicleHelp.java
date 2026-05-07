package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;

public class VehicleHelp extends MTVSubCommand {
   public VehicleHelp() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      this.sendMessage(String.format("&2&lMinetopiaVehicles Commands: (%s)", Main.instance.getDescription().getVersion()));
      this.sendMessage("");
      this.sendMessage(String.format("&2/vehicle &ainfo &f- &2%s", this.desc(Message.HELP_INFO)));
      this.sendMessage(String.format("&2/vehicle &apublic &f- &2%s", this.desc(Message.HELP_PUBLIC)));
      this.sendMessage(String.format("&2/vehicle &aprivate &f- &2%s", this.desc(Message.HELP_PRIVATE)));
      this.sendMessage(String.format("&2/vehicle &aaddrider &f- &2%s", this.desc(Message.HELP_ADD_RIDER)));
      this.sendMessage(String.format("&2/vehicle &aaddmember &f- &2%s", this.desc(Message.HELP_ADD_MEMBER)));
      this.sendMessage(String.format("&2/vehicle &aremoverider &f- &2%s", this.desc(Message.HELP_REMOVE_RIDER)));
      this.sendMessage(String.format("&2/vehicle &aremovemember &f- &2%s", this.desc(Message.HELP_REMOVE_MEMBER)));
      this.sendMessage(String.format("&2/vehicle &abuy &f- &2%s", this.desc(Message.HELP_NEW_BUY)));
      if (this.sender.hasPermission("mtvehicles.admin")) {
         this.sendMessage("");
         this.sendMessage(String.format("&2/vehicle &alanguage &f- &2%s", this.desc(Message.ADMIN_LANGUAGE)));
         this.sendMessage(String.format("&2/vehicle &aversion &f- &2%s", this.desc(Message.ADMIN_VERSION)));
         this.sendMessage(String.format("&2/vehicle &arefill &f- &2%s", this.desc(Message.ADMIN_REFILL)));
         this.sendMessage(String.format("&2/vehicle &arepair &f- &2%s", this.desc(Message.ADMIN_REPAIR)));
         this.sendMessage(String.format("&2/vehicle &aedit &f- &2%s", this.desc(Message.ADMIN_EDIT)));
         this.sendMessage(String.format("&2/vehicle &amenu &f- &2%s", this.desc(Message.ADMIN_MENU)));
         this.sendMessage(String.format("&2/vehicle &afuel &f- &2%s", this.desc(Message.ADMIN_FUEL)));
         this.sendMessage(String.format("&2/vehicle &arestore &f- &2%s", this.desc(Message.ADMIN_RESTORE)));
         this.sendMessage(String.format("&2/vehicle &areload &f- &2%s", this.desc(Message.ADMIN_RELOAD)));
         this.sendMessage(String.format("&2/vehicle &agive &f- &2%s", this.desc(Message.ADMIN_NEW_GIVE)));
         this.sendMessage(String.format("&2/vehicle &asetowner &f- &2%s", this.desc(Message.ADMIN_SETOWNER)));
         this.sendMessage(String.format("&2/vehicle &aupdate &f- &2%s", this.desc(Message.ADMIN_UPDATE)));
         this.sendMessage(String.format("&2/vehicle &adelete &f- &2%s", this.desc(Message.ADMIN_DELETE)));
         this.sendMessage(String.format("&2/vehicle &agivefuel &f- &2%s", this.desc(Message.ADMIN_GIVEFUEL)));
      }

      this.sendMessage("");
      this.sendMessage("&7&oDownload it for free at mtvehicles.nl (Maintained by Nikd0, gmrrh and Tiakin)");
      return true;
   }

   private String desc(Message message) {
      return ConfigModule.messagesConfig.getMessage(message);
   }
}
