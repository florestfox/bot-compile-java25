package nl.mtvehicles.core.infrastructure.vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Vehicle {
   private String licensePlate;
   private String name;
   private VehicleType vehicleType;
   private int skinDamage;
   private String skinItem;
   private boolean isGlowing;
   private boolean benzineEnabled;
   private double fuel;
   private double fuelUsage;
   private boolean hornEnabled;
   private double health;
   private boolean trunkEnabled;
   private int trunkRows;
   private List<String> kofferbakData;
   private double acceleratieSpeed;
   private double maxSpeed;
   private double brakingSpeed;
   private double frictionSpeed;
   private int rotateSpeed;
   private double maxSpeedBackwards;
   private boolean isPublic;
   private double price;
   private UUID owner;
   private String nbtValue;
   private List<String> riders;
   private List<String> members;
   private @Nullable Map<?, ?> vehicleData;
   /** @deprecated */
   @Deprecated
   public static HashMap<String, MTVSubCommand> subcommands = new HashMap();

   public Vehicle() {
   }

   public Vehicle(@Nullable Map<?, ?> vehicleData, String licensePlate, String name, VehicleType vehicleType, boolean isPublic, int skinDamage, String skinItem, boolean glowing, boolean hornEnabled, double health, boolean fuelEnabled, double fuel, double fuelUsage, boolean trunkEnabled, int trunkRows, List<String> trunkData, double accelerationSpeed, double maxSpeed, double maxSpeedBackwards, double brakingSpeed, double frictionSpeed, int rotateSpeed, UUID owner, List<String> riders, List<String> members, double price, String nbtValue) {
      this.setVehicleData(vehicleData);
      this.setLicensePlate(licensePlate);
      this.setPublic(isPublic);
      this.setName(name);
      this.setVehicleType(vehicleType);
      this.setSkinDamage(skinDamage);
      this.setSkinItem(skinItem);
      this.setGlowing(glowing);
      this.setHornEnabled(hornEnabled);
      this.setHealth(health);
      this.setBenzineEnabled(fuelEnabled);
      this.setFuel(fuel);
      this.setFuelUsage(fuelUsage);
      this.setTrunk(trunkEnabled);
      this.setTrunkRows(trunkRows);
      this.setTrunkData(trunkData);
      this.setAccelerationSpeed(accelerationSpeed);
      this.setMaxSpeed(maxSpeed);
      this.setBrakingSpeed(brakingSpeed);
      this.setFrictionSpeed(frictionSpeed);
      this.setRotateSpeed(rotateSpeed);
      this.setMaxSpeedBackwards(maxSpeedBackwards);
      this.setOwner(owner);
      this.setRiders(riders);
      this.setMembers(members);
      this.setPrice(price);
      this.setNbtValue(nbtValue);
   }

   public void save() {
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.NAME, this.getName());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.VEHICLE_TYPE, this.getVehicleType().toString());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.SKIN_DAMAGE, this.getSkinDamage());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.SKIN_ITEM, this.getSkinItem());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.IS_OPEN, this.isPublic());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.IS_GLOWING, this.isGlowing());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.FUEL_ENABLED, this.isFuelEnabled());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.FUEL, this.getFuel());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.FUEL_USAGE, this.getFuelUsage());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.HORN_ENABLED, this.isHornEnabled());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.HEALTH, this.getHealth());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.TRUNK_ENABLED, this.isTrunkEnabled());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.TRUNK_ROWS, this.getTrunkRows());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.TRUNK_DATA, this.getTrunkData());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.ACCELERATION_SPEED, this.getAccelerationSpeed());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.MAX_SPEED, this.getMaxSpeed());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.BRAKING_SPEED, this.getBrakingSpeed());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.FRICTION_SPEED, this.getFrictionSpeed());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.ROTATION_SPEED, this.getRotateSpeed());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.MAX_SPEED_BACKWARDS, this.getMaxSpeedBackwards());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.OWNER, this.getOwnerUUID().toString());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.NBT_VALUE, this.getNbtValue());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.RIDERS, this.getRiders());
      ConfigModule.vehicleDataConfig.set(this.getLicensePlate(), VehicleDataConfig.Option.MEMBERS, this.getMembers());
   }

   public String toString() {
      if (this.getName() == null) {
         return "an MTV vehicle";
      } else {
         return this.getLicensePlate() == null ? "a " + this.getName() : this.getName() + " with license plate " + this.getLicensePlate();
      }
   }

   public void delete() throws IllegalStateException {
      ConfigModule.vehicleDataConfig.delete(this.getLicensePlate());
   }

   public String getUUID() {
      return VehicleUtils.getUUID(this.getLicensePlate());
   }

   public int getSeatsAmount() {
      List<Map<String, Double>> seats = (List)this.getVehicleData().get("seats");
      return seats.size();
   }

   public @Nullable Player getCurrentDriver() {
      return VehicleUtils.getCurrentDriver(this.getLicensePlate());
   }

   public List<Map<String, Double>> getSeats() {
      return (List)this.getVehicleData().get("seats");
   }

   public @Nullable Double getCurrentSpeed() {
      return VehicleData.speed.get(this.getLicensePlate()) == null ? null : (Double)VehicleData.speed.get(this.licensePlate) * (double)20.0F;
   }

   public @Nullable Double getCurrentFuel() {
      if (!(Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED)) {
         return null;
      } else {
         return !(Boolean)ConfigModule.vehicleDataConfig.get(this.getLicensePlate(), VehicleDataConfig.Option.FUEL_ENABLED) ? null : (Double)VehicleData.fuel.get(this.licensePlate);
      }
   }

   public String getLicensePlate() {
      return this.licensePlate;
   }

   public void setLicensePlate(String licensePlate) {
      this.licensePlate = licensePlate;
   }

   public String getName() {
      return this.name;
   }

   public int getSkinDamage() {
      return this.skinDamage;
   }

   public String getSkinItem() {
      return this.skinItem;
   }

   public boolean isGlowing() {
      return this.isGlowing;
   }

   /** @deprecated */
   @Deprecated
   public boolean isGlow() {
      return this.isGlowing;
   }

   public boolean isPublic() {
      return this.isPublic;
   }

   /** @deprecated */
   @Deprecated
   public boolean isOpen() {
      return this.isPublic;
   }

   public boolean isFuelEnabled() {
      return this.benzineEnabled;
   }

   public boolean isHornEnabled() {
      return this.hornEnabled;
   }

   public double getHealth() {
      return this.health;
   }

   public double getFuel() {
      return this.fuel;
   }

   public boolean isTrunkEnabled() {
      return this.trunkEnabled;
   }

   public int getTrunkRows() {
      return this.trunkRows;
   }

   public double getAccelerationSpeed() {
      return this.acceleratieSpeed;
   }

   public double getMaxSpeed() {
      return this.maxSpeed;
   }

   public double getBrakingSpeed() {
      return this.brakingSpeed;
   }

   public double getFrictionSpeed() {
      return this.frictionSpeed;
   }

   public int getRotateSpeed() {
      return this.rotateSpeed;
   }

   public double getMaxSpeedBackwards() {
      return this.maxSpeedBackwards;
   }

   public UUID getOwnerUUID() {
      return this.owner;
   }

   public String getOwnerName() {
      return Bukkit.getOfflinePlayer(this.getOwnerUUID()).getName();
   }

   public boolean isOwner(OfflinePlayer player) {
      return this.owner.equals(player.getUniqueId());
   }

   public String getNbtValue() {
      return this.nbtValue;
   }

   public List<String> getRiders() {
      return this.riders;
   }

   public List<String> getMembers() {
      return this.members;
   }

   public double getFuelUsage() {
      return this.fuelUsage;
   }

   public double getPrice() {
      return this.price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setSkinDamage(int skinDamage) {
      this.skinDamage = skinDamage;
   }

   public void setSkinItem(String skinItem) {
      this.skinItem = skinItem;
   }

   public void setGlowing(boolean glow) {
      this.isGlowing = glow;
   }

   /** @deprecated */
   @Deprecated
   public void setGlow(boolean glow) {
      this.isGlowing = glow;
   }

   public void setPublic(boolean isPublic) {
      this.isPublic = isPublic;
   }

   /** @deprecated */
   @Deprecated
   public void setOpen(boolean isPublic) {
      this.isPublic = isPublic;
   }

   public void setBenzineEnabled(boolean benzineEnabled) {
      this.benzineEnabled = benzineEnabled;
   }

   public void setHornEnabled(boolean hornEnabled) {
      this.hornEnabled = hornEnabled;
   }

   public void setHealth(double health) {
      this.health = health;
   }

   public void setFuel(double fuel) {
      this.fuel = fuel;
   }

   public void setTrunk(boolean trunk) {
      this.trunkEnabled = trunk;
   }

   public void setTrunkRows(int trunkRows) {
      this.trunkRows = trunkRows;
   }

   public List<String> getTrunkData() {
      return this.kofferbakData;
   }

   public void setTrunkData(List<String> trunkData) {
      this.kofferbakData = trunkData;
   }

   public void setAccelerationSpeed(double accelerationSpeed) {
      this.acceleratieSpeed = accelerationSpeed;
   }

   public void setMaxSpeed(double maxSpeed) {
      this.maxSpeed = maxSpeed;
   }

   public void setBrakingSpeed(double brakingSpeed) {
      this.brakingSpeed = brakingSpeed;
   }

   public void setFrictionSpeed(double frictionSpeed) {
      this.frictionSpeed = frictionSpeed;
   }

   public void setRotateSpeed(int rotateSpeed) {
      this.rotateSpeed = rotateSpeed;
   }

   public void setMaxSpeedBackwards(double maxSpeedBackwards) {
      this.maxSpeedBackwards = maxSpeedBackwards;
   }

   /** @deprecated */
   @Deprecated
   public void setOwner(String ownerUUID) {
      try {
         this.owner = UUID.fromString(ownerUUID);
      } catch (IllegalArgumentException var3) {
         Main.logSevere("An error occurred while setting a vehicle's owner. This may lead to further issues...");
      }

   }

   public void setOwner(UUID owner) {
      this.owner = owner;
   }

   public void setNbtValue(String nbt) {
      this.nbtValue = nbt;
   }

   public void setRiders(List<String> riders) {
      this.riders = riders;
   }

   public void setMembers(List<String> members) {
      this.members = members;
   }

   public void setFuelUsage(double fuelUsage) {
      this.fuelUsage = fuelUsage;
   }

   public Map<?, ?> getVehicleData() {
      return this.vehicleData;
   }

   public void setVehicleData(Map<?, ?> vehicleData) {
      this.vehicleData = vehicleData;
   }

   public boolean canRide(Player player) {
      return ConfigModule.vehicleDataConfig.getRiders(this.licensePlate).contains(player.getUniqueId().toString());
   }

   public boolean canSit(Player player) {
      return ConfigModule.vehicleDataConfig.getMembers(this.licensePlate).contains(player.getUniqueId().toString());
   }

   public VehicleType getVehicleType() {
      return this.vehicleType;
   }

   public void setVehicleType(VehicleType vehicleType) {
      this.vehicleType = vehicleType;
   }

   public void saveSeats() {
      List<Map<String, Double>> seats = this.getSeats();
      VehicleData.seatsize.put(this.licensePlate, seats.size());

      for(int i = 1; i <= seats.size(); ++i) {
         Map<String, Double> seat = (Map)seats.get(i - 1);
         if (i == 1) {
            VehicleData.mainx.put("MTVEHICLES_MAINSEAT_" + this.licensePlate, (Double)seat.get("x"));
            VehicleData.mainy.put("MTVEHICLES_MAINSEAT_" + this.licensePlate, (Double)seat.get("y"));
            VehicleData.mainz.put("MTVEHICLES_MAINSEAT_" + this.licensePlate, (Double)seat.get("z"));
         } else if (i > 1) {
            VehicleData.seatx.put("MTVEHICLES_SEAT" + i + "_" + this.licensePlate, (Double)seat.get("x"));
            VehicleData.seaty.put("MTVEHICLES_SEAT" + i + "_" + this.licensePlate, (Double)seat.get("y"));
            VehicleData.seatz.put("MTVEHICLES_SEAT" + i + "_" + this.licensePlate, (Double)seat.get("z"));
         }
      }

   }

   public boolean isOccupied() {
      return VehicleUtils.isOccupied(this.getLicensePlate());
   }

   public static enum Seat {
      DRIVER,
      PASSENGER;

      public static Seat getSeat(Player player) throws IllegalStateException {
         if (!VehicleUtils.isInsideVehicle(player)) {
            throw new IllegalStateException("Player is not seated in a vehicle!");
         } else {
            String vehicleName = player.getVehicle().getCustomName();
            if (vehicleName.contains("MAINSEAT")) {
               return DRIVER;
            } else {
               return vehicleName.contains("SEAT") ? PASSENGER : null;
            }
         }
      }

      public boolean isDriver() {
         return this.equals(DRIVER);
      }

      public boolean isPassenger() {
         return this.equals(PASSENGER);
      }

      // $FF: synthetic method
      private static Seat[] $values() {
         return new Seat[]{DRIVER, PASSENGER};
      }
   }
}
