package nl.mtvehicles.core.commands.vehiclesubs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;
import java.util.stream.Collectors;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class VehicleInfo extends MTVSubCommand {
   public VehicleInfo() {
      this.setPlayerCommand(true);
   }

   public boolean execute() {
      Vehicle vehicle = this.getVehicle();
      if (vehicle == null) {
         return true;
      } else {
         String licensePlate = vehicle.getLicensePlate();
         NumberFormat formatter = new DecimalFormat("#0.000");
         this.sendMessage(Message.VEHICLE_INFO_INFORMATION);
         this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_TYPE) + vehicle.getVehicleType().getName());
         this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_NAME) + vehicle.getName());
         this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_LICENSE) + licensePlate);
         if (this.player.hasPermission("mtvehicles.admin")) {
            this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_UUID) + VehicleUtils.getUUID(licensePlate));
            if (DependencyModule.isDependencyEnabled(SoftDependency.VAULT) && DependencyModule.vault.isEconomySetUp()) {
               this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_PRICE) + DependencyModule.vault.getMoneyFormat(vehicle.getPrice()));
            }
         }

         this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_SPEED) + formatter.format(vehicle.getMaxSpeed() * (double)20.0F).toString().replace(",", ".") + " blocks/sec");
         this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_ACCELERATION) + formatter.format(vehicle.getAccelerationSpeed() / 0.2 * (double)100.0F).toString().replace(",", ".") + " blocks/sec^2");
         this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_HEALTH) + (double)Math.round(vehicle.getHealth() * (double)100.0F) / (double)100.0F);
         this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_OWNER) + vehicle.getOwnerName());
         if (vehicle.getRiders().size() == 0) {
            this.sendMessage(Message.VEHICLE_INFO_RIDERS_NONE);
         } else {
            this.sendMessage(String.format(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_RIDERS), vehicle.getRiders().size(), vehicle.getRiders().stream().map(UUID::fromString).map(Bukkit::getOfflinePlayer).map(OfflinePlayer::getName).collect(Collectors.joining(", "))));
         }

         if (vehicle.getMembers().size() == 0) {
            this.sendMessage(Message.VEHICLE_INFO_MEMBERS_NONE);
         } else {
            this.sendMessage(String.format(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_MEMBERS), vehicle.getMembers().size(), vehicle.getMembers().stream().map(UUID::fromString).map(Bukkit::getOfflinePlayer).map(OfflinePlayer::getName).collect(Collectors.joining(", "))));
         }

         return true;
      }
   }
}
