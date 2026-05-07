package nl.mtvehicles.core.infrastructure.dependencies;

import java.text.DecimalFormat;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderUtils extends PlaceholderExpansion {
   private final Main plugin;

   public PlaceholderUtils() {
      this.plugin = Main.instance;
   }

   public String getAuthor() {
      return "MTVehicles";
   }

   public String getIdentifier() {
      return "mtv";
   }

   public String getVersion() {
      return VersionModule.getPluginVersion();
   }

   public boolean persist() {
      return true;
   }

   public String onRequest(OfflinePlayer p, String parameter) {
      if (parameter.equalsIgnoreCase("fuel_pricePerLitre")) {
         return ConfigModule.defaultConfig.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE).toString();
      } else if (parameter.equalsIgnoreCase("vehicle_licensePlate")) {
         if (!p.isOnline()) {
            return "";
         } else {
            return !VehicleUtils.isInsideVehicle(p.getPlayer()) ? "" : VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
         }
      } else if (parameter.equalsIgnoreCase("vehicle_name")) {
         if (!p.isOnline()) {
            return "";
         } else if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
            return "";
         } else {
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString();
         }
      } else if (parameter.equalsIgnoreCase("vehicle_type")) {
         if (!p.isOnline()) {
            return "";
         } else if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
            return "";
         } else {
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return VehicleType.valueOf(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.VEHICLE_TYPE).toString()).getName();
         }
      } else if (parameter.equalsIgnoreCase("vehicle_fuel")) {
         if (!p.isOnline()) {
            return "";
         } else if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
            return "";
         } else {
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            Double fuel = VehicleUtils.getVehicle(licensePlate).getCurrentFuel();
            if (fuel == null) {
               return "";
            } else {
               DecimalFormat df = new DecimalFormat("#.##");
               return df.format(fuel) + " %";
            }
         }
      } else if (parameter.equalsIgnoreCase("vehicle_speed")) {
         if (!p.isOnline()) {
            return "";
         } else if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
            return "";
         } else {
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            Double speed = VehicleUtils.getVehicle(licensePlate).getCurrentSpeed();
            if (speed == null) {
               return "0.0 blocks/sec";
            } else {
               DecimalFormat df = new DecimalFormat("#.###");
               return df.format(speed) + " blocks/sec";
            }
         }
      } else if (parameter.equalsIgnoreCase("vehicle_maxspeed")) {
         if (!p.isOnline()) {
            return "";
         } else if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
            return "";
         } else {
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            DecimalFormat df = new DecimalFormat("#.###");
            return df.format((Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED) * (double)20.0F) + " blocks/sec";
         }
      } else if (parameter.equalsIgnoreCase("vehicle_place")) {
         if (!p.isOnline()) {
            return "";
         } else if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
            return "";
         } else {
            Vehicle.Seat seat = Vehicle.Seat.getSeat(p.getPlayer());
            return seat == null ? "" : seat.toString();
         }
      } else if (parameter.equalsIgnoreCase("vehicle_seats")) {
         if (!p.isOnline()) {
            return "";
         } else if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
            return "";
         } else {
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
            return String.valueOf(vehicle.getSeatsAmount());
         }
      } else if (parameter.equalsIgnoreCase("vehicle_uuid")) {
         if (!p.isOnline()) {
            return "";
         } else if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
            return "";
         } else {
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return VehicleUtils.getUUID(licensePlate);
         }
      } else if (parameter.equalsIgnoreCase("vehicle_owner")) {
         if (!p.isOnline()) {
            return "";
         } else if (!VehicleUtils.isInsideVehicle(p.getPlayer())) {
            return "";
         } else {
            String licensePlate = VehicleUtils.getLicensePlate(p.getPlayer().getVehicle());
            return VehicleUtils.getVehicle(licensePlate).getOwnerName();
         }
      } else {
         return null;
      }
   }

   public static String parsePlaceholders(Player player, String text) {
      return PlaceholderAPI.setPlaceholders(player, text);
   }

   public void unregisterOnDisable() {
      this.unregister();
   }
}
