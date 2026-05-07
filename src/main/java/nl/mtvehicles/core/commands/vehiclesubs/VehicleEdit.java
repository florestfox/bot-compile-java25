package nl.mtvehicles.core.commands.vehiclesubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.utils.TriFunction;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VehicleEdit extends MTVSubCommand {
   private static final Map<String, TriFunction<Player, String, String, Boolean>> EDIT_FUNCTIONS = new HashMap();

   public static List<String> getEditCommands() {
      return new ArrayList(EDIT_FUNCTIONS.keySet());
   }

   public VehicleEdit() {
      this.setPlayerCommand(false);
      EDIT_FUNCTIONS.put("licenseplate", VehicleEdit::editLicensePlate);
      EDIT_FUNCTIONS.put("name", VehicleEdit::editName);
      EDIT_FUNCTIONS.put("fuel", VehicleEdit::editFuel);
      EDIT_FUNCTIONS.put("fuelusage", VehicleEdit::editFuelUsage);
      EDIT_FUNCTIONS.put("trunkrows", VehicleEdit::editTrunkRows);
      EDIT_FUNCTIONS.put("accelerationspeed", VehicleEdit::editAccelerationSpeed);
      EDIT_FUNCTIONS.put("maxspeed", VehicleEdit::editMaxSpeed);
      EDIT_FUNCTIONS.put("brakingspeed", VehicleEdit::editBrakingSpeed);
      EDIT_FUNCTIONS.put("frictionspeed", VehicleEdit::editFrictionSpeed);
      EDIT_FUNCTIONS.put("maxspeedbackwards", VehicleEdit::editMaxSpeedBackwards);
      EDIT_FUNCTIONS.put("rotationspeed", VehicleEdit::editRotationSpeed);
      EDIT_FUNCTIONS.put("glowing", VehicleEdit::editGlowing);
      EDIT_FUNCTIONS.put("fuelenabled", VehicleEdit::editFuelEnabled);
      EDIT_FUNCTIONS.put("trunkenabled", VehicleEdit::editTrunkEnabled);
   }

   public boolean execute() {
      if (!this.checkPermission("mtvehicles.edit")) {
         return true;
      } else {
         ItemStack item = this.player.getInventory().getItemInMainHand();
         if (!this.isHoldingVehicle()) {
            return true;
         } else {
            ConfigModule.configList.forEach(MTVConfig::reload);
            if (this.arguments.length == 1 && this.isPlayer) {
               this.sendMessage(Message.MENU_OPEN);
               editMenu(this.player, item);
               return true;
            } else {
               Player targetPlayer = this.player;
               int argOffset = 0;
               Player specifiedPlayer = Bukkit.getPlayer(this.arguments[1]);
               if (specifiedPlayer != null) {
                  if (!this.player.hasPermission("mtvehicles.admin") && !this.player.equals(specifiedPlayer)) {
                     this.sendMessage(Message.NO_PERMISSION);
                     return true;
                  }

                  targetPlayer = specifiedPlayer;
                  argOffset = 1;
               }

               if (this.arguments.length == 3 + argOffset) {
                  return this.handleParameterEdit(targetPlayer, this.arguments[1 + argOffset], this.arguments[2 + argOffset]);
               } else {
                  ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.USE_EDIT);
                  return true;
               }
            }
         }
      }
   }

   private boolean handleParameterEdit(Player targetPlayer, String paramName, String paramValue) {
      ItemStack item = targetPlayer.getInventory().getItemInMainHand();
      String licensePlate = VehicleUtils.getLicensePlate(item);
      TriFunction<Player, String, String, Boolean> editFunction = (TriFunction)EDIT_FUNCTIONS.get(paramName.toLowerCase());
      if (editFunction == null) {
         ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.INVALID_INPUT);
         return true;
      } else {
         boolean success = (Boolean)editFunction.apply(targetPlayer, licensePlate, paramValue);
         if (success) {
            ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.ACTION_SUCCESSFUL);
         }

         return true;
      }
   }

   public static boolean editLicensePlate(Player player, String licensePlate, String newLicensePlate) {
      if (ConfigModule.vehicleDataConfig.get(newLicensePlate, VehicleDataConfig.Option.SKIN_ITEM) != null) {
         ConfigModule.messagesConfig.sendMessage(player, (Message)Message.ACTION_FAILED_DUP_LICENSE);
         return false;
      } else {
         for(VehicleDataConfig.Option option : VehicleDataConfig.Option.values()) {
            Object value = ConfigModule.vehicleDataConfig.get(licensePlate, option);
            if (value != null) {
               ConfigModule.vehicleDataConfig.set(newLicensePlate, option, value);
            }
         }

         player.getInventory().setItemInMainHand(ItemUtils.getVehicleItem(ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()), ConfigModule.vehicleDataConfig.getDamage(licensePlate), (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING), ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString(), newLicensePlate));
         ConfigModule.vehicleDataConfig.delete(licensePlate);
         return true;
      }
   }

   public static boolean editName(Player player, String licensePlate, String newName) {
      ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.NAME, newName);
      ConfigModule.vehicleDataConfig.save();
      player.getInventory().setItemInMainHand(ItemUtils.getVehicleItem(ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()), ConfigModule.vehicleDataConfig.getDamage(licensePlate), (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING), newName, licensePlate));
      return true;
   }

   public static boolean editFuel(Player player, String licensePlate, String fuelStr) {
      try {
         Integer fuel = Integer.parseInt(fuelStr);
         if (fuel > 100) {
            ConfigModule.messagesConfig.sendMessage(player, (Message)Message.FUEL_TOO_HIGH);
            return false;
         } else {
            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL, Double.parseDouble(fuelStr));
            ConfigModule.vehicleDataConfig.save();
            return true;
         }
      } catch (NumberFormatException var4) {
         ConfigModule.messagesConfig.sendMessage(player, (Message)Message.MUST_BE_INTEGER);
         return false;
      }
   }

   public static boolean editFuelUsage(Player player, String licensePlate, String fuelUsageStr) {
      return editDoubleValue(player, licensePlate, fuelUsageStr, VehicleDataConfig.Option.FUEL_USAGE);
   }

   public static boolean editTrunkRows(Player player, String licensePlate, String rowsStr) {
      try {
         int rows = Integer.parseInt(rowsStr);
         if (rows >= 1 && rows <= 6) {
            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.TRUNK_ROWS, rows);
            ConfigModule.vehicleDataConfig.save();
            return true;
         } else {
            ConfigModule.messagesConfig.sendMessage(player, (Message)Message.INVALID_INPUT);
            return false;
         }
      } catch (NumberFormatException var4) {
         ConfigModule.messagesConfig.sendMessage(player, (Message)Message.MUST_BE_INTEGER);
         return false;
      }
   }

   public static boolean editAccelerationSpeed(Player player, String licensePlate, String speedStr) {
      return editDoubleValue(player, licensePlate, speedStr, VehicleDataConfig.Option.ACCELERATION_SPEED);
   }

   public static boolean editMaxSpeed(Player player, String licensePlate, String speedStr) {
      return editDoubleValue(player, licensePlate, speedStr, VehicleDataConfig.Option.MAX_SPEED);
   }

   public static boolean editBrakingSpeed(Player player, String licensePlate, String speedStr) {
      return editDoubleValue(player, licensePlate, speedStr, VehicleDataConfig.Option.BRAKING_SPEED);
   }

   public static boolean editFrictionSpeed(Player player, String licensePlate, String speedStr) {
      return editDoubleValue(player, licensePlate, speedStr, VehicleDataConfig.Option.FRICTION_SPEED);
   }

   public static boolean editMaxSpeedBackwards(Player player, String licensePlate, String speedStr) {
      return editDoubleValue(player, licensePlate, speedStr, VehicleDataConfig.Option.MAX_SPEED_BACKWARDS);
   }

   public static boolean editRotationSpeed(Player player, String licensePlate, String speedStr) {
      return editIntValue(player, licensePlate, speedStr, VehicleDataConfig.Option.ROTATION_SPEED);
   }

   public static boolean editGlowing(Player player, String licensePlate, String valueStr) {
      ItemStack item = player.getInventory().getItemInMainHand();
      ItemMeta meta = item.getItemMeta();
      if (valueStr.equalsIgnoreCase("true")) {
         meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
      } else {
         meta.removeEnchant(Enchantment.ARROW_INFINITE);
         meta.removeItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
      }

      item.setItemMeta(meta);
      return editBooleanValue(player, licensePlate, valueStr, VehicleDataConfig.Option.IS_GLOWING);
   }

   public static boolean editFuelEnabled(Player player, String licensePlate, String valueStr) {
      return editBooleanValue(player, licensePlate, valueStr, VehicleDataConfig.Option.FUEL_ENABLED);
   }

   public static boolean editTrunkEnabled(Player player, String licensePlate, String valueStr) {
      return editBooleanValue(player, licensePlate, valueStr, VehicleDataConfig.Option.TRUNK_ENABLED);
   }

   private static boolean editBooleanValue(Player player, String licensePlate, String valueStr, VehicleDataConfig.Option option) {
      boolean value;
      switch (valueStr.toLowerCase()) {
         case "true":
            value = true;
            break;
         case "false":
            value = false;
            break;
         default:
            ConfigModule.messagesConfig.sendMessage(player, (Message)Message.MUST_BE_BOOLEAN);
            return false;
      }

      ConfigModule.vehicleDataConfig.set(licensePlate, option, value);
      ConfigModule.vehicleDataConfig.save();
      return true;
   }

   private static boolean editDoubleValue(Player player, String licensePlate, String valueStr, VehicleDataConfig.Option option) {
      try {
         double value = Double.parseDouble(valueStr);
         ConfigModule.vehicleDataConfig.set(licensePlate, option, value);
         ConfigModule.vehicleDataConfig.save();
         return true;
      } catch (NumberFormatException var6) {
         ConfigModule.messagesConfig.sendMessage(player, (Message)Message.MUST_BE_DOUBLE);
         return false;
      }
   }

   private static boolean editIntValue(Player player, String licensePlate, String valueStr, VehicleDataConfig.Option option) {
      try {
         int value = Integer.parseInt(valueStr);
         ConfigModule.vehicleDataConfig.set(licensePlate, option, value);
         ConfigModule.vehicleDataConfig.save();
         return true;
      } catch (NumberFormatException var5) {
         ConfigModule.messagesConfig.sendMessage(player, (Message)Message.MUST_BE_INTEGER);
         return false;
      }
   }

   public static void editMenu(Player p, ItemStack item) {
      String licensePlate = VehicleUtils.getLicensePlate(item);
      MessagesConfig msg = ConfigModule.messagesConfig;
      Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, InventoryTitle.VEHICLE_EDIT_MENU.getStringTitle());
      inv.setItem(10, ItemUtils.getMenuCustomItem(ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()), "mtcustom", ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NBT_VALUE), msg.getMessage(Message.VEHICLE_SETTINGS), ConfigModule.vehicleDataConfig.getDamage(licensePlate), ""));
      inv.setItem(11, ItemUtils.getMenuCustomItem(Material.DIAMOND_HOE, msg.getMessage(Message.FUEL_SETTINGS), 58, ""));
      inv.setItem(12, ItemUtils.getMenuItem(Material.CHEST, 1, msg.getMessage(Message.TRUNK_SETTINGS), ""));
      inv.setItem(13, ItemUtils.getMenuItem(Material.PAPER, 1, msg.getMessage(Message.MEMBER_SETTINGS), ""));
      inv.setItem(14, ItemUtils.getMenuItem("LIME_STAINED_GLASS", "STAINED_GLASS", (short)5, 1, msg.getMessage(Message.SPEED_SETTINGS), ""));
      inv.setItem(16, ItemUtils.getMenuItem(Material.BARRIER, 1, msg.getMessage(Message.DELETE_VEHICLE), msg.getMessage(Message.DELETE_WARNING_LORE)));
      p.openInventory(inv);
   }
}
