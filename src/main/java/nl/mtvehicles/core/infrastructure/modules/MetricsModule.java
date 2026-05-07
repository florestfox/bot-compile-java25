package nl.mtvehicles.core.infrastructure.modules;

import lombok.Generated;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.utils.Metrics;

public class MetricsModule {
   private static MetricsModule instance;

   public MetricsModule() {
      Metrics metrics = new Metrics(Main.instance, 5932);
      metrics.addCustomChart(new Metrics.SimplePie("used_language", () -> ConfigModule.secretSettings.getMessagesLanguage()));
      metrics.addCustomChart(new Metrics.SimplePie("used_driveUp", () -> {
         String returns;
         switch (ConfigModule.defaultConfig.driveUpSlabs()) {
            case SLABS:
               returns = "slabs";
               break;
            case BLOCKS:
               returns = "blocks";
               break;
            case BOTH:
            default:
               returns = "both";
         }

         return returns;
      }));
   }

   @Generated
   public static MetricsModule getInstance() {
      return instance;
   }

   @Generated
   public static void setInstance(MetricsModule instance) {
      MetricsModule.instance = instance;
   }
}
