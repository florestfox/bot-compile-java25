package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.events.JerryCanClickEvent;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class JerryCanClickListener extends MTVListener {
   @EventHandler
   public void onJerryCanClick(PlayerInteractEvent event) {
      this.event = event;
      if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
         ItemStack item = event.getItem();
         if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
            NBTItem nbt;
            try {
               nbt = new NBTItem(item);
               if (!nbt.hasKey("mtvehicles.benzinesize")) {
                  return;
               }
            } catch (Exception var7) {
               return;
            }

            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && event.getHand() == EquipmentSlot.HAND) {
               this.player = event.getPlayer();
               int currentFuel = Integer.parseInt(nbt.getString("mtvehicles.benzineval"));
               int maxFuel = Integer.parseInt(nbt.getString("mtvehicles.benzinesize"));
               this.setAPI(new JerryCanClickEvent(currentFuel, maxFuel));
               this.callAPI();
               if (!this.isCancelled()) {
                  event.setCancelled(true);
                  if (ConfigModule.defaultConfig.canFillJerryCans(this.player, clickedBlock.getLocation())) {
                     if (this.isFuelStation(clickedBlock)) {
                        if (this.player.isSneaking()) {
                           this.fillWholeJerryCan(currentFuel, maxFuel);
                        } else {
                           this.fillJerryCan(currentFuel, maxFuel);
                        }
                     }

                  }
               }
            } else {
               event.getPlayer().sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND)));
            }
         }
      }
   }

   private boolean isFuelStation(Block block) {
      String blockType = block.getType().toString();
      return blockType.contains("LEVER") && ConfigModule.defaultConfig.isFillJerryCansLeverEnabled() || blockType.contains("TRIPWIRE_HOOK") && ConfigModule.defaultConfig.isFillJerryCansTripwireHookEnabled();
   }

   private void fillJerryCan(int currentFuel, int maxFuel) {
      if (currentFuel >= maxFuel) {
         ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.JERRYCAN_FULL);
      } else {
         if (this.canAffordFuel(1)) {
            // [PERBAIKAN BSNL] Mengganti setItemInHand yang usang
            this.player.getInventory().setItemInMainHand(VehicleFuel.jerrycanItem(maxFuel, currentFuel + 1));
            this.playJerryCanSound();
         }
      }
   }

   private void fillWholeJerryCan(int currentFuel, int maxFuel) {
      if (currentFuel >= maxFuel) {
         ConfigModule.messagesConfig.sendMessage(this.player, (Message)Message.JERRYCAN_FULL);
      } else {
         int difference = maxFuel - currentFuel;
         if (this.canAffordFuel(difference)) {
            // [PERBAIKAN BSNL] Mengganti setItemInHand yang usang
            this.player.getInventory().setItemInMainHand(VehicleFuel.jerrycanItem(maxFuel, maxFuel));
            this.playJerryCanSound();
         }
      }
   }

   private boolean canAffordFuel(int litres) {
      if (!ConfigModule.defaultConfig.isFillJerryCanPriceEnabled()) {
         return true;
      } else {
         double price = (double)litres * ConfigModule.defaultConfig.getFillJerryCanPrice();
         return DependencyModule.vault.withdrawMoneyPlayer(this.player, price);
      }
   }

   @VersionSpecific
   private void playJerryCanSound() {
      if (ConfigModule.defaultConfig.jerryCanPlaySound()) {
         // Karena kita bypass versi, sound name ini aman untuk versi baru
         String soundName = "BLOCK_NOTE_BLOCK_PLING"; 

         try {
            this.player.getWorld().playSound(this.player.getLocation(), Sound.valueOf(soundName), 3.0F, 0.5F);
         } catch (IllegalArgumentException e) {
            Main.logSevere("Could not play sound '" + soundName + "'.");
            e.printStackTrace();
         }

      }
   }
}