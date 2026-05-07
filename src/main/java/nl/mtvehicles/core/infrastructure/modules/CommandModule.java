package nl.mtvehicles.core.infrastructure.modules;

import java.util.HashMap;
import lombok.Generated;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.commands.VehicleSubCommandManager;
import nl.mtvehicles.core.commands.VehicleTabCompleterManager;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import org.bukkit.command.PluginCommand;

public class CommandModule {
   private static CommandModule instance;
   public static HashMap<String, MTVSubCommand> subcommands = new HashMap();

   public CommandModule() {
      PluginCommand pluginCommand = Main.instance.getCommand("minetopiavehicles");
      if (pluginCommand != null) {
         pluginCommand.setExecutor(new VehicleSubCommandManager());
         pluginCommand.setTabCompleter(new VehicleTabCompleterManager());
      }

      VehicleTabCompleterManager.loadVehicleList();
   }

   @Generated
   public static CommandModule getInstance() {
      return instance;
   }

   @Generated
   public static void setInstance(CommandModule instance) {
      CommandModule.instance = instance;
   }
}
