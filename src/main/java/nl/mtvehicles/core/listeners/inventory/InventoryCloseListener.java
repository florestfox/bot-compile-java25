package nl.mtvehicles.core.listeners.inventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.LanguageUtils;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.listeners.VehicleVoucherListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener extends MTVListener {
   public static HashMap<String, Double> speed = new HashMap();

   @EventHandler
   public void onInventoryClose(InventoryCloseEvent event) {
      this.event = event;
      this.player = (Player)event.getPlayer();
      String stringTitle = event.getView().getTitle();
      InventoryTitle title = InventoryTitle.getByStringTitle(stringTitle);
      if (title != null) {
         this.setAPI(new nl.mtvehicles.core.events.inventory.InventoryCloseEvent(title));
         nl.mtvehicles.core.events.inventory.InventoryCloseEvent api = (nl.mtvehicles.core.events.inventory.InventoryCloseEvent)this.getAPI();
         this.callAPI();
         switch (title) {
            case VEHICLE_TRUNK:
               this.handleVehicleTrunk(event);
               break;
            case CHOOSE_LANGUAGE_MENU:
               this.handleLanguageMenu();
               break;
            case VOUCHER_REDEEM_MENU:
               this.handleVoucherRedeemMenu();
         }

      }
   }

   private void handleVehicleTrunk(InventoryCloseEvent event) {
      String license = (String)VehicleUtils.openedTrunk.remove(this.player);
      VehicleData.trunkViewerRemove(license, this.player);
      List<ItemStack> chest = Arrays.asList(event.getInventory().getContents());
      ConfigModule.vehicleDataConfig.set(license, VehicleDataConfig.Option.TRUNK_DATA, chest);
      ConfigModule.vehicleDataConfig.save();
   }

   private void handleLanguageMenu() {
      if ((Boolean)LanguageUtils.languageCheck.getOrDefault(this.player.getUniqueId(), false)) {
         this.player.sendMessage(TextUtils.colorize("&cThe language settings have not changed because the menu is closed. Do you want to change this anyway? Execute /vehicle language"));
      }

   }

   private void handleVoucherRedeemMenu() {
      VehicleVoucherListener.voucher.remove(this.player);
   }
}
