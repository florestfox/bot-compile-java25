package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.PluginUpdater;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import org.bukkit.Bukkit;

public class VehicleVersion extends MTVSubCommand {
   public VehicleVersion() {
      this.setPlayerCommand(false);
   }

   public boolean execute() {
      if (!this.checkPermission("mtvehicles.admin")) {
         return true;
      } else {
         String pluginVersion = VersionModule.getPluginVersion();
         String isLatest = PluginUpdater.isLatestVersion() && !VersionModule.isDevRelease ? " (latest)" : "";
         String serverVersion = Bukkit.getVersion();
         this.sender.sendMessage(String.format("§2Running §aMTVehicles v%s§2%s.", pluginVersion, isLatest));
         this.sender.sendMessage(String.format("§2Your server is running §a%s§2.", serverVersion));
         if (!DependencyModule.loadedDependencies.isEmpty()) {
            String dependencies = "";
            int numberOfDependencies = 0;

            for(SoftDependency dependency : DependencyModule.loadedDependencies) {
               if (numberOfDependencies == 0) {
                  dependencies = dependencies + dependency.getName();
               } else {
                  dependencies = dependencies + ", " + dependency.getName();
               }

               ++numberOfDependencies;
            }

            if (DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
               if (!DependencyModule.vault.isEconomySetUp()) {
                  dependencies = dependencies.replace("Vault", "§a§mVault§a");
               } else {
                  dependencies = dependencies.replace("Vault", "Vault (" + DependencyModule.vault.getEconomyName() + ")");
               }
            }

            this.sender.sendMessage(String.format("§2Loaded dependencies (%s§2): §a%s§2.", numberOfDependencies, dependencies));
         } else {
            this.sender.sendMessage(String.format("§2There are no loaded dependencies."));
         }

         if (VersionModule.isPreRelease) {
            this.sender.sendMessage("§e-----");
            if (VersionModule.isDevRelease) {
               this.sender.sendMessage(TextUtils.colorize("&cWarning: You're using a dev-build. Auto-updater may be confused."));
            }

            this.sendMessage(Message.USING_PRE_RELEASE);
         }

         return true;
      }
   }
}
