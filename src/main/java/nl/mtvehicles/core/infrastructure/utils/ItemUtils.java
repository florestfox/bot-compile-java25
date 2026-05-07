package nl.mtvehicles.core.infrastructure.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUtils {
   public static HashMap<String, Boolean> edit = new HashMap();

   public static Material getMaterial(String string) {
      try {
         Material material = Material.matchMaterial(string);

         assert material != null;

         return material;
      } catch (Exception var6) {
         try {
            Material material = Material.matchMaterial("LEGACY_" + string);

            assert material != null;

            return material;
         } catch (Exception var5) {
            try {
               Material material = Material.matchMaterial(string, true);

               assert material != null;

               return material;
            } catch (Exception var4) {
               Main.logSevere("An error occurred while trying to obtain material from string '" + string + "'. This might happen after meddling with the config files or it could be a plugin issue.");
               return null;
            }
         }
      }
   }

   public static ItemStack getMenuVehicle(@NotNull Material material, int durability, String name) {
      if (!material.isItem()) {
         return null;
      } else {
         ItemStack vehicle = (new ItemFactory(material)).setName(TextUtils.colorize("&6" + name)).setDurability(durability).setUnbreakable(true).setLore("&a").toItemStack();
         return vehicle;
      }
   }

   public static ItemStack getVehicleItem(@NotNull Material material, int durability, String name) {
      if (!material.isItem()) {
         return null;
      } else {
         String licensePlate = generateLicencePlate();
         ItemStack vehicle = (new ItemFactory(material)).setDurability(durability).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", licensePlate).setNBT("mtvehicles.naam", name).setLore("&a", "&a" + licensePlate, "&a").setUnbreakable(true).toItemStack();
         return vehicle;
      }
   }

   public static ItemStack getVehicleItem(@NotNull Material material, int durability, String name, String nbtKey, @Nullable Object nbtValue) {
      if (!material.isItem()) {
         return null;
      } else if (nbtValue == null) {
         return getVehicleItem(material, durability, name);
      } else {
         String licensePlate = generateLicencePlate();
         ItemStack vehicle = (new ItemFactory(material)).setDurability(durability).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", licensePlate).setNBT("mtvehicles.naam", name).setNBT(nbtKey, nbtValue.toString()).setLore("&a", "&a" + licensePlate, "&a").setUnbreakable(true).toItemStack();
         return vehicle;
      }
   }

   public static ItemStack getVehicleItem(@NotNull Material material, int durability, @Nullable Boolean glowing, String name, String licensePlate) {
      if (!material.isItem()) {
         return null;
      } else {
         if (glowing == null) {
            glowing = false;
         }

         ItemStack vehicle = (new ItemFactory(material)).setDurability(durability).setName(TextUtils.colorize("&6" + name)).setGlowing(glowing).setNBT("mtvehicles.kenteken", licensePlate).setNBT("mtvehicles.naam", name).setLore("&a", "&a" + licensePlate, "&a").setUnbreakable(true).toItemStack();
         return vehicle;
      }
   }

   public static ItemStack getVehicleItem(String licensePlate) {
      return getVehicleItem((Material)Objects.requireNonNull(getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString())), (Integer)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_DAMAGE), (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING), ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString(), licensePlate);
   }

   public static ItemStack getVehicleItem(String licensePlate, boolean nbt) {
      return !nbt ? getVehicleItem(licensePlate) : getVehicleItem((Material)Objects.requireNonNull(getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString())), (Integer)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_DAMAGE), (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING), ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString(), licensePlate, "mtcustom", ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NBT_VALUE));
   }

   public static ItemStack getVehicleItem(@NotNull Material material, int durability, Boolean glowing, String name, String licensePlate, String nbtKey, @Nullable Object nbtValue) {
      if (!material.isItem()) {
         return null;
      } else if (nbtValue == null) {
         return getVehicleItem(material, durability, glowing, name, licensePlate);
      } else {
         if (glowing == null) {
            glowing = false;
         }

         ItemStack vehicle = (new ItemFactory(material)).setDurability(durability).setName(TextUtils.colorize("&6" + name)).setGlowing(glowing).setNBT("mtvehicles.kenteken", licensePlate).setNBT("mtvehicles.naam", name).setNBT(nbtKey, nbtValue.toString()).setLore("&a", "&a" + licensePlate, "&a").setUnbreakable(true).toItemStack();
         return vehicle;
      }
   }

   private static String generateLicencePlate() {
      String plate = String.format("%s-%s-%s", RandomStringUtils.random(2, true, false), RandomStringUtils.random(2, true, false), RandomStringUtils.random(2, true, false));
      return plate.toUpperCase();
   }

   public static ItemStack getMenuItem(String materialName, String materialLegacyName, short legacyData, int amount, String name, List<String> lores) {
      ItemStack item;
      try {
         item = new ItemStack(getMaterial(materialName), amount);
      } catch (Exception var10) {
         try {
            item = new ItemStack(getMaterial(materialLegacyName), amount);
            item.setDurability(legacyData);
         } catch (Exception var9) {
            Main.logSevere("An error occurred - could not get item neither from " + materialName + " nor from " + materialLegacyName + ". This is most likely a plugin issue, contact us at discord.gg/vehicle!");
            return null;
         }
      }

      return (new ItemFactory(item)).setName(name).setLore(lores).toItemStack();
   }

   public static ItemStack getMenuItem(String materialName, String materialLegacyName, short legacyData, int amount, String name, String... lores) {
      return getMenuItem(materialName, materialLegacyName, legacyData, amount, name, Arrays.asList(lores));
   }

   public static ItemStack getMenuItem(@NotNull Material material, int amount, String name, List<String> lores) {
      ItemStack item = (new ItemFactory(material, amount)).setName(name).setLore(lores).toItemStack();
      return item;
   }

   public static ItemStack getMenuItem(@NotNull Material material, int amount, String name, String... lores) {
      return getMenuItem(material, amount, name, Arrays.asList(lores));
   }

   public static ItemStack getMenuGlowingItem(@NotNull Material material, int amount, String name, List<String> lores) {
      ItemStack item = (new ItemFactory(material, amount)).setName(name).setGlowing(true).setLore(lores).toItemStack();
      return item;
   }

   public static ItemStack getMenuGlowingItem(@NotNull Material material, int amount, String name, String... lores) {
      return getMenuGlowingItem(material, amount, name, Arrays.asList(lores));
   }

   public static ItemStack getMenuItem(@NotNull Material material, int amount, int durability, boolean unbreakable, String name, List<String> lores) {
      ItemStack item = (new ItemFactory(material, amount)).setName(name).setDurability(durability).setUnbreakable(unbreakable).setLore(lores).toItemStack();
      return item;
   }

   public static ItemStack getMenuItem(@NotNull Material material, int amount, int durability, String name, List<String> lores) {
      return getMenuItem(material, amount, durability, false, name, lores);
   }

   public static ItemStack getMenuItem(@NotNull Material material, int amount, int durability, String name, String... lores) {
      return getMenuItem(material, amount, durability, false, name, Arrays.asList(lores));
   }

   @VersionSpecific
   public static Material getStainedGlassPane() {
      return VersionModule.getServerVersion().is1_12() ? Material.matchMaterial("STAINED_GLASS_PANE") : Material.matchMaterial("WHITE_STAINED_GLASS_PANE");
   }

   public static ItemStack getMenuRidersItem(String licensePlate) {
      List<String> lore = new ArrayList();
      MessagesConfig msg = ConfigModule.messagesConfig;
      Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
      if (vehicle == null) {
         return null;
      } else {
         if (vehicle.getRiders().size() == 0) {
            lore.add(msg.getMessage(Message.VEHICLE_INFO_RIDERS_NONE));
         } else {
            lore.add(String.format(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_RIDERS), vehicle.getRiders().size(), ""));

            for(String rider : vehicle.getRiders()) {
               lore.add(TextUtils.colorize("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(rider)).getName()));
            }
         }

         return getMenuItem(Material.PAPER, 1, "&6" + msg.getMessage(Message.RIDERS), lore);
      }
   }

   public static ItemStack getMenuMembersItem(String licensePlate) {
      List<String> lore = new ArrayList();
      MessagesConfig msg = ConfigModule.messagesConfig;
      Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
      if (vehicle == null) {
         return null;
      } else {
         if (vehicle.getMembers().size() == 0) {
            lore.add(msg.getMessage(Message.VEHICLE_INFO_MEMBERS_NONE));
         } else {
            lore.add(String.format(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_INFO_MEMBERS), vehicle.getMembers().size(), ""));

            for(String member : vehicle.getMembers()) {
               lore.add(TextUtils.colorize("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(member)).getName()));
            }
         }

         return getMenuItem(Material.PAPER, 1, "&6" + msg.getMessage(Message.MEMBERS), lore);
      }
   }

   public static ItemStack getMenuCustomItem(@NotNull Material material, String name, int durability, List<String> lore) {
      if (!material.isItem()) {
         return null;
      } else {
         ItemStack vehicle = (new ItemFactory(material)).setDurability(durability).setName(name).setLore(lore).setUnbreakable(true).toItemStack();
         return vehicle;
      }
   }

   public static ItemStack getMenuCustomItem(@NotNull Material material, String name, int durability, String... lore) {
      return getMenuCustomItem(material, name, durability, Arrays.asList(lore));
   }

   public static ItemStack getMenuCustomItem(@NotNull Material material, String nbtKey, @Nullable Object nbtValue, String name, int durability, List<String> lore) {
      if (!material.isItem()) {
         return null;
      } else if (nbtValue == null) {
         return getMenuCustomItem(material, name, durability, lore);
      } else {
         ItemStack vehicle = (new ItemFactory(material)).setDurability(durability).setName(name).setNBT(nbtKey, nbtValue.toString()).setLore(lore).setUnbreakable(true).toItemStack();
         return vehicle;
      }
   }

   public static ItemStack getMenuCustomItem(@NotNull Material material, String nbtKey, @Nullable Object nbtValue, String name, int durability, String... lore) {
      return getMenuCustomItem(material, nbtKey, nbtValue, name, durability, Arrays.asList(lore));
   }

   public static ItemStack createVoucher(String carUUID) {
      MessagesConfig msg = ConfigModule.messagesConfig;
      ItemStack voucher = (new ItemFactory(Material.PAPER)).setName(TextUtils.colorize(VehicleUtils.getItem(carUUID).getItemMeta().getDisplayName() + " Voucher")).setLore(TextUtils.colorize("&8&m                                    "), TextUtils.colorize(msg.getMessage(Message.VOUCHER_DESCRIPTION)), TextUtils.colorize("&2&l"), TextUtils.colorize(msg.getMessage(Message.VOUCHER_VALIDITY)), TextUtils.colorize("&2> Permanent"), TextUtils.colorize("&8&m                                    ")).setNBT("mtvehicles.item", carUUID).toItemStack();
      return voucher;
   }
}
