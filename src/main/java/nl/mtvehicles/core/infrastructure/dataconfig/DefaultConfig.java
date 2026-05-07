package nl.mtvehicles.core.infrastructure.dataconfig;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.enums.DriveUp;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import nl.mtvehicles.core.infrastructure.enums.WGFlag;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DefaultConfig extends MTVConfig {
   private final GasStationConfig gasStations = new GasStationConfig();

   public DefaultConfig() {
      super(ConfigType.DEFAULT);
   }

   /** @deprecated */
   @Deprecated
   public String getMessage(String key) {
      return TextUtils.colorize(this.getConfiguration().getString(key));
   }

   public Object get(Option configOption) {
      return this.getConfiguration().get(configOption.getPath());
   }

   public boolean hasOldVersionChecking() {
      return this.getConfiguration().get("Config-Versie") != null;
   }

   public DriveUp driveUpSlabs() {
      DriveUp returns = DriveUp.BOTH;

      try {
         switch ((String)Objects.requireNonNull(this.get(DefaultConfig.Option.DRIVE_UP).toString().toLowerCase())) {
            case "blocks":
            case "block":
               returns = DriveUp.BLOCKS;
               break;
            case "slabs":
            case "slab":
               returns = DriveUp.SLABS;
         }
      } catch (NullPointerException var4) {
      }

      return returns;
   }

   public boolean isHoneySlowdownEnabled() {
      return (Boolean)this.get(DefaultConfig.Option.SLOW_ON_HONEY);
   }

   public boolean isIceSlippery() {
      return (Boolean)this.get(DefaultConfig.Option.SLIPPERY_ICE);
   }

   public boolean canUseJerryCan(Player player, Location loc) {
      if (!this.gasStations.areGasStationsEnabled()) {
         return true;
      } else if (DependencyModule.worldGuard.isInRegionWithFlag(player, loc, WGFlag.GAS_STATION, false)) {
         return false;
      } else {
         return this.gasStations.canUseJerryCanOutsideOfGasStation() ? true : DependencyModule.worldGuard.isInsideGasStation(player, loc);
      }
   }

   public boolean canUseJerryCan(Player player) {
      return this.canUseJerryCan(player, player.getLocation());
   }

   public boolean canFillJerryCans(Player p, Location loc) {
      if (!this.gasStations.areGasStationsEnabled()) {
         return false;
      } else if (!this.gasStations.isFillJerryCansEnabled()) {
         return false;
      } else if (!DependencyModule.worldGuard.isInsideGasStation(p, loc)) {
         return false;
      } else if (!this.gasStations.hasFillJerryCansPermission(p)) {
         p.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.NO_PERMISSION)));
         return false;
      } else {
         return true;
      }
   }

   public boolean jerryCanPlaySound() {
      return (Boolean)this.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_PLAY_SOUND);
   }

   public boolean isFillJerryCansLeverEnabled() {
      return (Boolean)this.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_LEVER);
   }

   public boolean isFillJerryCansTripwireHookEnabled() {
      return (Boolean)this.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_TRIPWIRE_HOOK);
   }

   public boolean isFillJerryCanPriceEnabled() {
      if (!this.gasStations.areGasStationsEnabled()) {
         return false;
      } else if (!this.gasStations.isFillJerryCansEnabled()) {
         return false;
      } else if (!DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
         return false;
      } else {
         return !DependencyModule.vault.isEconomySetUp() ? false : (Boolean)this.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_ENABLED);
      }
   }

   public double getFillJerryCanPrice() {
      return (Double)this.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE) <= (double)0.0F ? (double)30.0F : (Double)this.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE);
   }

   public boolean isWorldDisabled(String worldName) {
      return this.getDisabledWorlds().isEmpty() ? false : this.getDisabledWorlds().contains(worldName);
   }

   private List<String> getDisabledWorlds() {
      return (List)this.get(DefaultConfig.Option.DISABLED_WORLDS);
   }

   public boolean isBlockWhitelistEnabled() {
      return (Boolean)this.get(DefaultConfig.Option.BLOCK_WHITELIST_ENABLED);
   }

   public List<Material> blockWhiteList() {
      return (List)((List)this.get(DefaultConfig.Option.BLOCK_WHITELIST_LIST)).stream().map(Material::getMaterial).collect(Collectors.toList());
   }

   private RegionAction.ListType getRegionActionListType(RegionAction action) {
      String configOption = "disabled";
      switch (action) {
         case PLACE:
            configOption = this.get(DefaultConfig.Option.REGION_ACTIONS_PLACE).toString().toLowerCase(Locale.ROOT);
            break;
         case PICKUP:
            configOption = this.get(DefaultConfig.Option.REGION_ACTIONS_PICKUP).toString().toLowerCase(Locale.ROOT);
            break;
         case ENTER:
            configOption = this.get(DefaultConfig.Option.REGION_ACTIONS_ENTER).toString().toLowerCase(Locale.ROOT);
            break;
         case RIDE:
            configOption = this.get(DefaultConfig.Option.REGION_ACTIONS_RIDE).toString().toLowerCase(Locale.ROOT);
      }

      if (configOption.equalsIgnoreCase("whitelist")) {
         return RegionAction.ListType.WHITELIST;
      } else {
         return configOption.equalsIgnoreCase("blacklist") ? RegionAction.ListType.BLACKLIST : RegionAction.ListType.DISABLED;
      }
   }

   public boolean canProceedWithAction(RegionAction action, VehicleType vehicleType, Location loc, Player p) {
      if (this.isWorldDisabled(loc.getWorld().getName())) {
         return false;
      } else if (!DependencyModule.isDependencyEnabled(SoftDependency.WORLD_GUARD)) {
         return true;
      } else if (vehicleType.isUsageDisabled(p, loc)) {
         return false;
      } else {
         boolean returns = true;
         RegionAction.ListType listType = this.getRegionActionListType(action);
         if (!listType.isEnabled()) {
            return true;
         } else {
            boolean isWhitelist = listType.isWhitelist();
            boolean isBlacklist = listType.isBlacklist();
            switch (action) {
               case PLACE:
                  if (isWhitelist) {
                     returns = DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.PLACE, true);
                  } else if (isBlacklist) {
                     returns = !DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.PLACE, false);
                  }
                  break;
               case PICKUP:
                  if (isWhitelist) {
                     returns = DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.PICKUP, true);
                  } else if (isBlacklist) {
                     returns = !DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.PICKUP, false);
                  }
                  break;
               case ENTER:
                  if (isWhitelist) {
                     returns = DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.ENTER, true);
                  } else if (isBlacklist) {
                     returns = !DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.ENTER, false);
                  }
                  break;
               case RIDE:
                  if (isWhitelist) {
                     returns = DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.RIDE, true);
                  } else if (isBlacklist) {
                     returns = !DependencyModule.worldGuard.isInRegionWithFlag(p, loc, WGFlag.RIDE, false);
                  }
            }

            return returns;
         }
      }
   }

   public boolean usePlayerFacingDriving() {
      return (Boolean)this.get(DefaultConfig.Option.USE_PLAYER_FACING);
   }

   private class GasStationConfig {
      private GasStationConfig() {
      }

      private boolean areGasStationsEnabled() {
         return !DependencyModule.isDependencyEnabled(SoftDependency.WORLD_GUARD) ? false : (Boolean)DefaultConfig.this.get(DefaultConfig.Option.GAS_STATIONS_ENABLED);
      }

      private boolean canUseJerryCanOutsideOfGasStation() {
         return (Boolean)DefaultConfig.this.get(DefaultConfig.Option.GAS_STATIONS_CAN_USE_JERRYCAN_OUTSIDE_OF_GAS_STATION);
      }

      private boolean isFillJerryCansEnabled() {
         return (Boolean)DefaultConfig.this.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_ENABLED);
      }

      private boolean hasFillJerryCansPermission(Player p) {
         return !(Boolean)DefaultConfig.this.get(DefaultConfig.Option.GAS_STATIONS_FILL_JERRYCANS_NEED_PERMISSION) ? true : p.hasPermission("mtvehicles.filljerrycans");
      }
   }

   public static enum Option {
      AUTO_UPDATE("autoUpdate", true),
      VEHICLE_MENU_SIZE("vehicleMenuSize", 3),
      HELICOPTER_BLADES_ALWAYS_ON("helicopterBladesAlwaysOn", true),
      DISABLE_PICKUP_FROM_WATER("disablePickupFromWater", false),
      TRUNK_ENABLED("trunkEnabled", true),
      PUT_ONESELF_AS_OWNER("putOneselfAsOwner", false),
      MAX_FLYING_HEIGHT("maxFlyingHeight", 150),
      TAKE_OFF_SPEED("takeOffSpeed", 0.4),
      AIRPLANE_TNT("airplaneTNT", false),
      AIRPLANE_COOLDOWN("airplaneTNTCooldown", 3),
      CAR_PICKUP("carPickup", false),
      FUEL_ENABLED("fuelEnabled", true),
      FUEL_MULTIPLIER("fuelMultiplier", 1),
      JERRYCANS("jerrycans", new ArrayList(Arrays.asList(25, 50, 75))),
      DAMAGE_ENABLED("damageEnabled", false),
      DAMAGE_MULTIPLIER("damageMultiplier", (double)0.5F),
      EXPLODING_VEHICLE("explodingVehicle", false),
      DESTRUCTIBLE_VEHICLE("destructibleVehicle", false),
      HORN_COOLDOWN("hornCooldown", 5),
      HORN_TYPE("hornType", "minetopiaclassic.horn1"),
      HEADLIGHTS_ENABLED("headlightsEnabled", false),
      TANK_TNT("tankTNT", false),
      TANK_COOLDOWN("tankCooldown", 10),
      DRIVE_UP("driveUp", "both"),
      EXTREME_HELICOPTER_FALL("extremeHelicopterFall", false),
      HELICOPTER_FALL_DAMAGE("helicopterFallDamage", (double)40.0F),
      DRIVE_ON_CARPETS("driveOnCarpets", true),
      SLOW_ON_HONEY("slowDownOnHoney", false),
      SLIPPERY_ICE("slipperyIce", false),
      BLOCK_WHITELIST_ENABLED("blockWhitelist.enabled", false),
      BLOCK_WHITELIST_LIST("blockWhitelist.list", (new ArrayList()).add("GRAY_CONCRETE")),
      DISABLED_WORLDS("disabledWorlds", new ArrayList()),
      GAS_STATIONS_ENABLED("gasStations.enabled", false),
      GAS_STATIONS_CAN_USE_JERRYCAN_OUTSIDE_OF_GAS_STATION("gasStations.canUseJerryCanOutsideOfGasStation", true),
      GAS_STATIONS_FILL_JERRYCANS_ENABLED("gasStations.fillJerryCans.enabled", true),
      GAS_STATIONS_FILL_JERRYCANS_NEED_PERMISSION("gasStations.fillJerryCans.needPermission", false),
      GAS_STATIONS_FILL_JERRYCANS_PLAY_SOUND("gasStations.fillJerryCans.playSound", true),
      GAS_STATIONS_FILL_JERRYCANS_LEVER("gasStations.fillJerryCans.lever", true),
      GAS_STATIONS_FILL_JERRYCANS_TRIPWIRE_HOOK("gasStations.fillJerryCans.tripwireHook", false),
      GAS_STATIONS_FILL_JERRYCANS_PRICE_ENABLED("gasStations.fillJerryCans.price.enabled", true),
      GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE("gasStations.fillJerryCans.price.pricePerLitre", (double)30.0F),
      REGION_ACTIONS_PLACE("regionActions.place", "disabled"),
      REGION_ACTIONS_ENTER("regionActions.enter", "disabled"),
      REGION_ACTIONS_PICKUP("regionActions.pickup", "disabled"),
      REGION_ACTIONS_RIDE("regionActions.ride", "disabled"),
      USE_PLAYER_FACING("usePlayerFacing", false);

      private final String path;
      private final Object defaultValue;

      private Option(String path, Object defaultValue) {
         this.path = path;
         this.defaultValue = defaultValue;
      }

      public Object getDefaultValue() {
         return this.defaultValue;
      }

      public String getPath() {
         return this.path;
      }

      public Type getValueType() {
         return this.getDefaultValue().getClass();
      }

      // $FF: synthetic method
      private static Option[] $values() {
         return new Option[]{AUTO_UPDATE, VEHICLE_MENU_SIZE, HELICOPTER_BLADES_ALWAYS_ON, DISABLE_PICKUP_FROM_WATER, TRUNK_ENABLED, PUT_ONESELF_AS_OWNER, MAX_FLYING_HEIGHT, TAKE_OFF_SPEED, AIRPLANE_TNT, AIRPLANE_COOLDOWN, CAR_PICKUP, FUEL_ENABLED, FUEL_MULTIPLIER, JERRYCANS, DAMAGE_ENABLED, DAMAGE_MULTIPLIER, EXPLODING_VEHICLE, DESTRUCTIBLE_VEHICLE, HORN_COOLDOWN, HORN_TYPE, HEADLIGHTS_ENABLED, TANK_TNT, TANK_COOLDOWN, DRIVE_UP, EXTREME_HELICOPTER_FALL, HELICOPTER_FALL_DAMAGE, DRIVE_ON_CARPETS, SLOW_ON_HONEY, SLIPPERY_ICE, BLOCK_WHITELIST_ENABLED, BLOCK_WHITELIST_LIST, DISABLED_WORLDS, GAS_STATIONS_ENABLED, GAS_STATIONS_CAN_USE_JERRYCAN_OUTSIDE_OF_GAS_STATION, GAS_STATIONS_FILL_JERRYCANS_ENABLED, GAS_STATIONS_FILL_JERRYCANS_NEED_PERMISSION, GAS_STATIONS_FILL_JERRYCANS_PLAY_SOUND, GAS_STATIONS_FILL_JERRYCANS_LEVER, GAS_STATIONS_FILL_JERRYCANS_TRIPWIRE_HOOK, GAS_STATIONS_FILL_JERRYCANS_PRICE_ENABLED, GAS_STATIONS_FILL_JERRYCANS_PRICE_PER_LITRE, REGION_ACTIONS_PLACE, REGION_ACTIONS_ENTER, REGION_ACTIONS_PICKUP, REGION_ACTIONS_RIDE, USE_PLAYER_FACING};
      }
   }
}
