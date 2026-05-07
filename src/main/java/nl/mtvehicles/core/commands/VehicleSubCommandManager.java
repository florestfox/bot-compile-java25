package nl.mtvehicles.core.commands;

import nl.mtvehicles.core.commands.vehiclesubs.VehicleAddMember;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleAddRider;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleBuy;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleBuyCar;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleBuyVoucher;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleDelete;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleDespawn;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleGive;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleGiveCar;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleGiveFuel;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleGiveVoucher;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleHelp;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleInfo;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleLanguage;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleMenu;
import nl.mtvehicles.core.commands.vehiclesubs.VehiclePrivate;
import nl.mtvehicles.core.commands.vehiclesubs.VehiclePublic;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleRefill;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleReload;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleRemoveMember;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleRemoveRider;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleRepair;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleRestore;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleSetOwner;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleTrunk;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleUpdate;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleVault;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleVersion;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVCommand;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.CommandModule;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VehicleSubCommandManager extends MTVCommand {
   public static String CMD_NAME = "minetopiavehicles";

   public VehicleSubCommandManager() {
      CommandModule.subcommands.put("info", new VehicleInfo());
      CommandModule.subcommands.put("help", new VehicleHelp());
      CommandModule.subcommands.put("admin", new VehicleHelp());
      CommandModule.subcommands.put("reload", new VehicleReload());
      CommandModule.subcommands.put("menu", new VehicleMenu());
      CommandModule.subcommands.put("restore", new VehicleRestore());
      CommandModule.subcommands.put("edit", new VehicleEdit());
      CommandModule.subcommands.put("fuel", new VehicleFuel());
      CommandModule.subcommands.put("benzine", new VehicleFuel());
      CommandModule.subcommands.put("setowner", new VehicleSetOwner());
      CommandModule.subcommands.put("public", new VehiclePublic());
      CommandModule.subcommands.put("private", new VehiclePrivate());
      CommandModule.subcommands.put("addmember", new VehicleAddMember());
      CommandModule.subcommands.put("addrider", new VehicleAddRider());
      CommandModule.subcommands.put("removemember", new VehicleRemoveMember());
      CommandModule.subcommands.put("removerider", new VehicleRemoveRider());
      CommandModule.subcommands.put("give", new VehicleGive());
      CommandModule.subcommands.put("givecar", new VehicleGiveCar());
      CommandModule.subcommands.put("givevoucher", new VehicleGiveVoucher());
      CommandModule.subcommands.put("buy", new VehicleBuy());
      CommandModule.subcommands.put("buycar", new VehicleBuyCar());
      CommandModule.subcommands.put("buyvoucher", new VehicleBuyVoucher());
      CommandModule.subcommands.put("update", new VehicleUpdate());
      CommandModule.subcommands.put("delete", new VehicleDelete());
      CommandModule.subcommands.put("language", new VehicleLanguage());
      CommandModule.subcommands.put("about", new VehicleVersion());
      CommandModule.subcommands.put("version", new VehicleVersion());
      CommandModule.subcommands.put("repair", new VehicleRepair());
      CommandModule.subcommands.put("refill", new VehicleRefill());
      CommandModule.subcommands.put("refuel", new VehicleRefill());
      CommandModule.subcommands.put("trunk", new VehicleTrunk());
      CommandModule.subcommands.put("baggage", new VehicleTrunk());
      CommandModule.subcommands.put("givefuel", new VehicleGiveFuel());
      CommandModule.subcommands.put("despawn", new VehicleDespawn());
      CommandModule.subcommands.put("vault", new VehicleVault());
   }

   public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
      if (args.length == 0) {
         ((MTVSubCommand)CommandModule.subcommands.get("help")).onExecute(sender, cmd, label, args);
         return true;
      } else {
         String subcommand = args[0].toLowerCase();
         if (CommandModule.subcommands.get(subcommand) == null) {
            this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.COMMAND_DOES_NOT_EXIST));
            return true;
         } else {
            ((MTVSubCommand)CommandModule.subcommands.get(subcommand)).onExecute(sender, cmd, label, args);
            return true;
         }
      }
   }
}
