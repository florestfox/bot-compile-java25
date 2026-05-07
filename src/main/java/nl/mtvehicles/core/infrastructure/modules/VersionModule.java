package nl.mtvehicles.core.infrastructure.modules;

import java.util.logging.Logger;
import lombok.Generated;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.enums.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

public class VersionModule {
   private static VersionModule instance;
   public static String pluginVersionString;
   public static boolean isPreRelease;
   public static boolean isDevRelease;
   private static String serverVersion;
   public static String serverSoftware;
   private Logger logger;

   public VersionModule() {
      this.logger = Main.instance.getLogger();
      PluginDescriptionFile pdf = Main.instance.getDescription();
      pluginVersionString = pdf.getVersion();
      isPreRelease = pluginVersionString.toLowerCase().contains("pre") || pluginVersionString.toLowerCase().contains("rc") || pluginVersionString.toLowerCase().contains("dev");
      isDevRelease = pluginVersionString.toLowerCase().contains("dev");
      serverSoftware = Bukkit.getName();
      if (!serverSoftware.contains("Arclight")) {
         try {
            serverVersion = Bukkit.getServer().getMinecraftVersion();
         } catch (NoSuchMethodError var3) {
            serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
         }
      }
   }

   public static String getPluginVersion() {
      return pluginVersionString;
   }

   @VersionSpecific
   public static ServerVersion getServerVersion() {
      ServerVersion returns = null;
      switch (serverVersion) {
         case "1.12": case "1.12.1": case "1.12.2": case "v1_12_R1": returns = ServerVersion.v1_12; break;
         case "1.13.1": case "1.13.2": case "v1_13_R2": returns = ServerVersion.v1_13; break;
         case "1.15": case "1.15.1": case "1.15.2": case "v1_15_R1": returns = ServerVersion.v1_15; break;
         case "1.16.4": case "1.16.5": case "v1_16_R3": returns = ServerVersion.v1_16; break;
         case "1.17": case "1.17.1": case "v1_17_R1": returns = ServerVersion.v1_17; break;
         case "1.18": case "1.18.1": case "v1_18_R1": returns = ServerVersion.v1_18_R1; break;
         case "1.18.2": case "v1_18_R2": returns = ServerVersion.v1_18_R2; break;
         case "1.19": case "1.19.1": case "1.19.2": case "v1_19_R1": returns = ServerVersion.v1_19_R1; break;
         case "1.19.3": case "v1_19_R2": returns = ServerVersion.v1_19_R2; break;
         case "1.19.4": case "v1_19_R3": returns = ServerVersion.v1_19_R3; break;
         case "1.20": case "1.20.1": case "v1_20_R1": returns = ServerVersion.v1_20_R1; break;
         case "1.20.2": case "1.20.3": case "v1_20_R2": returns = ServerVersion.v1_20_R2; break;
         case "1.20.4": case "1.20.5": case "v1_20_R3": returns = ServerVersion.v1_20_R3; break;
         case "1.20.6": case "v1_20_R4": returns = ServerVersion.v1_20_R4; break;
         case "1.21": case "1.21.1": case "1.21.2": case "v1_21_R1": returns = ServerVersion.v1_21_R1; break;
         case "1.21.3": case "v1_21_R2": returns = ServerVersion.v1_21_R2; break;
         case "1.21.4": case "v1_21_R3": returns = ServerVersion.v1_21_R3; break;
         case "1.21.5": case "v1_21_R4": returns = ServerVersion.v1_21_R4; break;
         case "1.21.6": case "1.21.7": case "1.21.8": case "v1_21_R5": returns = ServerVersion.v1_21_R5; break;
         case "1.21.9": case "1.21.10": case "v1_21_R6": returns = ServerVersion.v1_21_R6; break;
         default: 
            // [PERBAIKAN BSNL] Jika versi tidak dikenali (seperti versi 26), paksakan pakai versi tertinggi yang ada
            returns = ServerVersion.v1_21_R6; 
            break;
      }
      return returns;
   }

   @VersionSpecific
   public boolean isSupportedVersion() {
      // [PERBAIKAN BSNL] Kita buat fungsi ini selalu mengembalikan TRUE agar plugin selalu hidup di versi baru
      return true;
   }

   @Generated
   public static VersionModule getInstance() {
      return instance;
   }

   @Generated
   public static void setInstance(VersionModule instance) {
      VersionModule.instance = instance;
   }
}