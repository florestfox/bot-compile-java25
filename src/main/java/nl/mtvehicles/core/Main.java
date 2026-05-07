package nl.mtvehicles.core;

import nl.mtvehicles.core.infrastructure.modules.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
   public static Main instance;

   @Override
   public void onEnable() {
      instance = this;
      new VersionModule(); 
      getLogger().info("MTVehicles diaktifkan untuk Java 25!");
      
      new CommandModule();
      new ListenersModule();
      new MetricsModule();
      new LoopModule();
      new ConfigModule();
   }

   @Override
   public void onDisable() {
      if (ConfigModule.vehicleDataConfig != null) ConfigModule.vehicleDataConfig.saveToDisk();
   }

   public void registerListener(Listener listener) {
      Bukkit.getPluginManager().registerEvents(listener, this);
   }

   public static void logInfo(String text) { instance.getLogger().info(text); }
   public static void schedulerRun(Runnable task) { Bukkit.getScheduler().runTask(instance, task); }
}