package nl.mtvehicles.core.infrastructure.utils;

import java.util.HashMap;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarUtils {
   public static HashMap<String, BossBar> Fuelbar = new HashMap();

   public static void setBossBarValue(double counter, String licensePlate) {
      if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED) && (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)) {
         if (!Fuelbar.containsKey(licensePlate)) {
            return;
         }

         ((BossBar)Fuelbar.get(licensePlate)).setProgress(counter);
         ((BossBar)Fuelbar.get(licensePlate)).setTitle(Math.round(counter * (double)100.0F) + "% " + TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.BOSSBAR_FUEL)));
         Double fuel = (Double)VehicleData.fuel.get(licensePlate);
         if (fuel < (double)30.0F) {
            ((BossBar)Fuelbar.get(licensePlate)).setColor(BarColor.RED);
            return;
         }

         if (fuel < (double)60.0F) {
            ((BossBar)Fuelbar.get(licensePlate)).setColor(BarColor.YELLOW);
            return;
         }

         if (fuel < (double)100.0F) {
            ((BossBar)Fuelbar.get(licensePlate)).setColor(BarColor.GREEN);
         }
      }

   }

   public static void removeBossBar(Player player, String licensePlate) {
      if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED) && (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)) {
         ((BossBar)Fuelbar.get(licensePlate)).removePlayer(player);
      }

   }

   public static void addBossBar(Player player, String licensePlate) {
      if ((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED) && (Boolean)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED)) {
         double fuel = (Double)ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL);
         String fuelString = String.valueOf(fuel);
         BossBar bar = Bukkit.createBossBar(Math.round(Double.parseDouble(fuelString)) + "% " + TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.BOSSBAR_FUEL)), BarColor.GREEN, BarStyle.SOLID, new BarFlag[0]);
         Fuelbar.put(licensePlate, bar);
         if (fuel < (double)30.0F) {
            ((BossBar)Fuelbar.get(licensePlate)).setColor(BarColor.RED);
         }

         if (fuel < (double)60.0F) {
            ((BossBar)Fuelbar.get(licensePlate)).setColor(BarColor.YELLOW);
         }

         if (fuel < (double)100.0F) {
            ((BossBar)Fuelbar.get(licensePlate)).setColor(BarColor.GREEN);
         }

         ((BossBar)Fuelbar.get(licensePlate)).addPlayer(player);
      }

   }
}
