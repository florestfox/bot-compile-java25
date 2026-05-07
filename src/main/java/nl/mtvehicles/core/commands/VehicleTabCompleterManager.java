package nl.mtvehicles.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.Generated;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.infrastructure.modules.CommandModule;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleTabCompleterManager implements TabCompleter {
   private static HashMap<String, String> vehicleList = new HashMap();

   public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
      if (strings.length == 1) {
         return this.getApplicableTabCompleters(strings[0], CommandModule.subcommands.keySet());
      } else {
         if (strings.length > 1) {
            String subCommand = strings[0].toLowerCase();
            if (subCommand.equals("edit")) {
               if (strings.length == 2) {
                  List<String> playerNames = new ArrayList();

                  for(Player player : Bukkit.getOnlinePlayers()) {
                     playerNames.add(player.getName());
                  }

                  List<String> suggestions = new ArrayList(playerNames);
                  suggestions.addAll(VehicleEdit.getEditCommands());
                  return this.getApplicableTabCompleters(strings[1], suggestions);
               }

               if (strings.length == 3 && Bukkit.getPlayer(strings[1]) != null) {
                  return this.getApplicableTabCompleters(strings[2], VehicleEdit.getEditCommands());
               }
            } else if (subCommand.equals("give")) {
               if (strings.length == 2) {
                  List<String> playerNames = new ArrayList();

                  for(Player player : Bukkit.getOnlinePlayers()) {
                     playerNames.add(player.getName());
                  }

                  return this.getApplicableTabCompleters(strings[1], playerNames);
               }

               if (strings.length == 3) {
                  if (Bukkit.getPlayer(strings[1]) != null) {
                     return this.getApplicableTabCompleters(strings[2], vehicleList.keySet());
                  }
               } else if (strings.length == 4 && commandSender.hasPermission("mtvehicles.givevoucher")) {
                  List<String> completions = Arrays.asList("--voucher:true", "--voucher:false");
                  return this.getApplicableTabCompleters(strings[3], completions);
               }
            } else if (subCommand.equals("buy")) {
               if (strings.length == 2) {
                  return this.getApplicableTabCompleters(strings[1], vehicleList.keySet());
               }

               if (strings.length == 3 && commandSender.hasPermission("mtvehicles.buyvoucher")) {
                  List<String> completions = Arrays.asList("--voucher:true", "--voucher:false");
                  return this.getApplicableTabCompleters(strings[2], completions);
               }
            }
         }

         return null;
      }
   }

   private List<String> getApplicableTabCompleters(String arg, Collection<String> completions) {
      return (List)StringUtil.copyPartialMatches(arg, completions, new ArrayList(completions.size()));
   }

   public static void loadVehicleList() {
      if (!vehicleList.isEmpty()) {
         vehicleList.clear();
      }

      for(Map<?, ?> configVehicle : ConfigModule.vehiclesConfig.getVehicles()) {
         for(Map<?, ?> skin : (List)configVehicle.get("cars")) {
            String skinName = ((String)skin.get("name")).replace(" ", "_").toUpperCase(Locale.ROOT);
            String skinUuid = (String)skin.get("uuid");
            vehicleList.put(skinName, skinUuid);
         }
      }

   }

   @Generated
   public static HashMap<String, String> getVehicleList() {
      return vehicleList;
   }
}
