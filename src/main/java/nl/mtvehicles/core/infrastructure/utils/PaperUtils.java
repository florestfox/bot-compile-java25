package nl.mtvehicles.core.infrastructure.utils;

import lombok.Generated;

public final class PaperUtils {
   public static final boolean isRunningPaper = hasClass("com.destroystokyo.paper.PaperConfig") || hasClass("io.papermc.paper.configuration.Configuration");

   private static boolean hasClass(String className) {
      try {
         Class.forName(className);
         return true;
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }

   @Generated
   private PaperUtils() {
   }
}
