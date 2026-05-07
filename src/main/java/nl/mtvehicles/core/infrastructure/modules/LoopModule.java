package nl.mtvehicles.core.infrastructure.modules;

import lombok.Generated;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.movement.MovementManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LoopModule {
   private static LoopModule instance;

   public LoopModule() {
      for(Player p : Bukkit.getServer().getOnlinePlayers()) {
         if (p.isInsideVehicle()) {
            p.kickPlayer(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.RELOAD_IN_VEHICLE)));
         }

         MovementManager.MovementSelector(p);
      }

   }

   @Generated
   public static LoopModule getInstance() {
      return instance;
   }

   @Generated
   public static void setInstance(LoopModule instance) {
      LoopModule.instance = instance;
   }
}
