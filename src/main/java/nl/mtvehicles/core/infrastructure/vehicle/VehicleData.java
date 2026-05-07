package nl.mtvehicles.core.infrastructure.vehicle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@ToDo("make nicer, move, refactor - for better API usage")
public class VehicleData {
   public static HashMap<String, Double> speed = new HashMap();
   /** @deprecated */
   @Deprecated
   public static HashMap<String, Double> speedhigh = new HashMap();
   public static HashMap<String, Integer> maxheight = new HashMap();
   public static HashMap<String, Double> mainx = new HashMap();
   public static HashMap<String, Double> mainy = new HashMap();
   public static HashMap<String, Double> mainz = new HashMap();
   public static HashMap<String, Integer> seatsize = new HashMap();
   public static HashMap<String, Double> seatx = new HashMap();
   public static HashMap<String, Double> seaty = new HashMap();
   public static HashMap<String, Double> seatz = new HashMap();
   public static HashMap<String, Double> wiekenx = new HashMap();
   public static HashMap<String, Double> wiekeny = new HashMap();
   public static HashMap<String, Double> wiekenz = new HashMap();
   public static HashMap<String, VehicleType> type = new HashMap();
   public static HashMap<String, Double> fuel = new HashMap();
   public static HashMap<String, Double> fuelUsage = new HashMap();
   public static HashMap<String, ArmorStand> autostand = new HashMap();
   public static HashMap<String, ArmorStand> autostand2 = new HashMap();
   public static Map<String, Long> lastUsage = new HashMap();
   public static HashMap<String, Boolean> fallDamage = new HashMap();
   private static HashMap<String, Integer> RotationSpeed = new HashMap();
   private static HashMap<String, Double> MaxSpeed = new HashMap();
   private static HashMap<String, Double> AccelerationSpeed = new HashMap();
   private static HashMap<String, Double> BrakingSpeed = new HashMap();
   private static HashMap<String, Double> MaxSpeedBackwards = new HashMap();
   private static HashMap<String, Double> FrictionSpeed = new HashMap();
   public static Set<String> frictionBlocked = new HashSet();
   public static Set<String> brakingBlocked = new HashSet();
   public static HashMap<String, Set<String>> lastRegions = new HashMap();
   public static HashMap<String, Boolean> destroyedVehicles = new HashMap();
   private static HashMap<String, Set<Player>> trunkViewers = new HashMap();

   private VehicleData() {
   }

   public static boolean isTrunkViewer(String licensePlate, Player player) {
      setTrunkViewers(licensePlate);
      return ((Set)trunkViewers.get(licensePlate)).contains(player);
   }

   public static Double getSpeed(@NotNull DataSpeed speedType, @NotNull String licensePlate) {
      if (speedType == VehicleData.DataSpeed.MAXSPEED) {
         return (Double)MaxSpeed.get(licensePlate);
      } else if (speedType == VehicleData.DataSpeed.ACCELERATION) {
         return (Double)AccelerationSpeed.get(licensePlate);
      } else if (speedType == VehicleData.DataSpeed.BRAKING) {
         return brakingBlocked.contains(licensePlate) ? (double)0.0F : (Double)BrakingSpeed.get(licensePlate);
      } else if (speedType == VehicleData.DataSpeed.MAXSPEEDBACKWARDS) {
         return (Double)MaxSpeedBackwards.get(licensePlate);
      } else if (speedType == VehicleData.DataSpeed.FRICTION) {
         return frictionBlocked.contains(licensePlate) ? (double)0.0F : (Double)FrictionSpeed.get(licensePlate);
      } else {
         return null;
      }
   }

   public static Integer getRotationSpeed(String licensePlate) {
      return (Integer)RotationSpeed.get(licensePlate);
   }

   public static void setSpeed(@NotNull DataSpeed speedType, @NotNull String licensePlate, @NotNull Double value) {
      switch (speedType.ordinal()) {
         case 0:
            MaxSpeed.put(licensePlate, value);
            break;
         case 1:
            AccelerationSpeed.put(licensePlate, value);
            break;
         case 2:
            BrakingSpeed.put(licensePlate, value);
            break;
         case 3:
            MaxSpeedBackwards.put(licensePlate, value);
            break;
         case 4:
            FrictionSpeed.put(licensePlate, value);
      }

   }

   public static void setRotationSpeed(String licensePlate, Integer value) {
      RotationSpeed.put(licensePlate, value);
   }

   public static boolean trunkViewerAdd(String licensePlate, Player player) {
      setTrunkViewers(licensePlate);
      Set<Player> viewers = (Set)trunkViewers.get(licensePlate);
      boolean returns = viewers.add(player);
      trunkViewers.put(licensePlate, viewers);
      return returns;
   }

   public static boolean trunkViewerRemove(String licensePlate, Player player) {
      setTrunkViewers(licensePlate);
      Set<Player> viewers = (Set)trunkViewers.get(licensePlate);
      boolean returns = viewers.remove(player);
      trunkViewers.put(licensePlate, viewers);
      return returns;
   }

   public static Set<Player> getTrunkViewers(String licensePlate) {
      setTrunkViewers(licensePlate);
      return (Set)trunkViewers.get(licensePlate);
   }

   private static void setTrunkViewers(String licensePlate) {
      if (!trunkViewers.containsKey(licensePlate)) {
         trunkViewers.put(licensePlate, new HashSet());
      }

   }

   public static void markVehicleAsDestroyed(String licensePlate) {
      destroyedVehicles.put(licensePlate, true);
   }

   public static void markVehicleAsRepaired(String licensePlate) {
      destroyedVehicles.remove(licensePlate);
   }

   public static boolean isVehicleDestroyed(String licensePlate) {
      return (Boolean)destroyedVehicles.getOrDefault(licensePlate, false);
   }

   public static enum DataSpeed {
      MAXSPEED,
      ACCELERATION,
      BRAKING,
      MAXSPEEDBACKWARDS,
      FRICTION;

      // $FF: synthetic method
      private static DataSpeed[] $values() {
         return new DataSpeed[]{MAXSPEED, ACCELERATION, BRAKING, MAXSPEEDBACKWARDS, FRICTION};
      }
   }
}
