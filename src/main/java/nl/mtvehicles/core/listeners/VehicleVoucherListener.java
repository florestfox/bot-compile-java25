package nl.mtvehicles.core.listeners;

import java.util.HashMap;
import nl.mtvehicles.core.events.VehicleVoucherEvent;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class VehicleVoucherListener extends MTVListener {
   public static HashMap<Player, String> voucher = new HashMap();
   private static Inventory cachedInventory;

   public VehicleVoucherListener() {
      super(new VehicleVoucherEvent());
      createVoucherInventory();
   }

   public static void createVoucherInventory() {
      Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, InventoryTitle.VOUCHER_REDEEM_MENU.getStringTitle());
      inv.setItem(11, ItemUtils.getMenuItem("RED_WOOL", "WOOL", (short)14, 1, "&c" + ConfigModule.messagesConfig.getMessage(Message.CANCEL), "&7" + ConfigModule.messagesConfig.getMessage(Message.CANCEL_ACTION), "&7" + ConfigModule.messagesConfig.getMessage(Message.CANCEL_VOUCHER)));
      inv.setItem(15, ItemUtils.getMenuItem("LIME_WOOL", "WOOL", (short)5, 1, "&a" + ConfigModule.messagesConfig.getMessage(Message.CONFIRM), "&7" + ConfigModule.messagesConfig.getMessage(Message.CONFIRM_ACTION), "&7" + ConfigModule.messagesConfig.getMessage(Message.CONFIRM_VOUCHER)));
      cachedInventory = inv;
   }

   @EventHandler
   public void onVoucherRedeem(PlayerInteractEvent event) {
      this.event = event;
      this.player = event.getPlayer();
      Action action = event.getAction();
      ItemStack item = event.getItem();
      if (item != null && item.getType() == Material.PAPER) {
         NBTItem nbt = new NBTItem(item);
         if (nbt.hasTag("mtvehicles.item")) {
            String carUUID = nbt.getString("mtvehicles.item");
            VehicleVoucherEvent api = (VehicleVoucherEvent)this.getAPI();
            api.setVoucherUUID(carUUID);
            this.callAPI();
            if (!this.isCancelled()) {
               carUUID = api.getVoucherUUID();
               if (event.getHand() != EquipmentSlot.HAND) {
                  event.setCancelled(true);
                  this.player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND));
               } else {
                  if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
                     voucher.put(this.player, carUUID);
                     this.player.openInventory(cachedInventory);
                  }

               }
            }
         }
      }
   }
}
