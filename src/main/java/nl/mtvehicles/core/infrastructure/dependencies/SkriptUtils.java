package nl.mtvehicles.core.infrastructure.dependencies;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import java.io.IOException;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Bukkit;

public class SkriptUtils {
   private static SkriptAddon addonInstance;

   public static void load() {
      try {
         getAddonInstance().loadClasses("nl.mtvehicles.core.infrastructure.dependencies.skript", new String[]{"types", "effects", "expressions", "events", "conditions"});
      } catch (IOException var1) {
         Bukkit.getLogger().severe("[MTVehicles] An error occurred whilst loading Skript as a soft-dependency. (Make sure you're using the latest version, or try restarting the server.)");
         DependencyModule.loadedDependencies.remove(SoftDependency.SKRIPT);
      }

   }

   public static SkriptAddon getAddonInstance() {
      if (addonInstance == null) {
         addonInstance = Skript.registerAddon(Main.instance);
      }

      return addonInstance;
   }
}
