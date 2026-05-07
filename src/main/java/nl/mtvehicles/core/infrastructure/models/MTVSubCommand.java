package nl.mtvehicles.core.infrastructure.models;

import javax.annotation.Nullable;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class MTVSubCommand {
   protected CommandSender sender;
   @Nullable
   protected Player player;
   protected boolean isPlayer;
   protected String[] arguments;
   private boolean isPlayerCommand;

   public boolean onExecute(CommandSender sender, Command cmd, String s, String[] args) {
      this.sender = sender;
      this.isPlayer = sender instanceof Player;
      this.player = this.isPlayer ? (Player)sender : null;
      this.arguments = args;
      if (this.isPlayerCommand && !this.isPlayer) {
         this.sendMessage(ConfigModule.messagesConfig.getMessage(Message.NOT_FOR_CONSOLE));
         return true;
      } else {
         return this.execute();
      }
   }

   public abstract boolean execute();

   public void sendMessage(String message) {
      this.sender.sendMessage(TextUtils.colorize(message));
   }

   public void sendMessage(Message message) {
      this.sender.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(message)));
   }

   public boolean checkPermission(String permission) {
      if (this.sender.hasPermission(permission)) {
         return true;
      } else {
         ConfigModule.messagesConfig.sendMessage(this.sender, Message.NO_PERMISSION);
         return false;
      }
   }

   public boolean isPlayerCommand() {
      return this.isPlayerCommand;
   }

   public void setPlayerCommand(boolean playerCommand) {
      this.isPlayerCommand = playerCommand;
   }

   protected Vehicle getVehicle() {
      if (this.player == null) {
         return null;
      } else {
         if (VehicleUtils.isInsideVehicle(this.player)) {
            Vehicle vehicle = VehicleUtils.getVehicle(VehicleUtils.getLicensePlate(this.player.getVehicle()));
            if (vehicle.isOwner(this.player)) {
               return vehicle;
            }
         }

         ItemStack item = this.player.getInventory().getItemInMainHand();
         if (item.hasItemMeta() && (new NBTItem(item)).hasKey("mtvehicles.kenteken")) {
            return VehicleUtils.getVehicle(VehicleUtils.getLicensePlate(item));
         } else {
            this.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.COMMAND_NO_VEHICLE)));
            return null;
         }
      }
   }

   protected boolean isHoldingVehicle() {
      ItemStack item = this.player.getInventory().getItemInMainHand();
      if (item.hasItemMeta() && (new NBTItem(item)).hasKey("mtvehicles.kenteken")) {
         return true;
      } else {
         this.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.NO_VEHICLE_IN_HAND)));
         return false;
      }
   }
}
