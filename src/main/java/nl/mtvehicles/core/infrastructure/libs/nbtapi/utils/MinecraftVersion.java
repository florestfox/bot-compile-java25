package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.bukkit.Metrics;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.charts.DrilldownPie;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.charts.SimplePie;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public enum MinecraftVersion {
   UNKNOWN(Integer.MAX_VALUE),
   MC1_7_R4(174),
   MC1_8_R3(183),
   MC1_9_R1(191),
   MC1_9_R2(192),
   MC1_10_R1(1101),
   MC1_11_R1(1111),
   MC1_12_R1(1121),
   MC1_13_R1(1131),
   MC1_13_R2(1132),
   MC1_14_R1(1141),
   MC1_15_R1(1151),
   MC1_16_R1(1161),
   MC1_16_R2(1162),
   MC1_16_R3(1163),
   MC1_17_R1(1171),
   MC1_18_R1(1181, true),
   MC1_18_R2(1182, true),
   MC1_19_R1(1191, true),
   MC1_19_R2(1192, true),
   MC1_19_R3(1193, true),
   MC1_20_R1(1201, true),
   MC1_20_R2(1202, true),
   MC1_20_R3(1203, true),
   MC1_20_R4(1204, true),
   MC1_21_R1(1211, true),
   MC1_21_R2(1212, true),
   MC1_21_R3(1213, true),
   MC1_21_R4(1214, true),
   MC1_21_R5(1215, true),
   MC1_21_R6(1216, true);

   private static MinecraftVersion version;
   private static Boolean hasGsonSupport;
   private static Boolean isForgePresent;
   private static Boolean isNeoForgePresent;
   private static Boolean isFabricPresent;
   private static Boolean isFoliaPresent;
   private static boolean bStatsDisabled = false;
   private static boolean disablePackageWarning = false;
   private static boolean updateCheckDisabled = true;
   private static Logger logger = Logger.getLogger("NBTAPI");
   protected static final String VERSION = "2.15.3-SNAPSHOT";
   private final int versionId;
   private final boolean mojangMapping;
   private static final Map<String, MinecraftVersion> VERSION_TO_REVISION = new HashMap<String, MinecraftVersion>() {
      {
         this.put("1.20", MinecraftVersion.MC1_20_R1);
         this.put("1.20.1", MinecraftVersion.MC1_20_R1);
         this.put("1.20.2", MinecraftVersion.MC1_20_R2);
         this.put("1.20.3", MinecraftVersion.MC1_20_R3);
         this.put("1.20.4", MinecraftVersion.MC1_20_R3);
         this.put("1.20.5", MinecraftVersion.MC1_20_R4);
         this.put("1.20.6", MinecraftVersion.MC1_20_R4);
         this.put("1.21", MinecraftVersion.MC1_21_R1);
         this.put("1.21.1", MinecraftVersion.MC1_21_R1);
         this.put("1.21.2", MinecraftVersion.MC1_21_R2);
         this.put("1.21.3", MinecraftVersion.MC1_21_R2);
         this.put("1.21.4", MinecraftVersion.MC1_21_R3);
         this.put("1.21.5", MinecraftVersion.MC1_21_R4);
         this.put("1.21.6", MinecraftVersion.MC1_21_R5);
         this.put("1.21.7", MinecraftVersion.MC1_21_R5);
         this.put("1.21.8", MinecraftVersion.MC1_21_R5);
         this.put("1.21.9", MinecraftVersion.MC1_21_R6);
         this.put("1.21.10", MinecraftVersion.MC1_21_R6);
      }
   };

   private MinecraftVersion(int versionId) {
      this(versionId, false);
   }

   private MinecraftVersion(int versionId, boolean mojangMapping) {
      this.versionId = versionId;
      this.mojangMapping = mojangMapping;
   }

   public int getVersionId() {
      return this.versionId;
   }

   public boolean isMojangMapping() {
      return this.mojangMapping;
   }

   public String getPackageName() {
      if (this == UNKNOWN) {
         try {
            return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
         } catch (Exception var2) {
         }
      }

      return this.name().replace("MC", "v");
   }

   public static boolean isAtLeastVersion(MinecraftVersion version) {
      return getVersion().getVersionId() >= version.getVersionId();
   }

   public static boolean isNewerThan(MinecraftVersion version) {
      return getVersion().getVersionId() > version.getVersionId();
   }

   public static MinecraftVersion getVersion() {
      if (version != null) {
         return version;
      } else {
         try {
            String ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            logger.info("[NBTAPI] Found Minecraft: " + ver + "! Trying to find NMS support");
            version = valueOf(ver.replace("v", "MC"));
         } catch (Exception var1) {
            logger.info("[NBTAPI] Found Minecraft: " + Bukkit.getServer().getBukkitVersion().split("-")[0] + "! Trying to find NMS support");
            version = (MinecraftVersion)VERSION_TO_REVISION.getOrDefault(Bukkit.getServer().getBukkitVersion().split("-")[0], UNKNOWN);
         }

         if (version != UNKNOWN) {
            logger.info("[NBTAPI] NMS support '" + version.name() + "' loaded!");
         } else {
            logger.warning("[NBTAPI] This Server-Version(" + Bukkit.getServer().getBukkitVersion() + ") is not supported by this NBT-API Version(" + "2.15.3-SNAPSHOT" + ") located in " + VersionChecker.getPlugin() + ". The NBT-API will try to work as good as it can! Some functions may not work!");
         }

         init();
         return version;
      }
   }

   public static String getNBTAPIVersion() {
      return "2.15.3-SNAPSHOT";
   }

   private static void init() {
      String defaultPackage = new String(new byte[]{100, 101, 46, 116, 114, 55, 122, 119, 46, 99, 104, 97, 110, 103, 101, 109, 101, 46, 110, 98, 116, 97, 112, 105, 46, 117, 116, 105, 108, 115});
      String reservedPackage = new String(new byte[]{100, 101, 46, 116, 114, 55, 122, 119, 46, 110, 98, 116, 97, 112, 105, 46, 117, 116, 105, 108, 115});

      try {
         if (hasGsonSupport() && !bStatsDisabled) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(VersionChecker.getPlugin());
            if (plugin != null && plugin instanceof JavaPlugin) {
               getLogger().info("[NBTAPI] Using the plugin '" + plugin.getName() + "' to create a bStats instance!");
               Metrics metrics = new Metrics((JavaPlugin)plugin, 1058);
               metrics.addCustomChart(new SimplePie("nbtapi_version", () -> "2.15.3-SNAPSHOT"));
               metrics.addCustomChart(new DrilldownPie("nms_version", () -> {
                  Map<String, Map<String, Integer>> map = new HashMap();
                  Map<String, Integer> entry = new HashMap();
                  entry.put(Bukkit.getName(), 1);
                  map.put(getVersion().name(), entry);
                  return map;
               }));
               metrics.addCustomChart(new SimplePie("shaded", () -> Boolean.toString(!"NBTAPI".equals(VersionChecker.getPlugin()))));
               metrics.addCustomChart(new SimplePie("server_software", () -> Bukkit.getName()));
               metrics.addCustomChart(new SimplePie("parent_plugin", () -> VersionChecker.getPluginforBStats()));
               metrics.addCustomChart(new SimplePie("parent_plugin_type", () -> VersionChecker.getPluginType()));
               metrics.addCustomChart(new SimplePie("special_environment", () -> {
                  if (isFoliaPresent()) {
                     return "Folia";
                  } else if (isForgePresent()) {
                     return "Forge";
                  } else if (isFabricPresent()) {
                     return "Fabric";
                  } else {
                     return isNeoForgePresent() ? "NeoForge" : "None";
                  }
               }));
               metrics.addCustomChart(new SimplePie("bindings_check", () -> {
                  boolean failedBinding = false;

                  for(ClassWrapper c : ClassWrapper.values()) {
                     if (c.isEnabled() && c.getClazz() == null) {
                        failedBinding = true;
                     }
                  }

                  for(ReflectionMethod method : ReflectionMethod.values()) {
                     if (method.isCompatible() && !method.isLoaded()) {
                        failedBinding = true;
                     }
                  }

                  return failedBinding ? "Failed" : "Pass";
               }));
            } else if (plugin == null) {
               getLogger().info("[NBTAPI] Unable to create a bStats instance!!");
            }
         }
      } catch (Exception ex) {
         logger.log(Level.WARNING, "[NBTAPI] Error enabling Metrics!", ex);
      }

      if (hasGsonSupport() && !updateCheckDisabled) {
         (new Thread(() -> {
            try {
               VersionChecker.checkForUpdates();
            } catch (Exception ex) {
               logger.log(Level.WARNING, "[NBTAPI] Error while checking for updates! Error: " + ex.getMessage());
            }

         })).start();
      }

      if (!disablePackageWarning && MinecraftVersion.class.getPackage().getName().equals(defaultPackage)) {
         logger.warning("#########################################- NBTAPI -#########################################");
         logger.warning("The NBT-API package has not been moved! This *will* cause problems with other plugins containing");
         logger.warning("a different version of the api! Please read the guide on the plugin page on how to get the");
         logger.warning("Maven Shade plugin to relocate the api to your personal location! If you are not the developer,");
         logger.warning("please check your plugins and contact their developer, so they can fix this issue.");
         logger.warning("#########################################- NBTAPI -#########################################");
      }

      if (!disablePackageWarning && !"NBTAPI".equals(VersionChecker.getPlugin())) {
         if (!"de.tr7zw.nbtapi.utils".equals(reservedPackage)) {
            logger.warning("#########################################- NBTAPI -#########################################");
            logger.warning("The NBT-API inside " + VersionChecker.getPlugin() + " is the plugin version, not the API!");
            logger.warning("The plugin itself should never be shaded! Remove the `-plugin` from the dependency and fix your shading setup.");
            logger.warning("For more info check: https://github.com/tr7zw/Item-NBT-API/wiki/Using-Maven#option-2-shading-the-nbt-api-into-your-plugin");
            logger.warning("#########################################- NBTAPI -#########################################");
            return;
         }

         if (MinecraftVersion.class.getPackage().getName().equals("de.tr7zw.nbtapi.utils")) {
            logger.warning("#########################################- NBTAPI -#########################################");
            logger.warning("The NBT-API inside " + VersionChecker.getPlugin() + " is located at 'de.tr7zw.nbtapi.utils'!");
            logger.warning("This package name is reserved for the official NBTAPI plugin, and not intended to be used for shading!");
            logger.warning("Please change the relocate to something else. For example: com.example.util.nbtapi");
            logger.warning("#########################################- NBTAPI -#########################################");
         }
      }

   }

   public static boolean hasGsonSupport() {
      if (hasGsonSupport != null) {
         return hasGsonSupport;
      } else {
         try {
            Class.forName("com.google.gson.Gson");
            hasGsonSupport = true;
         } catch (Exception var1) {
            logger.info("[NBTAPI] Gson not found! This will not allow the usage of some methods!");
            hasGsonSupport = false;
         }

         return hasGsonSupport;
      }
   }

   public static boolean isFabricPresent() {
      if (isFabricPresent != null) {
         return isFabricPresent;
      } else {
         try {
            logger.info("[NBTAPI] Found Fabric: " + Class.forName("net.fabricmc.api.ModInitializer"));
            isFabricPresent = true;
         } catch (Exception var1) {
            isFabricPresent = false;
         }

         return isFabricPresent;
      }
   }

   public static boolean isForgePresent() {
      if (isForgePresent != null) {
         return isForgePresent;
      } else {
         try {
            logger.info("[NBTAPI] Found Forge: " + (getVersion() == MC1_7_R4 ? Class.forName("cpw.mods.fml.common.Loader") : Class.forName("net.minecraftforge.fml.common.Loader")));
            isForgePresent = true;
         } catch (Exception var1) {
            isForgePresent = false;
         }

         return isForgePresent;
      }
   }

   public static boolean isNeoForgePresent() {
      if (isNeoForgePresent != null) {
         return isNeoForgePresent;
      } else {
         try {
            logger.info("[NBTAPI] Found NeoForge: " + Class.forName("net.neoforged.neoforge.common.NeoForge"));
            isNeoForgePresent = true;
         } catch (Exception var1) {
            isNeoForgePresent = false;
         }

         return isNeoForgePresent;
      }
   }

   public static boolean isFoliaPresent() {
      if (isFoliaPresent != null) {
         return isFoliaPresent;
      } else {
         try {
            logger.info("[NBTAPI] Found Folia: " + Class.forName("io.papermc.paper.threadedregions.RegionizedServer"));
            isFoliaPresent = true;
         } catch (Exception var1) {
            isFoliaPresent = false;
         }

         return isFoliaPresent;
      }
   }

   public static void disableBStats() {
      bStatsDisabled = true;
   }

   public static void disableUpdateCheck() {
      updateCheckDisabled = true;
   }

   public static void enableUpdateCheck() {
      updateCheckDisabled = false;
   }

   public static void disablePackageWarning() {
      disablePackageWarning = true;
   }

   public static Logger getLogger() {
      return logger;
   }

   public static void replaceLogger(Logger logger) {
      if (logger == null) {
         throw new NullPointerException("Logger can not be null!");
      } else {
         MinecraftVersion.logger = logger;
      }
   }

   // $FF: synthetic method
   private static MinecraftVersion[] $values() {
      return new MinecraftVersion[]{UNKNOWN, MC1_7_R4, MC1_8_R3, MC1_9_R1, MC1_9_R2, MC1_10_R1, MC1_11_R1, MC1_12_R1, MC1_13_R1, MC1_13_R2, MC1_14_R1, MC1_15_R1, MC1_16_R1, MC1_16_R2, MC1_16_R3, MC1_17_R1, MC1_18_R1, MC1_18_R2, MC1_19_R1, MC1_19_R2, MC1_19_R3, MC1_20_R1, MC1_20_R2, MC1_20_R3, MC1_20_R4, MC1_21_R1, MC1_21_R2, MC1_21_R3, MC1_21_R4, MC1_21_R5, MC1_21_R6};
   }
}
