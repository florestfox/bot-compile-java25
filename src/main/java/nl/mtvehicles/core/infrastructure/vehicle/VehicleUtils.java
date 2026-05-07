package nl.mtvehicles.core.infrastructure.vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.BossBarUtils;
import nl.mtvehicles.core.infrastructure.utils.ItemFactory;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.utils.PaperUtils;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class VehicleUtils {
   public static HashMap<Player, String> openedTrunk = new HashMap();

   private VehicleUtils() {
   }

   public static void spawnVehicle(String licensePlate, Location location) throws IllegalArgumentException {
      if (!existsByLicensePlate(licensePlate)) {
         throw new IllegalArgumentException("Vehicle does not exists.");
      } else {
         ArmorStand standSkin = (ArmorStand)location.getWorld().spawn(location, ArmorStand.class);
         allowTicking(standSkin);
         standSkin.setVisible(false);
         standSkin.setCustomName("MTVEHICLES_SKIN_" + licensePlate);
         standSkin.getEquipment().setHelmet(ItemUtils.getVehicleItem(ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()), (Integer)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_DAMAGE), false, ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString(), licensePlate));
         ArmorStand standMain = (ArmorStand)location.getWorld().spawn(location, ArmorStand.class);
         standMain.setVisible(false);
         standMain.setCustomName("MTVEHICLES_MAIN_" + licensePlate);
         Vehicle vehicle = getVehicle(licensePlate);
         List<Map<String, Double>> seats = (List)vehicle.getVehicleData().get("seats");
         Map<String, Double> mainSeat = (Map)seats.get(0);
         Location locationMainSeat = new Location(location.getWorld(), location.getX() + (Double)mainSeat.get("x"), location.getY() + (Double)mainSeat.get("y"), location.getZ() + (Double)mainSeat.get("z"));
         ArmorStand standMainSeat = (ArmorStand)locationMainSeat.getWorld().spawn(locationMainSeat, ArmorStand.class);
         standMainSeat.setCustomName("MTVEHICLES_MAINSEAT_" + licensePlate);
         standMainSeat.setGravity(false);
         standMainSeat.setVisible(false);
         if (ConfigModule.vehicleDataConfig.getType(licensePlate).isBoat()) {
            standMain.setGravity(false);
            standSkin.setGravity(false);
         }

         if (ConfigModule.vehicleDataConfig.getType(licensePlate).isHelicopter()) {
            List<Map<String, Double>> helicopterBlades = (List)vehicle.getVehicleData().get("wiekens");
            Map<?, ?> blade = (Map)helicopterBlades.get(0);
            Location locationBlade = new Location(location.getWorld(), location.getX() + (Double)blade.get("z"), location.getY() + (Double)blade.get("y"), location.getZ() + (Double)blade.get("x"));
            ArmorStand standRotors = (ArmorStand)locationBlade.getWorld().spawn(locationBlade, ArmorStand.class);
            standRotors.setCustomName("MTVEHICLES_WIEKENS_" + licensePlate);
            standRotors.setGravity(false);
            standRotors.setVisible(false);
            if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.HELICOPTER_BLADES_ALWAYS_ON)) {
               ItemStack rotor = (new ItemFactory(Material.getMaterial("DIAMOND_HOE"))).setDurability(1058).setName(TextUtils.colorize("&6Wieken")).setNBT("mtvehicles.kenteken", licensePlate).toItemStack();
               ItemMeta itemMeta = rotor.getItemMeta();
               List<String> lore = new ArrayList();
               lore.add(TextUtils.colorize("&a"));
               lore.add(TextUtils.colorize("&a" + licensePlate));
               lore.add(TextUtils.colorize("&a"));
               itemMeta.setLore(lore);
               itemMeta.setUnbreakable(true);
               rotor.setItemMeta(itemMeta);
               allowTicking(standRotors);
               standRotors.setHelmet((ItemStack)blade.get("item"));
            }
         }

      }
   }

   public static String getLicensePlate(ItemStack item) {
      NBTItem nbt = new NBTItem(item);
      return nbt.getString("mtvehicles.kenteken");
   }

   public static Vehicle getVehicle(ItemStack item) {
      return getVehicle(getLicensePlate(item));
   }

   public static @Nullable String getDrivenVehiclePlate(Player p) {
      if (p.getVehicle() == null) {
         return null;
      } else if (!p.getVehicle().getCustomName().contains("MTVEHICLES_")) {
         return null;
      } else {
         String[] name = p.getVehicle().getCustomName().split("_");
         return name[2];
      }
   }

   public static Vehicle getDrivenVehicle(Player p) {
      return getDrivenVehiclePlate(p) == null ? null : getVehicle(getDrivenVehiclePlate(p));
   }

   /** @deprecated */
   @Deprecated
   public static ItemStack getItemByUUID(Player p, String uuid) {
      return createAndGetItemByUUID(p, uuid);
   }

   public static boolean vehicleUUIDExists(String uuid) {
      boolean exists = false;

      for(Map<?, ?> configVehicle : ConfigModule.vehiclesConfig.getVehicles()) {
         for(Map<?, ?> skin : (List)configVehicle.get("cars")) {
            if (skin.get("uuid") != null && skin.get("uuid").equals(uuid)) {
               exists = true;
               return exists;
            }
         }
      }

      return exists;
   }

   public static ItemStack createAndGetItemByUUID(OfflinePlayer owner, String uuid) {
      for(Map<?, ?> configVehicle : ConfigModule.vehiclesConfig.getVehicles()) {
         for(Map<?, ?> skin : (List)configVehicle.get("cars")) {
            if (skin.get("uuid") != null && skin.get("uuid").equals(uuid)) {
               String nbtVal;
               if (skin.get("nbtValue") == null) {
                  nbtVal = "null";
               } else {
                  nbtVal = skin.get("nbtValue").toString();
               }

               ItemStack item = ItemUtils.getVehicleItem(ItemUtils.getMaterial(skin.get("SkinItem").toString()), (Integer)skin.get("itemDamage"), (String)((String)skin.get("name")), "mtcustom", (Object)nbtVal);
               NBTItem nbt = new NBTItem(item);
               String licensePlate = nbt.getString("mtvehicles.kenteken");
               Vehicle vehicle = new Vehicle((Map)null, licensePlate, (String)skin.get("name"), VehicleType.valueOf((String)configVehicle.get("vehicleType")), false, (Integer)skin.get("itemDamage"), (String)skin.get("SkinItem"), false, (Boolean)configVehicle.get("hornEnabled"), (Double)configVehicle.get("maxHealth"), (Boolean)configVehicle.get("benzineEnabled"), (double)100.0F, 0.01, (Boolean)configVehicle.get("kofferbakEnabled"), 1, (List)null, (Double)configVehicle.get("acceleratieSpeed"), (Double)configVehicle.get("maxSpeed"), (Double)configVehicle.get("maxSpeedBackwards"), (Double)configVehicle.get("brakingSpeed"), (Double)configVehicle.get("aftrekkenSpeed"), (Integer)configVehicle.get("rotateSpeed"), owner.getUniqueId(), (List)null, (List)null, (Double)skin.get("price"), (String)skin.get("nbtValue"));
               vehicle.save();
               return item;
            }
         }
      }

      return null;
   }

   public static boolean getHornByDamage(int damage) {
      for(Map<?, ?> configVehicle : ConfigModule.vehiclesConfig.getVehicles()) {
         for(Map<?, ?> skin : (List)configVehicle.get("cars")) {
            if (skin.get("itemDamage") != null && skin.get("itemDamage").equals(damage)) {
               return (Boolean)configVehicle.get("hornEnabled");
            }
         }
      }

      return false;
   }

   public static double getMaxHealthByDamage(int damage) {
      for(Map<?, ?> configVehicle : ConfigModule.vehiclesConfig.getVehicles()) {
         for(Map<?, ?> skin : (List)configVehicle.get("cars")) {
            if (skin.get("itemDamage") != null && skin.get("itemDamage").equals(damage)) {
               return (Double)configVehicle.get("maxHealth");
            }
         }
      }

      return (double)0.0F;
   }

   public static ItemStack getItemByLicensePlate(String licensePlate) {
      return getItem(getUUID(licensePlate));
   }

   public static ItemStack getItem(String carUUID) {
      List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
      List<Map<?, ?>> matchedVehicles = new ArrayList();

      for(Map<?, ?> configVehicle : vehicles) {
         for(Map<?, ?> skin : (List)configVehicle.get("cars")) {
            if (skin.get("uuid") != null && skin.get("uuid").equals(carUUID) && skin.get("uuid") != null) {
               ItemStack is = ItemUtils.getVehicleItem(ItemUtils.getMaterial(skin.get("SkinItem").toString()), (Integer)skin.get("itemDamage"), (String)skin.get("name"));
               matchedVehicles.add(configVehicle);
               return is;
            }
         }
      }

      return null;
   }

   public static boolean isVehicle(Entity entity) {
      return entity.getCustomName() != null && entity instanceof ArmorStand && entity.getCustomName().contains("MTVEHICLES");
   }

   public static @Nullable Player getCurrentDriver(String licensePlate) {
      Player driver = null;

      for(World world : Bukkit.getServer().getWorlds()) {
         for(Entity entity : world.getEntities()) {
            if (entity.getCustomName() != null && entity.getCustomName().contains("MAINSEAT_" + licensePlate)) {
               driver = (Player)entity.getPassenger();
            }
         }
      }

      return driver;
   }

   public static String getLicensePlate(@Nullable Entity entity) {
      if (entity == null) {
         return null;
      } else {
         String name = entity.getCustomName();
         return name.split("_").length > 1 ? name.split("_")[2] : null;
      }
   }

   public static String getUUID(String licensePlate) {
      if (!existsByLicensePlate(licensePlate)) {
         return null;
      } else {
         Object skinItem = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM);
         Object skinDamage = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_DAMAGE);
         Object nbtValue = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NBT_VALUE);

         for(Map<?, ?> configVehicle : ConfigModule.vehiclesConfig.getVehicles()) {
            for(Map<?, ?> skin : (List)configVehicle.get("cars")) {
               if (skin.get("itemDamage").equals(skinDamage) && skin.get("SkinItem").equals(skinItem)) {
                  if (skin.get("nbtValue") == null) {
                     return skin.get("uuid").toString();
                  }

                  if (skin.get("nbtValue").equals(nbtValue)) {
                     return skin.get("uuid").toString();
                  }
               }
            }
         }

         return null;
      }
   }

   @ToDo("Beautify the code inside this method.")
   public static Vehicle getVehicle(String licensePlate) {
      if (!existsByLicensePlate(licensePlate)) {
         return null;
      } else {
         Map<String, Object> vehicleData = new HashMap();

         for(VehicleDataConfig.Option option : VehicleDataConfig.Option.values()) {
            Object value = ConfigModule.vehicleDataConfig.get(licensePlate, option);
            if (value != null) {
               vehicleData.put(option.getPath(), value);
            }
         }

         List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
         List<Map<?, ?>> matchedVehicles = new ArrayList();
         double price = (double)0.0F;

         for(Map<?, ?> configVehicle : vehicles) {
            for(Map<?, ?> skin : (List)configVehicle.get("cars")) {
               if (skin.get("itemDamage").equals(vehicleData.get(VehicleDataConfig.Option.SKIN_DAMAGE.getPath())) && skin.get("SkinItem").equals(vehicleData.get(VehicleDataConfig.Option.SKIN_ITEM.getPath()))) {
                  if (skin.get("nbtValue") != null) {
                     if (skin.get("nbtValue").equals(vehicleData.get(VehicleDataConfig.Option.NBT_VALUE.getPath()))) {
                        matchedVehicles.add(configVehicle);
                        price = (Double)skin.get("price");
                     }
                  } else {
                     matchedVehicles.add(configVehicle);
                     price = (Double)skin.get("price");
                  }
               }
            }
         }

         if (matchedVehicles.isEmpty()) {
            return null;
         } else if (matchedVehicles.size() > 1) {
            return null;
         } else {
            return new Vehicle((Map)matchedVehicles.get(0), licensePlate, (String)vehicleData.get(VehicleDataConfig.Option.NAME.getPath()), VehicleType.valueOf((String)vehicleData.get(VehicleDataConfig.Option.VEHICLE_TYPE.getPath())), (Boolean)vehicleData.get(VehicleDataConfig.Option.IS_OPEN.getPath()), (Integer)vehicleData.get(VehicleDataConfig.Option.SKIN_DAMAGE.getPath()), (String)vehicleData.get(VehicleDataConfig.Option.SKIN_ITEM.getPath()), (Boolean)vehicleData.get(VehicleDataConfig.Option.IS_GLOWING.getPath()), ConfigModule.vehicleDataConfig.isHornSet(licensePlate) ? (Boolean)vehicleData.get(VehicleDataConfig.Option.HORN_ENABLED.getPath()) : ConfigModule.vehicleDataConfig.isHornEnabled(licensePlate), ConfigModule.vehicleDataConfig.isHealthSet(licensePlate) ? (Double)vehicleData.get(VehicleDataConfig.Option.HEALTH.getPath()) : ConfigModule.vehicleDataConfig.getHealth(licensePlate), (Boolean)vehicleData.get(VehicleDataConfig.Option.FUEL_ENABLED.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.FUEL.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.FUEL_USAGE.getPath()), (Boolean)vehicleData.get(VehicleDataConfig.Option.TRUNK_ENABLED.getPath()), (Integer)vehicleData.get(VehicleDataConfig.Option.TRUNK_ROWS.getPath()), ConfigModule.vehicleDataConfig.getTrunkData(licensePlate), (Double)vehicleData.get(VehicleDataConfig.Option.ACCELERATION_SPEED.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.MAX_SPEED.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.MAX_SPEED_BACKWARDS.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.BRAKING_SPEED.getPath()), (Double)vehicleData.get(VehicleDataConfig.Option.FRICTION_SPEED.getPath()), (Integer)vehicleData.get(VehicleDataConfig.Option.ROTATION_SPEED.getPath()), UUID.fromString((String)vehicleData.get(VehicleDataConfig.Option.OWNER.getPath())), ConfigModule.vehicleDataConfig.getRiders(licensePlate), ConfigModule.vehicleDataConfig.getMembers(licensePlate), price, (String)vehicleData.get(VehicleDataConfig.Option.NBT_VALUE.getPath()));
         }
      }
   }

   public static boolean existsByLicensePlate(String licensePlate) {
      return ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM) != null;
   }

   public static boolean canRide(Player player, String licensePlate) {
      return ConfigModule.vehicleDataConfig.getRiders(licensePlate).contains(player.getUniqueId().toString());
   }

   public static boolean canSit(Player player, String licensePlate) {
      return ConfigModule.vehicleDataConfig.getMembers(licensePlate).contains(player.getUniqueId().toString());
   }

   public static UUID getOwnerUUID(String licensePlate) {
      Object owner = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.OWNER);
      return owner == null ? null : UUID.fromString(owner.toString());
   }

   public static void openTrunk(Player p, String license) {
      if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.TRUNK_ENABLED)) {
         if (getVehicle(license) == null) {
            ConfigModule.messagesConfig.sendMessage(p, (Message)Message.VEHICLE_NOT_FOUND);
            return;
         }

         if (!getVehicle(license).isOwner(p) && !p.hasPermission("mtvehicles.kofferbak")) {
            p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_TRUNK).replace("%p%", getVehicle(license).getOwnerName())));
         } else {
            ConfigModule.configList.forEach(MTVConfig::reload);
            Inventory inv = Bukkit.createInventory((InventoryHolder)null, (Integer)ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_ROWS) * 9, InventoryTitle.VEHICLE_TRUNK.getStringTitle());
            if (ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_DATA) != null) {
               for(ItemStack item : (List)ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.TRUNK_DATA)) {
                  if (item != null) {
                     inv.addItem(new ItemStack[]{item});
                  }
               }
            }

            openedTrunk.put(p, license);
            VehicleData.trunkViewerAdd(license, p);
            p.openInventory(inv);
         }
      }

   }

   public static boolean isTrunkInventoryOpen(Player p, String license) {
      return openedTrunk.containsKey(p) && ((String)openedTrunk.get(p)).equals(license);
   }

   public static boolean isInsideVehicle(Player p) {
      if (p == null) {
         return false;
      } else {
         return !p.isInsideVehicle() ? false : isVehicle(p.getVehicle());
      }
   }

   public static boolean isOccupied(String licensePlate) {
      return getCurrentDriver(licensePlate) != null;
   }

   /** @deprecated */
   @Deprecated
   public static String getRidersAsString(String licensePlate) {
      StringBuilder sb = new StringBuilder();

      for(String s : ConfigModule.vehicleDataConfig.getRiders(licensePlate)) {
         if (!UUID.fromString(s).equals(getOwnerUUID(licensePlate))) {
            sb.append(Bukkit.getOfflinePlayer(UUID.fromString(s)).getName()).append(", ");
         }
      }

      if (sb.toString().isEmpty()) {
         sb.append("Niemand");
      }

      return sb.toString();
   }

   public static void pickupVehicle(String license, Player player) {
      Vehicle vehicle = getVehicle(license);
      if (vehicle == null) {
         for(World world : Bukkit.getServer().getWorlds()) {
            for(Entity entity : world.getEntities()) {
               if (entity.getCustomName() != null && entity.getCustomName().contains(license)) {
                  entity.remove();
               }
            }
         }

         ConfigModule.messagesConfig.sendMessage(player, (Message)Message.VEHICLE_NOT_FOUND);
      } else if (vehicle.getOwnerName() == null) {
         ConfigModule.messagesConfig.sendMessage(player, (Message)Message.VEHICLE_NOT_FOUND);
         Main.logSevere("Could not find the owner of the vehicle " + license + "! The vehicleData.yml must be malformed!");
      } else if ((!vehicle.isOwner(player) || (Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.CAR_PICKUP)) && !player.hasPermission("mtvehicles.oppakken")) {
         if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.CAR_PICKUP)) {
            player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.CANNOT_DO_THAT_HERE)));
         } else {
            player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_OWNER_PICKUP).replace("%p%", vehicle.getOwnerName())));
         }
      } else {
         for(World world : Bukkit.getServer().getWorlds()) {
            for(Entity entity : world.getEntities()) {
               if (entity.getCustomName() != null && entity.getCustomName().contains(license)) {
                  ArmorStand test = (ArmorStand)entity;
                  if (test.getCustomName().contains("MTVEHICLES_SKIN_" + license)) {
                     for(Player trunkViewer : VehicleData.getTrunkViewers(license)) {
                        trunkViewer.closeInventory();
                     }

                     if (TextUtils.checkInvFull(player)) {
                        ConfigModule.messagesConfig.sendMessage(player, (Message)Message.INVENTORY_FULL);
                        return;
                     }

                     player.getInventory().addItem(new ItemStack[]{test.getHelmet()});
                     player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_PICKUP).replace("%p%", vehicle.getOwnerName())));
                  }

                  test.remove();
               }
            }
         }

      }
   }

   public static void deleteVehicle(String... licensePlates) throws IllegalArgumentException, IllegalStateException {
      for(String licensePlate : licensePlates) {
         if (!existsByLicensePlate(licensePlate)) {
            throw new IllegalArgumentException("Vehicle " + licensePlate + " does not exist.");
         }

         despawnVehicle(licensePlate);
         ConfigModule.vehicleDataConfig.delete(licensePlate);
      }

   }

   public static void teleportVehicle(String licensePlate, Location location) throws IllegalArgumentException {
      if (!existsByLicensePlate(licensePlate)) {
         throw new IllegalArgumentException("Vehicle does not exists.");
      } else {
         for(World world : Bukkit.getServer().getWorlds()) {
            for(Entity entity : world.getEntities()) {
               if (entity.getCustomName() != null && entity.getCustomName().contains(licensePlate)) {
                  entity.teleport(location);
               }
            }
         }

      }
   }

   public static int despawnVehicle(String... licensePlates) throws IllegalArgumentException {
      int despawned = 0;

      for(String licensePlate : licensePlates) {
         if (!existsByLicensePlate(licensePlate)) {
            throw new IllegalArgumentException("Vehicle " + licensePlate + " does not exist.");
         }

         for(Player trunkViewer : VehicleData.getTrunkViewers(licensePlate)) {
            trunkViewer.closeInventory();
         }

         for(World world : Bukkit.getServer().getWorlds()) {
            for(Entity entity : world.getEntities()) {
               if (entity.getCustomName() != null && entity.getCustomName().contains(licensePlate) && entity.getCustomName().contains("MTVEHICLES")) {
                  entity.remove();
                  ++despawned;
               }
            }
         }
      }

      return despawned;
   }

   public static int despawnVehicle(World world, String... licensePlates) throws IllegalArgumentException {
      int despawned = 0;

      for(String licensePlate : licensePlates) {
         if (!existsByLicensePlate(licensePlate)) {
            throw new IllegalArgumentException("Vehicle " + licensePlate + " does not exist.");
         }

         for(Player trunkViewer : VehicleData.getTrunkViewers(licensePlate)) {
            trunkViewer.closeInventory();
         }

         for(Entity entity : world.getEntities()) {
            if (entity.getCustomName() != null && entity.getCustomName().contains(licensePlate)) {
               entity.remove();
               ++despawned;
            }
         }
      }

      return despawned;
   }

   public static List<String> getAllSpawnedVehiclePlates() {
      List<String> list = new ArrayList();

      for(World world : Bukkit.getServer().getWorlds()) {
         for(Entity entity : world.getEntities()) {
            if (entity.getCustomName() != null) {
               String name = entity.getCustomName();
               if (name.contains("MTVEHICLES_MAIN_")) {
                  list.add(name.split("_")[2]);
               }
            }
         }
      }

      return list;
   }

   public static List<String> getAllSpawnedVehiclePlates(World world) {
      List<String> list = new ArrayList();

      for(Entity entity : world.getEntities()) {
         if (entity.getCustomName() != null) {
            String name = entity.getCustomName();
            if (name.contains("MTVEHICLES_MAIN_")) {
               list.add(name.split("_")[2]);
            }
         }
      }

      return list;
   }

   public static Set<String> getUniqueSpawnedVehiclePlates() {
      return new HashSet(getAllSpawnedVehiclePlates());
   }

   public static Set<String> getUniqueSpawnedVehiclePlates(World world) {
      return new HashSet(getAllSpawnedVehiclePlates(world));
   }

   public static boolean setFuel(String licensePlate, Double fuel) {
      if (!existsByLicensePlate(licensePlate)) {
         return false;
      } else if (fuel <= (double)100.0F && fuel >= (double)0.0F) {
         VehicleData.fuel.put(licensePlate, fuel);
         ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL, fuel);
         return true;
      } else {
         return false;
      }
   }

   @ToDo("Beautify the code inside this method.")
   public static void enterVehicle(String licensePlate, Player p) {
      if (VehicleData.autostand2.get(licensePlate) == null || ((ArmorStand)VehicleData.autostand2.get(licensePlate)).isEmpty()) {
         Vehicle vehicle = getVehicle(licensePlate);
         if (vehicle == null) {
            ConfigModule.messagesConfig.sendMessage(p, (Message)Message.VEHICLE_NOT_FOUND);
         } else if (vehicle.getOwnerName() == null) {
            ConfigModule.messagesConfig.sendMessage(p, (Message)Message.VEHICLE_NOT_FOUND);
            Main.logSevere("Could not find the owner of vehicle " + licensePlate + "! The vehicleData.yml must be malformed!");
         } else if (!vehicle.isPublic() && !vehicle.isOwner(p) && !vehicle.canRide(p) && !p.hasPermission("mtvehicles.ride")) {
            p.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_ENTER).replace("%p%", vehicle.getOwnerName()));
         } else {
            for(Entity entity : p.getWorld().getEntities()) {
               if (entity.getCustomName() != null && entity.getCustomName().contains(licensePlate)) {
                  ArmorStand vehicleAs = (ArmorStand)entity;
                  if (!entity.isEmpty()) {
                     return;
                  }

                  VehicleData.fuel.put(licensePlate, vehicle.getFuel());
                  VehicleData.fuelUsage.put(licensePlate, (Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_USAGE));
                  VehicleData.type.put(licensePlate, VehicleType.valueOf(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.VEHICLE_TYPE).toString().toUpperCase(Locale.ROOT)));
                  VehicleData.setRotationSpeed(licensePlate, (Integer)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.ROTATION_SPEED));
                  VehicleData.setSpeed(VehicleData.DataSpeed.MAXSPEED, licensePlate, (Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED));
                  VehicleData.setSpeed(VehicleData.DataSpeed.ACCELERATION, licensePlate, (Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.ACCELERATION_SPEED));
                  VehicleData.setSpeed(VehicleData.DataSpeed.BRAKING, licensePlate, (Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.BRAKING_SPEED));
                  VehicleData.setSpeed(VehicleData.DataSpeed.MAXSPEEDBACKWARDS, licensePlate, (Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED_BACKWARDS));
                  VehicleData.setSpeed(VehicleData.DataSpeed.FRICTION, licensePlate, (Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FRICTION_SPEED));
                  Location location = new Location(entity.getWorld(), entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());
                  if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.ENTER, vehicle.getVehicleType(), location, p)) {
                     ConfigModule.messagesConfig.sendMessage(p, (Message)Message.CANNOT_DO_THAT_HERE);
                     return;
                  }

                  VehicleType vehicleType = ConfigModule.vehicleDataConfig.getType(licensePlate);
                  if (vehicleAs.getCustomName().contains("MTVEHICLES_SKIN_" + licensePlate)) {
                     basicStandCreator(licensePlate, "SKIN", location, vehicleAs.getHelmet(), false);
                     basicStandCreator(licensePlate, "MAIN", location, (ItemStack)null, true);
                     vehicle.saveSeats();
                     List<Map<String, Double>> seats = vehicle.getSeats();
                     VehicleData.seatsize.put(licensePlate, seats.size());

                     for(int i = 1; i <= seats.size(); ++i) {
                        Map<String, Double> seat = (Map)seats.get(i - 1);
                        if (i == 1) {
                           mainSeatStandCreator(licensePlate, location, p, (Double)seat.get("x"), (Double)seat.get("y"), (Double)seat.get("z"));
                           BossBarUtils.addBossBar(p, licensePlate);
                           p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ENTER_RIDER).replace("%p%", getVehicle(licensePlate).getOwnerName())));
                        }

                        if (i > 1) {
                           VehicleData.seatx.put("MTVEHICLES_SEAT" + i + "_" + licensePlate, (Double)seat.get("x"));
                           VehicleData.seaty.put("MTVEHICLES_SEAT" + i + "_" + licensePlate, (Double)seat.get("y"));
                           VehicleData.seatz.put("MTVEHICLES_SEAT" + i + "_" + licensePlate, (Double)seat.get("z"));
                           Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf((Double)seat.get("x")), location.getY() + Double.valueOf((Double)seat.get("y")), location.getZ() + Double.valueOf((Double)seat.get("z")));
                           ArmorStand as = (ArmorStand)location2.getWorld().spawn(location2, ArmorStand.class);
                           allowTicking(as);
                           as.setVisible(false);
                           as.setCustomName("MTVEHICLES_SEAT" + i + "_" + licensePlate);
                           as.setGravity(false);
                           VehicleData.autostand.put("MTVEHICLES_SEAT" + i + "_" + licensePlate, as);
                        }
                     }

                     List<Map<String, Double>> wiekens = (List)vehicle.getVehicleData().get("wiekens");
                     if (vehicleType.isHelicopter()) {
                        VehicleData.maxheight.put(licensePlate, (Integer)ConfigModule.defaultConfig.get(DefaultConfig.Option.MAX_FLYING_HEIGHT));

                        for(int i = 1; i <= wiekens.size(); ++i) {
                           Map<?, ?> seat = (Map)wiekens.get(i - 1);
                           if (i == 1) {
                              Location location2 = new Location(location.getWorld(), location.getX() + (Double)seat.get("z"), Double.valueOf(location.getY()) + (Double)seat.get("y"), location.getZ() + (Double)seat.get("x"));
                              VehicleData.wiekenx.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double)seat.get("x"));
                              VehicleData.wiekeny.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double)seat.get("y"));
                              VehicleData.wiekenz.put("MTVEHICLES_WIEKENS_" + licensePlate, (Double)seat.get("z"));
                              ArmorStand as = (ArmorStand)location2.getWorld().spawn(location2, ArmorStand.class);
                              allowTicking(as);
                              as.setVisible(false);
                              as.setCustomName("MTVEHICLES_WIEKENS_" + licensePlate);
                              as.setGravity(false);
                              as.setHelmet((ItemStack)seat.get("item"));
                              VehicleData.autostand.put("MTVEHICLES_WIEKENS_" + licensePlate, as);
                           }
                        }
                     }
                  }

                  vehicleAs.remove();
               }
            }

         }
      }
   }

   private static void basicStandCreator(String license, String type, Location location, ItemStack item, Boolean gravity) {
      ArmorStand as = (ArmorStand)location.getWorld().spawn(location, ArmorStand.class);
      allowTicking(as);
      as.setVisible(false);
      as.setCustomName("MTVEHICLES_" + type + "_" + license);
      as.setHelmet(item);
      as.setGravity(gravity);
      VehicleData.autostand.put("MTVEHICLES_" + type + "_" + license, as);
   }

   private static void allowTicking(ArmorStand armorStand) {
      if (PaperUtils.isRunningPaper) {
         armorStand.setCanTick(true);
      }

   }

   private static void mainSeatStandCreator(String license, Location location, Player p, double x, double y, double z) {
      Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(z), location.getY() + Double.valueOf(y), location.getZ() + Double.valueOf(z));
      ArmorStand as = (ArmorStand)location2.getWorld().spawn(location2, ArmorStand.class);
      allowTicking(as);
      as.setVisible(false);
      as.setCustomName("MTVEHICLES_MAINSEAT_" + license);
      as.setGravity(false);
      VehicleData.autostand.put("MTVEHICLES_MAINSEAT_" + license, as);
      VehicleData.speed.put(license, (double)0.0F);
      VehicleData.speedhigh.put(license, (double)0.0F);
      VehicleData.mainx.put("MTVEHICLES_MAINSEAT_" + license, x);
      VehicleData.mainy.put("MTVEHICLES_MAINSEAT_" + license, y);
      VehicleData.mainz.put("MTVEHICLES_MAINSEAT_" + license, z);
      as.setPassenger(p);
      VehicleData.autostand2.put(license, as);
   }

   public static Vehicle.Seat getSeat(Player player) {
      return Vehicle.Seat.getSeat(player);
   }

   public static boolean kickOut(Player player) throws IllegalStateException {
      if (getSeat(player) == null) {
         throw new IllegalStateException("Player is not seated in a vehicle!");
      } else {
         Entity seat = player.getVehicle();
         if (!getSeat(player).isDriver()) {
            return seat.removePassenger(player);
         } else {
            String license = getLicensePlate(seat);
            if (seat.removePassenger(player)) {
               BossBarUtils.removeBossBar(player, license);
               return turnOff(license);
            } else {
               return false;
            }
         }
      }
   }

   public static Location getLocation(Vehicle vehicle) {
      return getLocation(vehicle.getLicensePlate());
   }

   public static Location getLocation(String licensePlate) {
      return VehicleData.autostand.get("MTVEHICLES_MAIN_" + licensePlate) == null ? null : ((ArmorStand)VehicleData.autostand.get("MTVEHICLES_MAIN_" + licensePlate)).getLocation();
   }

   public static boolean turnOff(@NotNull Vehicle vehicle) {
      String licensePlate = vehicle.getLicensePlate();
      if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + licensePlate) == null) {
         return false;
      } else {
         ArmorStand standMain = (ArmorStand)VehicleData.autostand.get("MTVEHICLES_MAIN_" + licensePlate);
         ArmorStand standSkin = (ArmorStand)VehicleData.autostand.get("MTVEHICLES_SKIN_" + licensePlate);
         ArmorStand standMainSeat = (ArmorStand)VehicleData.autostand.get("MTVEHICLES_MAINSEAT_" + licensePlate);
         VehicleType vehicleType = (VehicleType)VehicleData.type.get(licensePlate);
         VehicleData.lastRegions.remove(licensePlate);
         if (vehicleType == null) {
            return true;
         } else {
            if (vehicleType.isHelicopter()) {
               ArmorStand blades = (ArmorStand)VehicleData.autostand.get("MTVEHICLES_WIEKENS_" + licensePlate);
               Location locBelow = new Location(blades.getLocation().getWorld(), blades.getLocation().getX(), blades.getLocation().getY() - 0.2, blades.getLocation().getZ(), blades.getLocation().getYaw(), blades.getLocation().getPitch());
               blades.setGravity(locBelow.getBlock().getType().equals(Material.AIR));
            }

            if (vehicleType.isHelicopter() && (Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.EXTREME_HELICOPTER_FALL) && !standMainSeat.isOnGround()) {
               VehicleData.fallDamage.put(licensePlate, true);
            }

            if (!vehicleType.isBoat()) {
               standMain.setGravity(true);
               standSkin.setGravity(true);
            }

            List<Map<String, Double>> seats = vehicle.getSeats();

            for(int i = 2; i <= seats.size(); ++i) {
               if (VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + licensePlate) != null) {
                  ((ArmorStand)VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + licensePlate)).remove();
               }
            }

            VehicleData.type.remove(licensePlate);
            if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED) && (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)) {
               double fuel = (Double)VehicleData.fuel.get(licensePlate);
               ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL, fuel);
               ConfigModule.vehicleDataConfig.saveToDisk();
            }

            return true;
         }
      }
   }

   public static boolean turnOff(@NotNull String licensePlate) {
      return getVehicle(licensePlate) == null ? false : turnOff(getVehicle(licensePlate));
   }

   public static Double getPrice(String carUUID) {
      for(Map<?, ?> configVehicle : ConfigModule.vehiclesConfig.getVehicles()) {
         for(Map<?, ?> skin : (List)configVehicle.get("cars")) {
            if (skin.get("uuid") != null && skin.get("uuid").equals(carUUID) && skin.get("uuid") != null) {
               return (Double)skin.get("price");
            }
         }
      }

      return null;
   }
}
