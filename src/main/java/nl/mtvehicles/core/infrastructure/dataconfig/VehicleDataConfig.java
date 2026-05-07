package nl.mtvehicles.core.infrastructure.dataconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class VehicleDataConfig extends MTVConfig {
   private Map<String, ConfigurationSection> vehicleDataInMemory = new HashMap();
   private BukkitRunnable saveTask;

   public VehicleDataConfig() {
      super(ConfigType.VEHICLE_DATA);
      this.loadFromDisk();
      this.startAutoSaveTask();
   }

   private void loadFromDisk() {
      ConfigurationSection vehiclesSection = this.getConfiguration().getConfigurationSection("vehicle");
      if (vehiclesSection != null) {
         for(String licensePlate : vehiclesSection.getKeys(false)) {
            this.vehicleDataInMemory.put(licensePlate, vehiclesSection.getConfigurationSection(licensePlate));
         }
      }

   }

   private void startAutoSaveTask() {
      this.saveTask = new BukkitRunnable() {
         public void run() {
            VehicleDataConfig.this.saveToDisk();
         }
      };
      this.saveTask.runTaskTimer(Main.instance, 12000L, 12000L);
   }

   public void saveToDisk() {
      this.clearFile(false);

      for(Map.Entry<String, ConfigurationSection> entry : this.vehicleDataInMemory.entrySet()) {
         this.getConfiguration().set("vehicle." + (String)entry.getKey(), entry.getValue());
      }

      this.save();
   }

   private void clearFile(boolean save) {
      this.getConfiguration().set("vehicle", (Object)null);
      if (save) {
         this.save();
      }

   }

   public Object get(String licensePlate, Option dataOption) {
      ConfigurationSection vehicleSection = (ConfigurationSection)this.vehicleDataInMemory.getOrDefault(licensePlate, (Object)null);
      return vehicleSection == null ? null : vehicleSection.get(dataOption.getPath());
   }

   public void set(String licensePlate, Option dataOption, Object value) {
      ConfigurationSection vehicleSection = (ConfigurationSection)this.vehicleDataInMemory.computeIfAbsent(licensePlate, (k) -> this.getConfiguration().createSection("vehicle." + k));
      vehicleSection.set(dataOption.getPath(), value);
   }

   public void delete(String licensePlate) throws IllegalStateException {
      if (!this.vehicleDataInMemory.containsKey(licensePlate)) {
         throw new IllegalStateException("An error occurred while trying to delete a vehicle. Vehicle is already deleted.");
      } else {
         this.vehicleDataInMemory.remove(licensePlate);
         this.saveToDisk();
      }
   }

   public boolean isEmpty() {
      return this.vehicleDataInMemory.isEmpty();
   }

   public Map<String, ConfigurationSection> getVehicles() {
      return new HashMap(this.vehicleDataInMemory);
   }

   public int getDamage(String licensePlate) {
      return (Integer)this.get(licensePlate, VehicleDataConfig.Option.SKIN_DAMAGE);
   }

   public int getDamage(Vehicle vehicle) {
      return this.getDamage(vehicle.getLicensePlate());
   }

   public List<String> getMembers(String licensePlate) {
      return (List<String>)(this.get(licensePlate, VehicleDataConfig.Option.MEMBERS) == null ? new ArrayList() : (List)this.get(licensePlate, VehicleDataConfig.Option.MEMBERS));
   }

   public List<String> getRiders(String licensePlate) {
      return (List<String>)(this.get(licensePlate, VehicleDataConfig.Option.RIDERS) == null ? new ArrayList() : (List)this.get(licensePlate, VehicleDataConfig.Option.RIDERS));
   }

   public List<String> getTrunkData(String licensePlate) {
      return (List<String>)(this.get(licensePlate, VehicleDataConfig.Option.TRUNK_DATA) == null ? new ArrayList() : (List)this.get(licensePlate, VehicleDataConfig.Option.TRUNK_DATA));
   }

   public VehicleType getType(String licensePlate) {
      try {
         return VehicleType.valueOf(this.get(licensePlate, VehicleDataConfig.Option.VEHICLE_TYPE).toString().toUpperCase(Locale.ROOT));
      } catch (IllegalArgumentException var3) {
         Main.logSevere("An error occurred while setting a vehicle's type. Using default (CAR)...");
         return VehicleType.CAR;
      }
   }

   public boolean isHornEnabled(String license) {
      Boolean horn = (Boolean)this.get(license, VehicleDataConfig.Option.HORN_ENABLED);
      if (horn == null) {
         this.saveToDisk();
         String path = "vehicle." + license + ".hornEnabled";
         if (!this.isHornSet(license)) {
            this.setInitialHorn(license);
         }

         return this.getConfiguration().getBoolean(path);
      } else {
         return horn;
      }
   }

   public boolean isHornSet(String license) {
      String path = "vehicle." + license + ".hornEnabled";
      return this.getConfiguration().isSet(path);
   }

   public void setInitialHorn(String license) {
      String path = "vehicle." + license + ".hornEnabled";
      boolean state = VehicleUtils.getHornByDamage(this.getDamage(license));
      this.getConfiguration().set(path, state);
      this.save();
      this.loadFromDisk();
   }

   public double getHealth(String license) {
      Double health = (Double)this.get(license, VehicleDataConfig.Option.HEALTH);
      if (health == null) {
         this.saveToDisk();
         String path = "vehicle." + license + ".health";
         if (!this.isHealthSet(license)) {
            this.setInitialHealth(license);
         }

         return this.getConfiguration().getDouble(path);
      } else {
         return health;
      }
   }

   public boolean isHealthSet(String license) {
      String path = "vehicle." + license + ".health";
      return this.getConfiguration().isSet(path);
   }

   public void setInitialHealth(String license) {
      String path = "vehicle." + license + ".health";
      int damage = this.getConfiguration().getInt("vehicle." + license + ".skinDamage");
      double state = VehicleUtils.getMaxHealthByDamage(damage);
      this.getConfiguration().set(path, state);
      this.save();
      this.loadFromDisk();
   }

   public void damageVehicle(String license, double damage) {
      double health = this.getHealth(license) - damage;
      this.set(license, VehicleDataConfig.Option.HEALTH, Math.max(health, (double)0.0F));
   }

   public void setHealth(String license, double health) {
      this.set(license, VehicleDataConfig.Option.HEALTH, health);
      String path = "vehicle." + license + ".health";
      this.getConfiguration().set(path, health);
      this.save();
   }

   public int getNumberOfOwnedVehicles(Player p) {
      String playerUUID = p.getUniqueId().toString();
      Map<String, Object> vehicleData = this.getConfiguration().getValues(true);
      int owned = (int)vehicleData.keySet().stream().filter((key) -> key.contains(".owner") && String.valueOf(vehicleData.get(key)).equals(playerUUID)).count();
      return owned;
   }

   public static enum Option {
      NAME("name"),
      VEHICLE_TYPE("vehicleType"),
      SKIN_ITEM("skinItem"),
      SKIN_DAMAGE("skinDamage"),
      OWNER("owner"),
      RIDERS("riders"),
      MEMBERS("members"),
      FUEL_ENABLED("benzineEnabled"),
      FUEL("benzine"),
      FUEL_USAGE("benzineVerbruik"),
      BRAKING_SPEED("brakingSpeed"),
      FRICTION_SPEED("aftrekkenSpeed"),
      ACCELERATION_SPEED("acceleratieSpeed"),
      MAX_SPEED("maxSpeed"),
      MAX_SPEED_BACKWARDS("maxSpeedBackwards"),
      ROTATION_SPEED("rotateSpeed"),
      TRUNK_ENABLED("kofferbak"),
      TRUNK_ROWS("kofferbakRows"),
      TRUNK_DATA("kofferbakData"),
      IS_OPEN("isOpen"),
      IS_GLOWING("isGlow"),
      HORN_ENABLED("hornEnabled"),
      HEALTH("health"),
      NBT_VALUE("nbtValue"),
      IS_PUBLIC("isPublic");

      private final String path;

      private Option(String path) {
         this.path = path;
      }

      public String getPath() {
         return this.path;
      }

      // $FF: synthetic method
      private static Option[] $values() {
         return new Option[]{NAME, VEHICLE_TYPE, SKIN_ITEM, SKIN_DAMAGE, OWNER, RIDERS, MEMBERS, FUEL_ENABLED, FUEL, FUEL_USAGE, BRAKING_SPEED, FRICTION_SPEED, ACCELERATION_SPEED, MAX_SPEED, MAX_SPEED_BACKWARDS, ROTATION_SPEED, TRUNK_ENABLED, TRUNK_ROWS, TRUNK_DATA, IS_OPEN, IS_GLOWING, HORN_ENABLED, HEALTH, NBT_VALUE, IS_PUBLIC};
      }
   }
}
