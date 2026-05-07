package nl.mtvehicles.core.infrastructure.utils;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TextUtils {
   public static String colorize(String text) {
      return ChatColor.translateAlternateColorCodes('&', text);
   }

   /** @deprecated */
   @Deprecated
   public static String licenseReplacer(String license) {
      return license.split("_").length > 1 ? license.split("_")[2] : null;
   }

   public static List<String> list(String... strings) {
      return Arrays.asList(strings);
   }

   public static boolean checkInvFull(Player player) {
      return !Arrays.asList(player.getInventory().getStorageContents()).contains((Object)null);
   }
}
