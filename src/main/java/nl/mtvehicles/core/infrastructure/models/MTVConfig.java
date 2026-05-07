package nl.mtvehicles.core.infrastructure.models;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class MTVConfig {
   protected final ConfigType configType;
   protected FileConfiguration config;
   private File configFile = null;
   private String fileName;

   public MTVConfig(ConfigType configType) {
      this.configType = configType;
      if (!configType.isMessages()) {
         this.fileName = configType.getFileName();
      }

   }

   public void reload() {
      if (this.configFile == null) {
         this.setConfigFile(new File(Main.instance.getDataFolder(), this.fileName));
      }

      if (!this.configFile.exists()) {
         this.saveDefaultConfig();
      }

      this.config = YamlConfiguration.loadConfiguration(this.configFile);
      Reader defConfigStream = new InputStreamReader((InputStream)Objects.requireNonNull(Main.instance.getResource(this.fileName)), StandardCharsets.UTF_8);
      YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
      this.config.setDefaults(defConfig);
   }

   /** @deprecated */
   @Deprecated
   public FileConfiguration getConfig() {
      if (this.config == null) {
         this.reload();
      }

      return this.config;
   }

   protected FileConfiguration getConfiguration() {
      if (this.config == null) {
         this.reload();
      }

      return this.config;
   }

   public boolean save() {
      if (this.config != null && this.configFile != null) {
         try {
            this.getConfiguration().save(this.configFile);
         } catch (IOException ex) {
            Main.instance.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, ex);
         }

         this.reload();
         return true;
      } else {
         return false;
      }
   }

   public void saveDefaultConfig() {
      if (this.configFile == null) {
         this.configFile = new File(Main.instance.getDataFolder(), this.fileName);
      }

      if (!this.configFile.exists()) {
         Main.instance.saveResource(this.fileName, false);
      }

   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   public void setConfigFile(File configFile) {
      this.configFile = configFile;
   }
}
