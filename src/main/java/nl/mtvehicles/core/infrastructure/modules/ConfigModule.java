package nl.mtvehicles.core.infrastructure.modules;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Generated;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.SecretSettingsConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehiclesConfig;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;

public class ConfigModule {
   private static ConfigModule instance;
   public static List<MTVConfig> configList = new ArrayList();
   public static SecretSettingsConfig secretSettings = new SecretSettingsConfig();
   public static MessagesConfig messagesConfig = new MessagesConfig();
   public static VehicleDataConfig vehicleDataConfig = new VehicleDataConfig();
   public static VehiclesConfig vehiclesConfig = new VehiclesConfig();
   public static DefaultConfig defaultConfig = new DefaultConfig();

   public ConfigModule() {
      SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
      Date date = new Date();
      String configVersion = "2.5.6-b";
      String messagesVersion = "2.5.8-dev7";
      Main.instance.saveResource("credits.txt", true);
      boolean oldConfigVersion = !secretSettings.getConfigVersion().equals(configVersion) || defaultConfig.hasOldVersionChecking();
      boolean oldMessagesVersion = !secretSettings.getMessagesVersion().equals(messagesVersion) || defaultConfig.hasOldVersionChecking();
      if (oldConfigVersion) {
         File dc = new File(Main.instance.getDataFolder(), "config.yml");
         File vc = new File(Main.instance.getDataFolder(), "vehicles.yml");
         File sss = new File(Main.instance.getDataFolder(), "supersecretsettings.yml");
         dc.renameTo(new File(Main.instance.getDataFolder(), "configOld_" + formatter.format(date) + ".yml"));
         vc.renameTo(new File(Main.instance.getDataFolder(), "vehiclesOld_" + formatter.format(date) + ".yml"));
         sss.delete();
         Main.instance.saveDefaultConfig();
      }

      if (oldMessagesVersion) {
         File sss = new File(Main.instance.getDataFolder(), "supersecretsettings.yml");
         sss.delete();
         messagesConfig.saveNewLanguageFiles(formatter.format(date));
      }

      configList.add(secretSettings);
      configList.add(messagesConfig);
      configList.add(vehicleDataConfig);
      configList.add(vehiclesConfig);
      configList.add(defaultConfig);
      reloadConfigs();
   }

   public static void reloadConfigs() {
      configList.forEach(MTVConfig::reload);
      if (!messagesConfig.setLanguageFile(secretSettings.getMessagesLanguage())) {
         Main.instance.getLogger().severe("Messages.yml for your desired language could not be found. Disabling the plugin...");
         Main.disablePlugin();
      }

   }

   @Generated
   public static ConfigModule getInstance() {
      return instance;
   }

   @Generated
   public static void setInstance(ConfigModule instance) {
      ConfigModule.instance = instance;
   }
}
