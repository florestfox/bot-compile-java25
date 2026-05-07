package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils;

import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;

public class CheckUtil {
   private CheckUtil() {
   }

   public static void assertAvailable(MinecraftVersion version) {
      if (!MinecraftVersion.isAtLeastVersion(version)) {
         throw new NbtApiException("This Method is only avaliable for the version " + version.name() + " and above!");
      }
   }
}
