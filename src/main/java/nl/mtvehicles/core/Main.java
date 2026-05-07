package nl.mtvehicles.core;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dependencies.SkriptUtils;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.MinecraftVersion;
import nl.mtvehicles.core.infrastructure.modules.CommandModule;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.ListenersModule;
import nl.mtvehicles.core.infrastructure.modules.LoopModule;
import nl.mtvehicles.core.infrastructure.modules.MetricsModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.PluginUpdater;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
   public static Main instance;
   public static final String configVersion = "2.5.6-b";
   public static final String messagesVersion = "2.5.8-dev7";

   @Override
   public void onEnable() {
      instance = this;
      
      // Kita tetap menjalankan inisialisasi VersionModule, 
      // tetapi kita MENGHAPUS blokade (if isSupportedVersion) agar plugin memaksa jalan di MC terbaru.
      new VersionModule(); 
      
      logInfo("Plugin has been loaded (Forced for new MC Versions)!");
      if (VersionModule.isPreRelease) {
         logWarning("Be aware: You are using a pre-release. It might not be stable and it's generally not advised to use it on a production server.");
      }

      logInfo("--------------------------");
      logInfo("Welcome by MTVehicles v" + VersionModule.getPluginVersion() + "!");
      logInfo("Thanks for using our plugin. (Updated by BSNL)");
      logInfo("--------------------------");
      
      this.disableNBTAPIVersionMessages();
      this.loadSkript();
      
      // Memuat semua modul penting kendaraan
      new CommandModule();
      new ListenersModule();
      new MetricsModule();
      new LoopModule();
      new ConfigModule();
      
      // Mengecek pembaruan (jika diaktifkan di config)
      if ((Boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.AUTO_UPDATE)) {
         PluginUpdater.checkNewVersion(this.getServer().getConsoleSender());
      }
   }

   @Override
   public void onLoad() {
      new DependencyModule();
   }

   @Override
   public void onDisable() {
      if (ConfigModule.vehicleDataConfig != null) {
          ConfigModule.vehicleDataConfig.saveToDisk();
      }
      if (DependencyModule.isDependencyEnabled(SoftDependency.PLACEHOLDER_API)) {
         DependencyModule.placeholderAPI.unregisterOnDisable();
      }
   }

   public static String getFileAsString() {
      return String.valueOf(instance.getFile());
   }

   public void registerListener(Listener listener) {
      Bukkit.getPluginManager().registerEvents(listener, this);
   }

   private void disableNBTAPIVersionMessages() {
      MinecraftVersion.disableUpdateCheck();
   }

   private void loadSkript() {
      if (DependencyModule.isDependencyEnabled(SoftDependency.SKRIPT)) {
         try {
            // [PERBAIKAN] Menghapus decompiler artifact (var10000) yang menyebabkan error
            SkriptUtils.load();
         } catch (Exception e) { 
            // [PERBAIKAN] Mengubah var2 menjadi 'e' (Exception)
            Bukkit.getLogger().severe("MTVehicles could not load Skript addon. (Maybe you've just reloaded the plugin with PlugMan?)");
         }
      }
   }

   public static void disablePlugin() {
      logSevere("Disabling the plugin (a critical bug might have occurred)...");
      instance.setEnabled(false);
   }

   public static void logInfo(String text) {
      instance.getLogger().info(text);
   }

   public static void logWarning(String text) {
      instance.getLogger().warning(text);
   }

   public static void logSevere(String text) {
      instance.getLogger().severe(text);
   }

   public static void schedulerRun(Runnable task) {
      Bukkit.getScheduler().runTask(instance, task);
   }

   public static boolean isNotNull(Object... objects) {
      // [PERBAIKAN] Merapikan sintaks perulangan
      for (Object object : objects) {
         if (object == null) {
            return false;
         }
      }
      return true;
   }
}